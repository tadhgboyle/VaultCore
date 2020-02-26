package net.vaultmc.vaultcore.survival.claim;

import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ClaimCharger extends BukkitRunnable {
    public ClaimCharger() {
        runTaskTimer(VaultLoader.getInstance(), 0L, 6000L /* 5 min */);
    }

    @Override
    public void run() {
        World world = Bukkit.getWorld("Survival");
        for (File file : VaultLoader.getPlayerDataFolder().listFiles()) {
            VLOfflinePlayer player = VLOfflinePlayer.getOfflinePlayer(
                    UUID.fromString(file.getName().replace(".yml", "")));
            if (System.currentTimeMillis() - player.getPlayerData().getLong("last-claim-paid") >= 86400000 /* 1 Day */) {
                List<String> claims = player.getPlayerData().getStringList("claim.chunks");
                if (player.hasMoney(world, claims.size() * 5)) {
                    player.withdraw(world, claims.size() * 5);
                } else {
                    long maxCanAfford = (long) Math.floor(player.getBalance(world) / 5);
                    long toRemove = claims.size() - maxCanAfford;
                    Iterator<String> it = claims.iterator();
                    for (int i = 0; i < toRemove; i++) {
                        if (it.hasNext()) {
                            it.next();
                            it.remove();
                        } else {
                            break;
                        }
                    }
                    player.getPlayerData().set("claim.chunks", claims);
                    player.sendOrScheduleMessage(VaultLoader.getMessage("vaultcore.commands.claim.claim-removed"
                            .replace("{COUNT}", String.valueOf(toRemove))));
                }

                player.getPlayerData().set("last-claim-paid", System.currentTimeMillis());
            }
        }
    }
}
