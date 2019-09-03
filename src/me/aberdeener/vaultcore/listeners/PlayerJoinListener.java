package me.aberdeener.vaultcore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.aberdeener.vaultcore.VaultCore;
import net.md_5.bungee.api.ChatColor;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent join) {

		Player player = join.getPlayer();

		join.setJoinMessage(
				ChatColor.YELLOW + player.getName() + " has " + ChatColor.GREEN + "joined" + ChatColor.YELLOW + ".");

		// send message from config
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				VaultCore.getInstance().getConfig().getString("welcome-message")));

		String uuid = player.getUniqueId().toString();

		String username = player.getName().toString();

		if (VaultCore.getInstance().getPlayerData().get("players." + uuid) == null) {

			VaultCore.getInstance().getPlayerData().createSection("players." + uuid);

			VaultCore.getInstance().getPlayerData().set("players." + uuid + ".username", username);

			VaultCore.getInstance().savePlayerData();
		}
	}
}