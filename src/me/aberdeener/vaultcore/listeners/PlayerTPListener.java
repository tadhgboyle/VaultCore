package me.aberdeener.vaultcore.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.aberdeener.vaultcore.VaultCore;

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
				VaultCore.getInstance().getPlayerData().set("players." + event.getPlayer().getUniqueId() + ".sv", from);
				VaultCore.getInstance().savePlayerData();
			}
			else if (event.getFrom().getWorld().getName().equals("Creative")) {
				VaultCore.getInstance().getPlayerData().set("players." + event.getPlayer().getUniqueId() + ".cr", from);
				VaultCore.getInstance().savePlayerData();
			}
		}
	}
}