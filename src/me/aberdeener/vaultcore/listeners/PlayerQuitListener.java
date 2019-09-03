package me.aberdeener.vaultcore.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.aberdeener.vaultcore.VaultCore;

public class PlayerQuitListener implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent quit) {
		
		// define variables
		Player player = quit.getPlayer();

		String uuid = player.getUniqueId().toString();
		
		long firstSeen = player.getFirstPlayed();

		long lastSeen = player.getLastPlayed();
		
		String lastIp = player.getAddress().getAddress().getHostAddress().toString();

		String lastWorld = player.getWorld().getName().toString();

		String lastRank = VaultCore.getChat().getPrimaryGroup(player).toString();
				
		// now place into data.yml
		VaultCore.getInstance().getPlayerData().set("players." + uuid + ".firstSeen", firstSeen);

		VaultCore.getInstance().getPlayerData().set("players." + uuid + ".lastSeen", lastSeen);
		
		VaultCore.getInstance().getPlayerData().set("players." + uuid + ".lastIp", lastIp);

		VaultCore.getInstance().getPlayerData().set("players." + uuid + ".lastWorld", lastWorld);

		VaultCore.getInstance().getPlayerData().set("players." + uuid + ".lastRank", lastRank);
				
		VaultCore.getInstance().savePlayerData();

		quit.setQuitMessage(
				ChatColor.YELLOW + player.getName() + " has " + ChatColor.RED + "left" + ChatColor.YELLOW + ".");

	}
}
