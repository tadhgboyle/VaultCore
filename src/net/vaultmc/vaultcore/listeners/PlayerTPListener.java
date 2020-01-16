package net.vaultmc.vaultcore.listeners;

import net.vaultmc.vaultcore.VaultCore;
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
            if (event.getFrom().getWorld().getName().equals("Survival")) {
                VaultCore.getInstance().getLocationFile().set("players." + event.getPlayer().getUniqueId() + ".sv", from);
                VaultCore.getInstance().saveLocations();
            } else if (event.getFrom().getWorld().getName().equals("Creative")) {
                VaultCore.getInstance().getLocationFile().set("players." + event.getPlayer().getUniqueId() + ".cr", from);
                VaultCore.getInstance().saveLocations();
            }
        }
    }
}