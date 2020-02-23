package net.vaultmc.vaultcore.survival.home;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.commands.wrappers.WrappedSuggestion;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

@RootCommand(
        literal = "home",
        description = "Warps to your home."
)
@Permission(Permissions.Home)
@PlayerOnly
public class HomeCommand extends CommandExecutor implements Listener {
    private static final Map<UUID, BukkitTask> warpTasks = new HashMap<>();

    public HomeCommand() {
        register("home", Collections.emptyList());
        register("homeHome", Collections.singletonList(Arguments.createArgument("name", Arguments.word())));
        VaultCore.getInstance().registerEvents(this);
    }

    @TabCompleter(
            subCommand = "homeHome",
            argument = "name"
    )
    public List<WrappedSuggestion> suggestions(VLPlayer sender, String remaining) {
        return sender.getPlayerData().getKeys().stream().filter(p -> p.startsWith("home.")).map(s ->
                new WrappedSuggestion(s.replaceFirst("home.", ""))).collect(Collectors.toList());
    }

    @SubCommand("home")
    public void home(VLPlayer sender) {
        homeHome(sender, "home");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (warpTasks.containsKey(e.getPlayer().getUniqueId())) {
            warpTasks.get(e.getPlayer().getUniqueId()).cancel();
            e.getPlayer().sendTitle(ChatColor.RED + "Task Cancelled", "", 10, 70, 20);
            warpTasks.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (warpTasks.containsKey(e.getPlayer().getUniqueId())) {
            warpTasks.get(e.getPlayer().getUniqueId()).cancel();
            warpTasks.remove(e.getPlayer().getUniqueId());
        }
    }

    @SubCommand("homeHome")
    public void homeHome(VLPlayer sender, String home) {
        if (!sender.getPlayerData().contains("home." + home)) {
            sender.sendMessage(VaultLoader.getMessage("home.no-home").replace("{HOME}", home));
            return;
        }
        warpTasks.put(sender.getUniqueId(), Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
            sender.teleport(Utilities.deserializeLocation(sender.getPlayerData().getString("home." + home)));
            sender.getPlayer().sendTitle(ChatColor.YELLOW + "Warped to " + ChatColor.GOLD + home, "", 10, 70, 20);
            warpTasks.remove(sender.getUniqueId());
        }, 100L));
        sender.getPlayer().sendTitle(ChatColor.YELLOW + "Warping to " + ChatColor.GOLD + home + ChatColor.YELLOW + " in 5",
                ChatColor.YELLOW + "Do not move or turn", 10, 70, 20);
    }
}
