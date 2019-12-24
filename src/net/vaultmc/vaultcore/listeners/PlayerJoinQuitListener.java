package net.vaultmc.vaultcore.listeners;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class PlayerJoinQuitListener implements Listener {

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

        String groupPrefix = VaultCore.getChat().getPlayerPrefix(player);
        String prefix = ChatColor.translateAlternateColorCodes('&', groupPrefix);

        player.setDisplayName(prefix + username);
        player.setPlayerListName(player.getDisplayName());

        if (VaultCore.getInstance().getPlayerData().get("players." + player.getUniqueId() + ".settings.msg") == null) {
            VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.msg", true);
            VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.pwc", true);
            VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.tpa", true);
            VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.autotpa", true);
            VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.cycle", true);
            VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.swearfilter",
                    true);
            VaultCore.getInstance().savePlayerData();
        }

        join.setJoinMessage(
                ChatColor.YELLOW + VaultCoreAPI.getName(player) + ChatColor.YELLOW + " has " + ChatColor.GREEN + "joined" + ChatColor.YELLOW + ".");

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
        long lastseen = System.currentTimeMillis();
        long playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        String rank = VaultCore.getChat().getPrimaryGroup(player);
        String ip = player.getAddress().getAddress().getHostAddress();

        quit.setQuitMessage(
                ChatColor.YELLOW + VaultCoreAPI.getName(player) + ChatColor.YELLOW + " has " + ChatColor.RED + "left" + ChatColor.YELLOW + ".");
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