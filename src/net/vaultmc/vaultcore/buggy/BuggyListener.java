package net.vaultmc.vaultcore.buggy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class BuggyListener implements Runnable, Listener {
    static final Map<UUID, Stage> stages = new HashMap<>();
    static final Map<UUID, Bug> bugs = new HashMap<>();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        stages.remove(e.getPlayer().getUniqueId());
        bugs.remove(e.getPlayer().getUniqueId());
        Bukkit.getScheduler().runTaskTimer(VaultLoader.getInstance(), this, 0L, 12000L);
    }

    @EventHandler
    public void annoyAssignedPlayers(PlayerJoinEvent e) {
        int bugs = 0;
        for (Bug bug : Bug.getBugs()) {
            if (bug.getAssignees().stream().map(VLOfflinePlayer::getUniqueId).collect(Collectors.toSet()).contains(e.getPlayer().getUniqueId()) &&
                    (bug.getStatus() == Bug.Status.OPEN || bug.getStatus() == Bug.Status.REOPENED)) {
                bugs++;
            }
        }
        if (bugs > 0) {
            e.getPlayer().sendMessage(VaultLoader.getMessage("buggy.total-assigned").replace("{BUGS}", String.valueOf(bugs)));
        }
    }

    @EventHandler
    public void onPlayerCreateBug(AsyncPlayerChatEvent e) {
        if (stages.containsKey(e.getPlayer().getUniqueId())) {
            VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
            if (e.getMessage().trim().equalsIgnoreCase("CANCEL")) {
                player.sendMessage(VaultLoader.getMessage("buggy.cancelled"));
                e.setCancelled(true);
                stages.remove(e.getPlayer().getUniqueId());
                bugs.remove(e.getPlayer().getUniqueId());
                return;
            }
            e.setCancelled(true);
            switch (stages.get(e.getPlayer().getUniqueId())) {
                case TITLE:
                    bugs.get(player.getUniqueId()).setTitle(e.getMessage());
                    player.sendMessage(VaultLoader.getMessage("buggy.description"));
                    stages.put(player.getUniqueId(), Stage.DESCRIPTION);
                    break;
                case DESCRIPTION:
                    bugs.get(player.getUniqueId()).setDescription(e.getMessage());
                    player.sendMessage(VaultLoader.getMessage("buggy.expected-behavior"));
                    stages.put(player.getUniqueId(), Stage.EXPECTED_BEHAVIOR);
                    break;
                case EXPECTED_BEHAVIOR:
                    bugs.get(player.getUniqueId()).setExpectedBehavior(e.getMessage());
                    player.sendMessage(VaultLoader.getMessage("buggy.actual-behavior"));
                    stages.put(player.getUniqueId(), Stage.ACTUAL_BEHAVIOR);
                    break;
                case ACTUAL_BEHAVIOR:
                    bugs.get(player.getUniqueId()).setActualBehavior(e.getMessage());
                    player.sendMessage(VaultLoader.getMessage("buggy.steps-to-reproduce"));
                    stages.put(player.getUniqueId(), Stage.STEPS_TO_REPRODUCE);
                    break;
                case STEPS_TO_REPRODUCE:
                    if (e.getMessage().trim().equals("!")) {
                        player.sendMessage(VaultLoader.getMessage("buggy.additional-information"));
                        stages.put(player.getUniqueId(), Stage.ADDITIONAL_INFORMATION);
                        break;
                    }
                    bugs.get(player.getUniqueId()).getStepsToReproduce().add(e.getMessage());
                    player.sendMessage(VaultLoader.getMessage("buggy.type-another-or-at"));
                    break;
                case ADDITIONAL_INFORMATION:
                    bugs.get(player.getUniqueId()).setAdditionalInformation(e.getMessage());
                    player.sendMessage(VaultLoader.getMessage("buggy.should-hide"));
                    stages.put(player.getUniqueId(), Stage.SHOULD_HIDE);
                    break;
                case SHOULD_HIDE:
                    Bug bug = bugs.get(player.getUniqueId());
                    player.sendMessage(VaultLoader.getMessage("buggy.finished").replace("{UID}", bug.getUniqueId()));
                    stages.remove(player.getUniqueId());
                    bug.setStatus(Bug.Status.OPEN);
                    if (e.getMessage().equalsIgnoreCase("Yes")) {
                        bug.setHidden(true);
                    }
                    bug.serialize();
                    Bug.getBugs().add(bug);
                    Bug.save();
                    bugs.remove(player.getUniqueId());
                    break;
            }
        }
    }

    @Override
    public void run() {
        try {
            Map<VLPlayer, Integer> map = new HashMap<>();
            for (Bug bug : Bug.getBugs()) {
                if (bug.getStatus() == Bug.Status.OPEN || bug.getStatus() == Bug.Status.REOPENED) {
                    for (VLOfflinePlayer assignee : bug.getAssignees()) {
                        if (assignee.isOnline()) {
                            int i = map.getOrDefault(assignee.getOnlinePlayer(), 0);
                            i++;
                            map.put(assignee.getOnlinePlayer(), i);
                        }
                    }
                }
            }

            for (Map.Entry<VLPlayer, Integer> entry : map.entrySet()) {
                entry.getKey().sendMessage(VaultLoader.getMessage("buggy.total-assigned").replace("{BUGS}", String.valueOf(entry.getValue())));
            }
        } catch (ConcurrentModificationException ignored) {
        }
    }

    @AllArgsConstructor
    public enum Stage {
        TITLE("buggy.stage.title"),
        DESCRIPTION("buggy.stage.description"),
        ACTUAL_BEHAVIOR("buggy.stage.actual-behavior"),
        EXPECTED_BEHAVIOR("buggy.stage.expected-behavior"),
        STEPS_TO_REPRODUCE("buggy.stage.steps-to-reproduce"),
        ADDITIONAL_INFORMATION("buggy.stage.additional-information"),
        SHOULD_HIDE("buggy.stage.should-hide");

        @Getter
        private final String key;
    }
}
