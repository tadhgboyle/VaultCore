package net.vaultmc.vaultcore.listeners;

import lombok.Getter;
import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.ReloadPersistentStorage;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.ResultSet;
import java.util.HashMap;

public class PlayerJoinQuitListener implements Listener {
	static DBConnection database = VaultCore.getDatabase();
	@SuppressWarnings("unchecked")
	@Getter
	private static HashMap<String, String> session_ids = VaultCore.isReloaded
			? (HashMap<String, String>) ReloadPersistentStorage.get("sessionIds", VaultCore.getInstance())
			: new HashMap<>();
	@SuppressWarnings("unchecked")
	@Getter
	private static HashMap<String, Long> session_duration = VaultCore.isReloaded
			? (HashMap<String, Long>) ReloadPersistentStorage.get("sessionDuration", VaultCore.getInstance())
			: new HashMap<>();
	String string = Utilities.string;
	String variable2 = Utilities.variable2;

	@SneakyThrows
	public static String count() {
		String total_players = null;
		ResultSet rs = database.executeQueryStatement("SELECT COUNT(uuid) FROM players");
		while (rs.next()) {
			total_players = rs.getString(1);
		}
		return total_players;
	}

	@EventHandler
	@SneakyThrows
	public void onJoin(PlayerJoinEvent join) {
		VLPlayer player = VLPlayer.getPlayer(join.getPlayer());
		String uuid = player.getUniqueId().toString();
		String username = player.getName();
		long firstseen = player.getFirstPlayed();
		long lastseen = System.currentTimeMillis();
		long playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
		String rank = player.getGroup();
		String ip = player.getAddress().getAddress().getHostAddress();

		String prefix = ChatColor.translateAlternateColorCodes('&', player.getPrefix());

		String session_id = RandomStringUtils.random(8, true, true);
		long start_time = System.currentTimeMillis();

		session_ids.put(uuid, session_id);
		session_duration.put(session_id, lastseen);

		player.getPlayer().setDisplayName(prefix + username);
		player.getPlayer().setPlayerListName(player.getDisplayName());

		playerDataQuery(uuid, username, firstseen, lastseen, playtime, rank, ip);

		sessionQuery(session_id, uuid, username, ip, 0, start_time, 0);

		if (!player.getDataConfig().contains("settings")) {
			player.getDataConfig().set("settings.msg", true);
			player.getDataConfig().set("settings.tpa", true);
			player.getDataConfig().set("settings.autotpa", false);
			player.getDataConfig().set("settings.cycle", false);
			player.getDataConfig().set("settings.swearfilter", true);
			player.saveData();

			for (Player players : Bukkit.getOnlinePlayers()) {
				players.sendMessage(string + "Welcome " + player.getName() + " to VaultMC! (" + variable2 + "#"
						+ count() + string + ")");
			}
		}
		join.setJoinMessage(player.getFormattedName() + string + " has " + ChatColor.GREEN + "joined" + string + ".");
		player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				VaultCore.getInstance().getConfig().getString("welcome-message")));
	}

	@EventHandler
	@SneakyThrows
	public void onQuit(PlayerQuitEvent quit) {
		VLPlayer player = VLPlayer.getPlayer(quit.getPlayer());
		String uuid = player.getUniqueId().toString();
		long lastseen = System.currentTimeMillis();
		long playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
		String rank = player.getGroup();

		String session_id = session_ids.get(uuid);
		long duration = System.currentTimeMillis() - session_duration.get(session_id);
		long end_time = System.currentTimeMillis();

		sessionQuery(session_id, "", "", "", duration, 0, end_time);
		session_ids.remove(uuid);

		quit.setQuitMessage(player.getFormattedName() + string + " has " + ChatColor.RED + "left" + string + ".");
		playerDataQuery(uuid, "", 0, lastseen, playtime, rank, "");
	}

	@SneakyThrows
	private void playerDataQuery(String uuid, String username, long firstseen, long lastseen, long playtime,
			String rank, String ip) {
		database.executeUpdateStatement(
				"INSERT INTO players (uuid, username, firstseen, lastseen, playtime, rank, ip) VALUES ("
						+ "?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE uuid=?, lastseen=?, playtime=?, rank=?",
				uuid, username, firstseen, lastseen, playtime, rank, ip, uuid, lastseen, playtime, rank);
	}

	@SneakyThrows
	private void sessionQuery(String session_id, String uuid, String username, String ip, long duration,
			long start_time, long end_time) {
		database.executeUpdateStatement(
				"INSERT INTO sessions (session_id, uuid, username, ip, start_time) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE duration=?, end_time=?",
				session_id, uuid, username, ip, start_time, duration, end_time);
	}
}
