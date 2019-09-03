package me.aberdeener.vaultcore.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.aberdeener.vaultcore.VaultCore;
import me.aberdeener.vaultcore.runnables.ScoreBoard;

public class SetDisplayName implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		String groupPrefix = VaultCore.getChat().getPlayerPrefix(player);
		String prefix = ChatColor.translateAlternateColorCodes('&', groupPrefix);
		String name = player.getName();
		
		// set their chat displayName
		player.setDisplayName(prefix + name);
		
		// set their tab list name to their displayName above
		player.setPlayerListName(player.getDisplayName());
		
		// sort them in tab class
        ScoreBoard.scoreboard(player);
	}
}