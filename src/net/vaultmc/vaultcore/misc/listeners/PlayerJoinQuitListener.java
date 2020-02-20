package net.vaultmc.vaultcore.misc.listeners;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.sql.ResultSet;

public class PlayerJoinQuitListener implements Listener {
    static DBConnection database = VaultCore.getDatabase();

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
    public void preLogin(AsyncPlayerPreLoginEvent e) {
        if (e.getName().equalsIgnoreCase("vaultmc")) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Username not allowed (Error: Reserved Username)");
        }
    }

    @EventHandler
    @SneakyThrows
    public void onJoin(PlayerJoinEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        if (player.getWorld().getName().equalsIgnoreCase("Lobby")) {
            player.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
        }
        String uuid = player.getUniqueId().toString();
        String username = player.getName();
        long firstSeen = player.getFirstPlayed();
        long lastSeen = System.currentTimeMillis();
        long playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        String rank = player.getGroup();
        String ip = player.getAddress().getAddress().getHostAddress();

        playerDataQuery(uuid, username, firstSeen, lastSeen, playtime, rank, ip);

        if (!player.getPlayerData().contains("settings.msg")) {
            player.getPlayerData().set("settings.msg", true);
            player.getPlayerData().set("settings.tpa", true);
            player.getPlayerData().set("settings.autotpa", false);
            player.getPlayerData().set("settings.cycle", false);
            player.saveData();

            for (Player players : Bukkit.getOnlinePlayers()) {
                players.sendMessage(
                        Utilities.formatMessage(VaultLoader.getMessage("vaultcore.listeners.joinquit.new_player"),
                                player.getFormattedName(), count()));
            }
        }
        e.setJoinMessage(
                Utilities.formatMessage(VaultLoader.getMessage("vaultcore.listeners.joinquit.event_message"),
                        player.getFormattedName(), ChatColor.GREEN + "joined"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                VaultCore.getInstance().getConfig().getString("welcome-message")));

        File directory = new File(VaultCore.getInstance().getDataFolder() + "/schems/" + player.getUniqueId().toString() + "/");
        if (!directory.exists()) {
            directory.mkdir();
            directory.setExecutable(true);
            directory.setReadable(true);
            directory.setWritable(true);
        }
    }

    @EventHandler
    @SneakyThrows
    public void onQuit(PlayerQuitEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        String uuid = player.getUniqueId().toString();
        long lastSeen = System.currentTimeMillis();
        long playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        String rank = player.getGroup();
        String ip = player.getAddress().getAddress().getHostAddress();

        e.setQuitMessage(
                Utilities.formatMessage(VaultLoader.getMessage("vaultcore.listeners.joinquit.event_message"),
                        player.getFormattedName(), ChatColor.RED + "left"));
        playerDataQuery(uuid, "", 0, lastSeen, playtime, rank, ip);
    }

    @SneakyThrows
    private void playerDataQuery(String uuid, String username, long firstseen, long lastseen, long playtime,
                                 String rank, String ip) {
        database.executeUpdateStatement(
                "INSERT INTO players (uuid, username, firstseen, lastseen, playtime, rank, ip) VALUES ("
                        + "?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE uuid=?, lastseen=?, playtime=?, rank=?, ip=?",
                uuid, username, firstseen, lastseen, playtime, rank, ip, uuid, lastseen, playtime, rank, ip);
    }

}
