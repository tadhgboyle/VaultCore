package net.vaultmc.vaultcore.pvp.listeners;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class PlayerDeathListener extends ConstructorRegisterListener implements Runnable {
    private static final Set<UUID> cooldown = new HashSet<>();

    public PlayerDeathListener() {
        Bukkit.getScheduler().runTaskTimer(VaultLoader.getInstance(), this, 40L, 40L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (!e.getPlayer().getWorld().getName().equalsIgnoreCase("PvP")) {
            return;
        }

        cooldown.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent e) {
        if (!e.getPlayer().getWorld().getName().equalsIgnoreCase("Pvp")) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    @SneakyThrows
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!e.getEntity().getPlayer().getWorld().getName().equalsIgnoreCase("Pvp")) {
            return;
        }

        if (cooldown.contains(e.getEntity().getUniqueId())) return;
        VLPlayer damaged = VLPlayer.getPlayer(e.getEntity());
        if (e.getEntity().getKiller() != null) {
            VLPlayer damager = VLPlayer.getPlayer(e.getEntity().getKiller());

            if (damaged == damager) {
                damaged.sendMessage(ChatColor.RED + "You killed yourself. Interesting.");
                return;
            }

            damaged.teleport(Bukkit.getWorld("world").getSpawnLocation());

            Random r = new Random();
            int low = 10;
            int high = 30;
            int result = r.nextInt(high - low) + low;
            int kills = damager.getPlayerData().getInt("stats.kills", 0);
            int deaths = damaged.getPlayerData().getInt("stats.deaths", 0);
            damager.deposit(Bukkit.getWorld("pvp"), result);
            damager.sendMessage(ChatColor.YELLOW + "You killed " + damaged.getFormattedName() + ChatColor.YELLOW + " and received " + ChatColor.DARK_GREEN + "$" + (double) result + ChatColor.YELLOW + ".");

            Bukkit.broadcastMessage(damaged.getFormattedName() + ChatColor.YELLOW + " was killed by " + damager.getFormattedName() + ChatColor.YELLOW + ".");

            damager.getPlayerData().set("stats.kills", kills + 1);
            damaged.getPlayerData().set("stats.deaths", deaths + 1);

            damaged.saveData();

            kills++;
            deaths++;

            // Handle damager
            ResultSet rsDamager = VaultCore.getDatabase().executeQueryStatement("SELECT * FROM pvp_stats WHERE uuid=?", damager.getUniqueId().toString());
            if (rsDamager.next()) {
                VaultCore.getDatabase().executeUpdateStatement("UPDATE pvp_stats SET kills=? WHERE uuid=?", kills, damager.getUniqueId().toString());
            } else {
                VaultCore.getDatabase().executeUpdateStatement("INSERT INTO pvp_stats (uuid, username, kills, deaths) VALUES (?, ?, ?, ?)",
                        damager.getUniqueId().toString(), damager.getName(), kills, 0);
            }
            rsDamager.close();

            // Handler damaged
            ResultSet rsDamaged = VaultCore.getDatabase().executeQueryStatement("SELECT * FROM pvp_stats WHERE uuid=?", damaged.getUniqueId().toString());
            if (rsDamaged.next()) {
                VaultCore.getDatabase().executeUpdateStatement("UPDATE pvp_stats SET deaths=? WHERE uuid=?", deaths, damaged.getUniqueId().toString());
            } else {
                VaultCore.getDatabase().executeUpdateStatement("INSERT INTO pvp_stats (uuid, username, kills, deaths) VALUES (?, ?, ?, ?)",
                        damaged.getUniqueId().toString(), damaged.getName(), 0, deaths);
            }
            rsDamaged.close();

            damager.saveData();
            damaged.saveData();
        }
        cooldown.add(e.getEntity().getUniqueId());
    }

    @Override
    public void run() {
        cooldown.clear();
    }
}
