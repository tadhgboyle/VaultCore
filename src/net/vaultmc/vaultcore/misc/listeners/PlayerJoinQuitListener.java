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
        long firstseen = player.getFirstPlayed();
        long lastseen = System.currentTimeMillis();
        long playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        String rank = player.getGroup();
        String ip = player.getAddress().getAddress().getHostAddress();

        String prefix = ChatColor.translateAlternateColorCodes('&', player.getPrefix());
        String suffix = ChatColor.translateAlternateColorCodes('&', player.getSuffix());

        player.getPlayer().setDisplayName(prefix + username + suffix);
        player.getPlayer().setPlayerListName(player.getDisplayName());

        playerDataQuery(uuid, username, firstseen, lastseen, playtime, rank, ip);

        if (!player.getDataConfig().contains("settings")) {
            player.getDataConfig().set("settings.msg", true);
            player.getDataConfig().set("settings.tpa", true);
            player.getDataConfig().set("settings.autotpa", false);
            player.getDataConfig().set("settings.cycle", false);
            player.getDataConfig().set("settings.swearfilter", true);
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
    public void onQuit(PlayerQuitEvent quit) {
        Player player = quit.getPlayer();
        String uuid = player.getUniqueId().toString();
        long lastseen = System.currentTimeMillis();
        long playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        String rank = VaultCore.getChat().getPrimaryGroup(player);
        String ip = player.getAddress().getAddress().getHostAddress();

        quit.setQuitMessage(
                Utilities.formatMessage(VaultLoader.getMessage("vaultcore.listeners.joinquit.event_message"),
                        player.getDisplayName(), ChatColor.RED + "left"));
        playerDataQuery(uuid, "", 0, lastseen, playtime, rank, ip);
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
