package net.vaultmc.vaultcore.teleport;

import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerTPListener implements Listener {
    public static HashMap<UUID, Location> teleports = new HashMap<>();

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        teleports.put(event.getPlayer().getUniqueId(), event.getFrom());
        if (event.getFrom().getWorld().getName().equals("Survival") || (event.getFrom().getWorld().getName().equals("Creative"))) {
            Location from = event.getFrom();
            if (event.getFrom().getWorld().equals(event.getTo().getWorld())) {
                return;
            }
            VLPlayer player = VLPlayer.getPlayer(event.getPlayer());
            if (event.getFrom().getWorld().getName().equals("Survival")) {
                player.getDataConfig().set("locations.sv", from);
            } else if (event.getFrom().getWorld().getName().equals("Creative")) {
                player.getDataConfig().set("locations.cr", from);
            }
        }
    }
}