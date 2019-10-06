package me.aberdeener.vaultcore.listeners;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.aberdeener.vaultcore.VaultCore;

public class PlayerJoinQuitListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent join) throws SQLException {

		Player player = join.getPlayer();
		String uuid = player.getUniqueId().toString();
		String username = player.getName();
		long firstseen = player.getFirstPlayed();
		long lastseen = player.getLastPlayed();
		long playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
		String rank = VaultCore.getChat().getPrimaryGroup(player).toString();
		String ip = player.getAddress().getAddress().getHostAddress().toString();

		join.setJoinMessage(
				ChatColor.YELLOW + player.getName() + " has " + ChatColor.GREEN + "joined" + ChatColor.YELLOW + ".");

		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				VaultCore.getInstance().getConfig().getString("welcome-message")));
		query(uuid, username, firstseen, lastseen, playtime, rank, ip);
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent quit) throws SQLException {

		Player player = quit.getPlayer();
		String uuid = player.getUniqueId().toString();
		String username = player.getName();
		long firstseen = player.getFirstPlayed();
		long lastseen = player.getLastPlayed();
		long playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
		String rank = VaultCore.getChat().getPrimaryGroup(player).toString();
		String ip = player.getAddress().getAddress().getHostAddress().toString();

		quit.setQuitMessage(
				ChatColor.YELLOW + player.getName() + " has " + ChatColor.RED + "left" + ChatColor.YELLOW + ".");
		query(uuid, username, firstseen, lastseen, playtime, rank, ip);
	}
	private void query(String uuid, String username, long firstseen, long lastseen, long playtime, String rank,
			String ip) throws SQLException {
		VaultCore.getInstance().connection.createStatement()
				.executeUpdate("INSERT INTO players (uuid, username, firstseen, lastseen, playtime, rank, ip) VALUES ('"
						+ uuid + "', '" + username + "', " + firstseen + ", " + lastseen + ", " + playtime + ", '"
						+ rank + "', '" + ip + "') ON DUPLICATE KEY UPDATE username='" + username + "', lastseen="
						+ lastseen + ", playtime=" + playtime + ", rank='" + rank + "', ip='" + ip + "';");
	}
}