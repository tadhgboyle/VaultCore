/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

package net.vaultmc.vaultcore.survival;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class TheEndReset extends BukkitRunnable {
    @Override
    public void run() {
        long lastReset = VaultCore.getInstance().getData().getLong("survival-end-last-reset", System.currentTimeMillis());
        if (System.currentTimeMillis() - lastReset <= 86400000 /* 1 day */) {
            for (Player player : Bukkit.getWorld("Survival_the_end").getPlayers()) {
                player.sendMessage(VaultLoader.getMessage("vaultcore.survival.end_resetting"));
            }
            VaultCore.getInstance().getData().set("survival-end-last-reset", System.currentTimeMillis());
            Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
                for (Player player : Bukkit.getWorld("Survival_the_end").getPlayers()) {
                    player.sendMessage(VaultLoader.getMessage("vaultcore.survival.end_resetting-2"));
                }
            }, 1200);
            Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
                for (Player player : Bukkit.getWorld("Survival_the_end").getPlayers()) {
                    player.sendMessage(VaultLoader.getMessage("vaultcore.survival.end_resetting-3"));
                }
            }, 2400);
            Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
                for (Player player : Bukkit.getWorld("Survival_the_end").getPlayers()) {
                    player.sendMessage(VaultLoader.getMessage("vaultcore.survival.end_reset"));
                    player.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
                }

                Bukkit.unloadWorld("Survival_the_end", false);

                File worldsFolder = new File(VaultLoader.getInstance().getDataFolder().getParentFile().getParentFile(), "worlds");
                File end = new File(worldsFolder, "Survival_the_end").getAbsoluteFile();

                try {
                    if (end.exists()) FileUtils.deleteDirectory(end);
                    FileUtils.copyDirectory(new File(worldsFolder, ".Survival_end_bk"), new File(worldsFolder, "Survival_the_end"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                Bukkit.createWorld(new WorldCreator("Survival_the_end").environment(World.Environment.THE_END));
            }, 3600);
        }
    }
}
