package net.vaultmc.vaultcore.runnables;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.DBConnection;

public class Statistics {

	public static void statistics() {

		long timestamp = System.currentTimeMillis();

		double tps = Bukkit.getServer().getTPS()[0];

		int onlinePlayers = Bukkit.getOnlinePlayers().toArray().length;

		List<Integer> pingList = new ArrayList<Integer>();

		for (Player players : Bukkit.getOnlinePlayers()) {
			int ping = players.spigot().getPing();
			pingList.add(ping);
		}

		double pingAverage = pingList.stream().mapToInt(val -> val).average().orElse(0);

		DBConnection database = VaultCore.getDatabase();

		database.executeUpdateStatement("INSERT INTO statistics (timestamp, tps, players_online, average_ping) VALUES (?, ?, ?, ?)",
				timestamp, tps, onlinePlayers, pingAverage);
	}

}
