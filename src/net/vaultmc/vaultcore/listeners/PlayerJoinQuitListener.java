package net.vaultmc.vaultcore.listeners;

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultloader.utils.DBConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerJoinQuitListener implements Listener {

	String string = Utilities.string;
	String variable1 = Utilities.variable1;
	String variable2 = Utilities.variable2;
	DBConnection database = VaultCore.getDatabase();

	@EventHandler
	public void onJoin(PlayerJoinEvent join) throws SQLException {

		Player player = join.getPlayer();
		String uuid = player.getUniqueId().toString();
		String username = player.getName();
		long firstseen = player.getFirstPlayed();
		long lastseen = System.currentTimeMillis();
		long playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
		String rank = VaultCore.getChat().getPrimaryGroup(player);
		String ip = player.getAddress().getAddress().getHostAddress();

		String prefix = ChatColor.translateAlternateColorCodes('&', VaultCore.getChat().getPlayerPrefix(player));

		player.setDisplayName(prefix + username);
		player.setPlayerListName(player.getDisplayName());

		query(uuid, username, firstseen, lastseen, playtime, rank, ip);

		if (VaultCore.getInstance().getPlayerData().get("players." + player.getUniqueId() + ".settings.msg") == null) {
			VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.msg", true);
			VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.tpa", true);
			VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.autotpa", true);
			VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.cycle", true);
			VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.swearfilter",
					true);
			VaultCore.getInstance().savePlayerData();
			for (Player players : Bukkit.getOnlinePlayers()) {
				players.sendMessage(string + "Welcome " + player.getName() + " to VaultMC! (" + variable2 + "#"
						+ count() + string + ")");
			}
		}

		join.setJoinMessage(VaultCoreAPI.getName(player) + ChatColor.YELLOW + " has " + ChatColor.GREEN + "joined"
				+ ChatColor.YELLOW + ".");

		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				VaultCore.getInstance().getConfig().getString("welcome-message")));
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent quit) throws SQLException {

		Player player = quit.getPlayer();
		String uuid = player.getUniqueId().toString();
		String username = player.getName();
		long firstseen = player.getFirstPlayed();
		long lastseen = System.currentTimeMillis();
		long playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
		String rank = VaultCore.getChat().getPrimaryGroup(player);
		String ip = player.getAddress().getAddress().getHostAddress();

		quit.setQuitMessage(ChatColor.YELLOW + VaultCoreAPI.getName(player) + ChatColor.YELLOW + " has " + ChatColor.RED
				+ "left" + ChatColor.YELLOW + ".");
		query(uuid, username, firstseen, lastseen, playtime, rank, ip);
	}

	private void query(String uuid, String username, long firstseen, long lastseen, long playtime, String rank,
			String ip) throws SQLException {
		database.executeUpdateStatement(
				"INSERT INTO players (uuid, username, firstseen, lastseen, playtime, rank, ip) VALUES ("
						+ "?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE username=?, lastseen=?, playtime=?, rank=?, ip=?",
				uuid, username, firstseen, lastseen, playtime, rank, ip, username, lastseen, playtime, rank, ip);
	}

	private String count() throws SQLException {
		String total_players = null;
		ResultSet rs = database.executeQueryStatement("SELECT COUNT(uuid) FROM players");
		while (rs.next()) {
			total_players = rs.getString(1);
		}
		return total_players;
	}
}