package net.vaultmc.vaultcore.misc.listeners;

import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class LobbyPortals extends ConstructorRegisterListener {
    private static final Location out = new Location(Bukkit.getWorld("Lobby"), 176.5, 110, -5.5, -90, 0);

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getTo().getBlockX() >= 62 && e.getTo().getBlockY() >= 110 && e.getTo().getBlockZ() >= -11 &&
                e.getTo().getBlockX() <= 63 && e.getTo().getBlockY() <= 117 && e.getTo().getBlockZ() <= 0) {
            e.getPlayer().performCommand("sv");
        } else if (e.getTo().getBlockX() >= 116 && e.getTo().getBlockY() >= 110 && e.getTo().getBlockZ() >= 52 &&
                e.getTo().getBlockX() <= 127 && e.getTo().getBlockY() <= 117 && e.getTo().getBlockZ() <= 53) {
            e.getPlayer().performCommand("cr");
        } else if (e.getTo().getBlockX() >= 179 && e.getTo().getBlockX() >= 110 && e.getTo().getBlockZ() >= -12 &&
                e.getTo().getBlockX() <= 180 && e.getTo().getBlockY() <= 117 && e.getTo().getBlockZ() <= -1) {
            e.getPlayer().performCommand("is");
            e.getPlayer().teleport(out);
        }
    }
}
