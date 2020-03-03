package net.vaultmc.vaultcore.misc.commands;

import com.google.common.hash.Hashing;
import lombok.Getter;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

@RootCommand(
        literal = "seclog",
        description = "Secure your login on VaultMC with a password."
)
@PlayerOnly
public class SecLogCommand extends CommandExecutor implements Listener {
    private static final Location auth = new Location(Bukkit.getWorld("Lobby"), 185.5, 120, 125.5, -90F, 90F);
    private static final Set<UUID> setPrompt = new HashSet<>();
    private static final Map<UUID, String> setConfirmPrompt = new HashMap<>();
    private static final Set<UUID> unsetPrompt = new HashSet<>();
    private static final Set<UUID> resetPrompt = new HashSet<>();
    private static final Set<UUID> resetNewPrompt = new HashSet<>();
    private static final Map<UUID, String> resetConfirmPrompt = new HashMap<>();
    @Getter
    private static final Map<UUID, Location> loggingPlayers = new HashMap<>();

    public SecLogCommand() {
        register("set", Collections.singletonList(Arguments.createLiteral("set")));
        register("unset", Collections.singletonList(Arguments.createLiteral("unset")));
        register("unsetConfirm", Arrays.asList(
                Arguments.createLiteral("unset"),
                Arguments.createLiteral("confirm")
        ));
        register("reset", Collections.singletonList(Arguments.createLiteral("reset")));
        VaultCore.getInstance().registerEvents(this);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        setPrompt.remove(e.getPlayer().getUniqueId());
        setConfirmPrompt.remove(e.getPlayer().getUniqueId());
        unsetPrompt.remove(e.getPlayer().getUniqueId());
        resetPrompt.remove(e.getPlayer().getUniqueId());
        resetNewPrompt.remove(e.getPlayer().getUniqueId());
        resetConfirmPrompt.remove(e.getPlayer().getUniqueId());
        loggingPlayers.remove(e.getPlayer().getUniqueId());
    }

    @SubCommand("set")
    public void set(VLPlayer sender) {
        if (sender.getPlayerData().contains("password")) {
            sender.sendMessage(VaultLoader.getMessage("sec-log.set.already-set"));
        }
        setPrompt.add(sender.getUniqueId());
        sender.sendMessage(VaultLoader.getMessage("sec-log.set.create"));
        sender.sendMessage(VaultLoader.getMessage("sec-log.set.enter-password"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        if (loggingPlayers.containsKey(e.getPlayer().getUniqueId())) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());

        e.getRecipients().removeIf(p -> loggingPlayers.containsKey(p.getUniqueId()));

        if (setPrompt.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            setConfirmPrompt.put(e.getPlayer().getUniqueId(), e.getMessage());
            setPrompt.remove(e.getPlayer().getUniqueId());
            e.getPlayer().sendMessage(VaultLoader.getMessage("sec-log.set.confirm-password"));
        } else if (setConfirmPrompt.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            if (e.getMessage().equals(setConfirmPrompt.remove(e.getPlayer().getUniqueId()))) {
                player.getPlayerData().set("password", Hashing.sha512().hashString(e.getMessage(), StandardCharsets.UTF_8).toString());
                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> e.getPlayer().kickPlayer(VaultLoader.getMessage("sec-log.set.set")));
            } else {
                e.getPlayer().sendMessage(VaultLoader.getMessage("sec-log.set.not-match"));
            }
        } else if (unsetPrompt.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            unsetPrompt.remove(e.getPlayer().getUniqueId());
            if (Hashing.sha512().hashString(e.getMessage(), StandardCharsets.UTF_8).toString().equals(player.getPlayerData().getString("password"))) {
                player.getPlayerData().remove("password");
                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> e.getPlayer().kickPlayer(VaultLoader.getMessage("sec-log.set.set")));
            } else {
                player.sendMessage(VaultLoader.getMessage("sec-log.unset.incorrect-password"));
            }
        } else if (resetPrompt.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            resetPrompt.remove(e.getPlayer().getUniqueId());
            if (Hashing.sha512().hashString(e.getMessage(), StandardCharsets.UTF_8).toString().equals(player.getPlayerData().getString("password"))) {
                player.getPlayerData().remove("password");
                resetNewPrompt.add(player.getUniqueId());
                player.sendMessage(VaultLoader.getMessage("sec-log.reset.enter-new-password"));
            } else {
                player.sendMessage(VaultLoader.getMessage("sec-log.unset.incorrect-password"));
            }
        } else if (resetNewPrompt.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            resetNewPrompt.remove(e.getPlayer().getUniqueId());
            resetConfirmPrompt.put(e.getPlayer().getUniqueId(), e.getMessage());
            player.sendMessage(VaultLoader.getMessage("sec-log.reset.confirm-new-password"));
        } else if (resetConfirmPrompt.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            if (e.getMessage().equals(resetConfirmPrompt.remove(e.getPlayer().getUniqueId()))) {
                player.getPlayerData().set("password", Hashing.sha512().hashString(e.getMessage(), StandardCharsets.UTF_8).toString());
                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> e.getPlayer().kickPlayer(VaultLoader.getMessage("sec-log.set.set")));
            } else {
                player.sendMessage(VaultLoader.getMessage("sec-log.set.not-match"));
            }
        } else if (loggingPlayers.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            if (Hashing.sha512().hashString(ChatColor.stripColor(e.getMessage()), StandardCharsets.UTF_8).toString().equals(player.getPlayerData().getString("password"))) {
                player.sendMessage(VaultLoader.getMessage("sec-log.success"));
                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> player.teleport(loggingPlayers.remove(e.getPlayer().getUniqueId())));
            } else {
                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> e.getPlayer().kickPlayer(VaultLoader.getMessage("sec-log.unset.incorrect-password")));
            }
        }
    }

    @SubCommand("unsetConfirm")
    public void realUnset(VLPlayer sender) {
        if (!sender.getPlayerData().contains("password")) {
            sender.sendMessage(VaultLoader.getMessage("sec-log.unset.not-set"));
            return;
        }

        sender.sendMessage(VaultLoader.getMessage("sec-log.set.enter-password"));
        unsetPrompt.add(sender.getUniqueId());
    }

    @SubCommand("unset")
    public void unset(VLPlayer sender) {
        if (!sender.getPlayerData().contains("password")) {
            sender.sendMessage(VaultLoader.getMessage("sec-log.unset.not-set"));
            return;
        }

        if (sender.getGroup().equalsIgnoreCase("moderator") || sender.getGroup().equalsIgnoreCase("admin")) {
            sender.sendMessage(VaultLoader.getMessage("sec-log.unset.mod"));
        } else {
            sender.sendMessage(VaultLoader.getMessage("sec-log.unset.default"));
        }
    }

    @SubCommand("reset")
    public void reset(VLPlayer sender) {
        if (!sender.getPlayerData().contains("password")) {
            sender.sendMessage(VaultLoader.getMessage("sec-log.unset.not-set"));
            return;
        }
        resetPrompt.add(sender.getUniqueId());
        sender.sendMessage(VaultLoader.getMessage("sec-log.set.enter-password"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (VaultCore.getInstance().getConfig().getString("server").equalsIgnoreCase("vaultmc")) {
            VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
            if ((player.getGroup().equalsIgnoreCase("moderator") || player.getGroup().equalsIgnoreCase("admin")) && !player.getPlayerData().contains("password")) {
                player.sendMessage(VaultLoader.getMessage("sec-log.secure-account"));
            }

            if (player.getPlayerData().contains("password")) {
                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> player.sendMessage(VaultLoader.getMessage("sec-log.set.enter-password")));
                Location loc = player.getLocation().clone();
                player.teleport(auth);
                loggingPlayers.put(player.getUniqueId(), loc);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent e) {
        if (loggingPlayers.containsKey(e.getPlayer().getUniqueId())) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (loggingPlayers.containsKey(e.getPlayer().getUniqueId())) e.setCancelled(true);
    }
}
