package net.vaultmc.vaultcore.misc.listeners;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.misc.commands.NicknameCommand;
import net.vaultmc.vaultcore.settings.PlayerCustomColours;
import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;

public class PlayerJoinQuitListener implements Listener {

    static DBConnection database = VaultCore.getDatabase();
    SessionHandler sessionHandler = new SessionHandler();

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
        e.setJoinMessage(null);
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
        sessionHandler.newSession(player);

        if (NicknameCommand.getNickname(player) != null) {
            Bukkit.getPlayer(player.getUniqueId()).setDisplayName(ChatColor.ITALIC + player.getPlayerData().getString("nickname"));
        }

        File directory = new File(VaultCore.getInstance().getDataFolder() + "/schems/" + player.getUniqueId().toString() + "/");
        if (!directory.exists()) {
            directory.mkdir();
            directory.setExecutable(true);
            directory.setReadable(true);
            directory.setWritable(true);
        }
        if (!PlayerSettings.getSetting(player, "minimal_messages"))
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    VaultCore.getInstance().getConfig().getString("welcome-message")));

        PlayerCustomColours.setColours(player, PlayerCustomColours.getColoursFromFile(player));

        for (VLPlayer players : VLPlayer.getOnlinePlayers()) {
            if (PlayerSettings.getSetting(players, "settings.minimal_messages")) continue;
            players.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.listeners.joinquit.event_message"), players,
                    player.getFormattedName(), ChatColor.GREEN + "joined"));
        }
    }

    @EventHandler
    @SneakyThrows
    public void onQuit(PlayerQuitEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        e.setQuitMessage(null);
        String uuid = player.getUniqueId().toString();
        String username = player.getName();
        long lastSeen = System.currentTimeMillis();
        long playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        String rank = player.getGroup();
        String ip = player.getAddress().getAddress().getHostAddress();

        PlayerCustomColours.saveColoursToFile(player);

        playerDataQuery(uuid, username, 0, lastSeen, playtime, rank, ip);
        sessionHandler.endSession(player);

        for (VLPlayer players : VLPlayer.getOnlinePlayers()) {
            if (PlayerSettings.getSetting(players, "settings.minimal_messages")) continue;
            players.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.listeners.joinquit.event_message"), players,
                    player.getFormattedName(), ChatColor.RED + "left"));
        }
    }

    @SneakyThrows
    private void playerDataQuery(String uuid, String username, long firstseen, long lastseen, long playtime,
                                 String rank, String ip) {
        database.executeUpdateStatement(
                "INSERT INTO players (uuid, username, firstseen, lastseen, playtime, rank, ip) VALUES ("
                        + "?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE uuid=?, username=?, lastseen=?, playtime=?, rank=?, ip=?",
                uuid, username, firstseen, lastseen, playtime, rank, ip, uuid, username, lastseen, playtime, rank, ip);
    }
}
