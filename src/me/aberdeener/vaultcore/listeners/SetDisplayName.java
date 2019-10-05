package me.aberdeener.vaultcore.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.aberdeener.vaultcore.VaultCore;

public class SetDisplayName implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		
		Player player = e.getPlayer();
		String groupPrefix = VaultCore.getChat().getPlayerPrefix(player);
		String prefix = ChatColor.translateAlternateColorCodes('&', groupPrefix);
		String name = player.getName();
		
		player.setDisplayName(prefix + name);
		
		player.setPlayerListName(player.getDisplayName());
		
        ScoreBoard.scoreboard(player);
	}
}