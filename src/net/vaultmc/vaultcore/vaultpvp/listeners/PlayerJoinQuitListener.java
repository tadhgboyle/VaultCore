package net.vaultmc.vaultcore.vaultpvp.listeners;

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    private String string = "&e";

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        VLPlayer player = VLPlayer.getPlayer(event.getPlayer());
        //set dataConfig

        if (!player.getDataConfig().contains("player-coins")) {
            player.getDataConfig().set("player-coins", (double) 0);
            player.saveData();
        }

        VaultCore.getInstance().getDatabase().executeUpdateStatement(
                "INSERT INTO pvp_stats (uuid, username, kills, deaths) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE username=?",
                player.getUniqueId().toString(), player.getName(), 0, 0, player.getName());
    }


}
