package net.vaultmc.vaultcore.runnables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.listeners.PlayerJoinQuitListener;
import net.vaultmc.vaultloader.utils.DBConnection;

public class Statistics {

	static DBConnection database = VaultCore.getDatabase();

	public static void statistics() throws SQLException {

		long timestamp = System.currentTimeMillis();

		double tps = Bukkit.getServer().getTPS()[0];

		int players_online = Bukkit.getOnlinePlayers().toArray().length;

		List<Integer> pingList = new ArrayList<Integer>();

		for (Player players : Bukkit.getOnlinePlayers()) {
			int ping = players.spigot().getPing();
			pingList.add(ping);
		}

		double average_ping = pingList.stream().mapToInt(val -> val).average().orElse(0);

		ResultSet pt = database.executeQueryStatement("SELECT SUM(playtime) AS total_playtime FROM players");
		int total_playtime = 0;
		if (pt.next()) {
			total_playtime = pt.getInt("total_playtime");
		}

		ResultSet session = database.executeQueryStatement(
				"SELECT AVG(duration) AS average_session, COUNT(session_id) AS total_sessions FROM sessions");
		String average_session = null;
		String total_sessions = null;
		if (session.next()) {
			average_session = session.getString("average_session");
			total_sessions = session.getString("total_sessions");
		}

		String total_players = PlayerJoinQuitListener.count();

		database.executeUpdateStatement(
				"INSERT INTO statistics (timestamp, tps, players_online, average_ping, total_playtime, average_session, total_sessions, total_players) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
				timestamp, tps, players_online, average_ping, total_playtime, average_session, total_sessions,
				total_players);
	}
}
