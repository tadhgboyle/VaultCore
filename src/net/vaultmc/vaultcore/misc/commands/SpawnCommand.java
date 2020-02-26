package net.vaultmc.vaultcore.misc.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RootCommand(
        literal = "spawn",
        description = "Teleports you to the world spawn."
)
@Permission(Permissions.SpawnCommand)
@PlayerOnly
public class SpawnCommand extends CommandExecutor implements Listener {
    private static final Map<UUID, BukkitTask> tasks = new HashMap<>();

    public SpawnCommand() {
        register("spawn", Collections.emptyList());
        VaultCore.getInstance().registerEvents(this);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (tasks.containsKey(e.getPlayer().getUniqueId())) {
            tasks.get(e.getPlayer().getUniqueId()).cancel();
            tasks.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (tasks.containsKey(e.getPlayer().getUniqueId())) {
            e.getPlayer().sendTitle(ChatColor.RED + "Task Cancelled", "", 10, 70, 20);
            tasks.get(e.getPlayer().getUniqueId()).cancel();
            tasks.remove(e.getPlayer().getUniqueId());
        }
    }

    @SubCommand("spawn")
    public void spawn(VLPlayer sender) {
        if (sender.getWorld().getName().equalsIgnoreCase("skyblock")) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.spawn.disabled"));
            return;
        }
        sender.getPlayer().sendTitle(ChatColor.YELLOW + "Warping to world spawn in " + ChatColor.GOLD + "5",
                ChatColor.YELLOW + "Do not move or turn", 10, 70, 20);
        tasks.put(sender.getUniqueId(), Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
            sender.teleport(sender.getWorld().getSpawnLocation());
            sender.getPlayer().sendTitle(ChatColor.YELLOW + "Warped to world spawn", "", 10, 70, 20);
            tasks.remove(sender.getUniqueId());
        }, 100L));
    }
}
