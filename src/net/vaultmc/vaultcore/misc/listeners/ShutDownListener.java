package net.vaultmc.vaultcore.misc.listeners;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.stats.Statistics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.net.InetSocketAddress;
import java.net.Socket;

public class ShutDownListener implements Listener {

    @EventHandler
    public void onShutDownPlayer(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equals("/stop")) {
            event.setCancelled(true);
            kickAll();
        }
    }

    // FIXME  Edge case: Ctrl-C
    @EventHandler
    public void onShutDownConsole(ServerCommandEvent event) {
        if (event.getCommand().equals("stop")) {
            event.setCancelled(true);
            kickAll();
        }
    }

    @SneakyThrows
    public void kickAll() {
        try {
            Socket s = new Socket();
            s.connect(new InetSocketAddress("192.168.1.77", 25567), 15);
            s.close();
            VaultCore.getInstance().sendToBackup();
            shutDown(true);
        } catch (Exception e) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.kickPlayer(ChatColor.RED + "VaultMC is shutting down for maintenance... Be right back!");
            }
            shutDown(false);
        }
    }

    @SneakyThrows
    public void shutDown(boolean fallback) {
        if (fallback) {
            Bukkit.getConsoleSender().sendMessage("[VaultCore] Fallback server is ONLINE -- Sending players");
        } else {
            Bukkit.getConsoleSender().sendMessage("[VaultCore] Fallback server is OFFLINE -- Kicking players");
        }
        Bukkit.getConsoleSender().sendMessage("Saving statistics to database...");
        Statistics.statistics();
        Bukkit.shutdown();
    }
}
