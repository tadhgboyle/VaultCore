package net.vaultmc.vaultcore.runnables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.DBConnection;

public class Statistics {

	public static void statistics() throws SQLException {

		if (Bukkit.getOnlinePlayers().toArray().length != 0) {

			long timestamp = System.currentTimeMillis();

			double tps = Bukkit.getServer().getTPS()[0];

			int onlinePlayers = Bukkit.getOnlinePlayers().toArray().length;

			List<Integer> pingList = new ArrayList<Integer>();

			for (Player players : Bukkit.getOnlinePlayers()) {
				int ping = players.spigot().getPing();
				pingList.add(ping);
			}

			double average_ping = pingList.stream().mapToInt(val -> val).average().orElse(0);

			DBConnection database = VaultCore.getDatabase();
			
			ResultSet pt = database.executeQueryStatement("SELECT SUM(playtime) AS total_playtime FROM players");
			String total_playtime = null;
			total_playtime = pt.getString(total_playtime);
			
			ResultSet session = database.executeQueryStatement("SELECT AVG(duration) AS average_session FROM sessions");
			String average_session = null;
			average_session = session.getString(average_session);

			database.executeUpdateStatement(
					"INSERT INTO statistics (timestamp, tps, players_online, average_ping, total_playtime, average_session) VALUES (?, ?, ?, ?, ?, ?)",
					timestamp, tps, onlinePlayers, average_ping, total_playtime, average_session);
		}
	}
}
