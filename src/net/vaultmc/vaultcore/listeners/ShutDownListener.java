package net.vaultmc.vaultcore.listeners;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import net.vaultmc.vaultcore.runnables.Statistics;

public class ShutDownListener implements Listener {

	@EventHandler
	public void onShutDownPlayer(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().equals("/stop")) {
			event.setCancelled(true);
			kickAll();
		}
	}

	@EventHandler
	public void onShutDownConsole(ServerCommandEvent event) {
		if (event.getCommand().equals("stop")) {
			event.setCancelled(true);
			kickAll();
		}
	}

	public void kickAll() {
		for (Player players : Bukkit.getOnlinePlayers()) {
			players.kickPlayer(ChatColor.RED + "VaultMC is shutting down for maintenance... Be right back!");
		}
		try {
			Bukkit.getConsoleSender().sendMessage("Saving statistics to database...");
			Statistics.statistics();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.shutdown();
	}
}
