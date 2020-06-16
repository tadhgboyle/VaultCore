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

package net.vaultmc.vaultcore.tour;

import net.vaultmc.vaultloader.VaultLoader;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class TourMusic extends BukkitRunnable {
    private static final List<Double> music = Arrays.asList(
            Math.pow(2, -8D / 12D),  // Si
            Math.pow(2, 1D / 12D),  // Sol
            Math.pow(2, -3D / 12D), // Mi
            -1D,
            Math.pow(2, -8D / 12D),  // Si
            Math.pow(2, 1D / 12D),  // Sol
            Math.pow(2, -3D / 12D), // Mi
            -1D,
            Math.pow(2, 1D / 12D), // Sol
            -1D,
            Math.pow(2, 4D / 12D), // Si
            Math.pow(2, 3D / 12D), // La
            Math.pow(2, 4D / 12D), // Si
            Math.pow(2, 6D / 12D), // Do
            Math.pow(2, 4D / 12D), // Si
            -1D,
            Math.pow(2, -8D / 12D),  // Si
            Math.pow(2, 1D / 12D),  // Sol
            Math.pow(2, -3D / 12D), // Mi
            -1D,
            Math.pow(2, -8D / 12D),  // Si
            Math.pow(2, 1D / 12D),  // Sol
            Math.pow(2, -3D / 12D), // Mi
            -1D,
            Math.pow(2, 1D / 12D), // Sol
            -1D,
            Math.pow(2, 4D / 12D), // Si
            Math.pow(2, 3D / 12D), // La
            Math.pow(2, 4D / 12D), // Si
            Math.pow(2, 6D / 12D), // Do
            Math.pow(2, 4D / 12D), // Si
            -1D,
            Math.pow(2, 4D / 12D), // Si
            Math.pow(2, 9D / 12D), // Mi
            Math.pow(2, 8D / 12D), // Re
            Math.pow(2, 8D / 12D), // Re
            Math.pow(2, -1D / 12D), // Fa
            Math.pow(2, -1D / 12D), // Fa
            -1D,
            Math.pow(2, 2D / 12D), // La
            Math.pow(2, 6D / 12D), // Do
            Math.pow(2, 4D / 12D), // Si
            Math.pow(2, 4D / 12D), // Si
            Math.pow(2, -3D / 12D), // Mi
            Math.pow(2, -3D / 12D), // Mi
            -1D,
            Math.pow(2, 1D / 12D), // Sol
            Math.pow(2, 4D / 12D), // Si
            Math.pow(2, 2D / 12D), // La
            -1D,
            Math.pow(2, -6D / 12D), // Do
            Math.pow(2, -3D / 12D), // Mi
            Math.pow(2, -4D / 12D), // Re
            -1D,
            Math.pow(2, 1D / 12D), // Sol
            Math.pow(2, -1D / 12D), // Fa
            Math.pow(2, -3D / 12D), // Mi
            -1D
    );
    private static Iterator<Double> it;

    public TourMusic() {
        runTaskTimer(VaultLoader.getInstance(), 5L, 5L);
    }

    @Override
    public void run() {
        if (it == null) it = music.listIterator();
        if (it.hasNext()) {
            double pitch = it.next();
            if (pitch == -1D) return;
            for (UUID uuid : Tour.getTouringPlayers()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) {
                    continue;
                }
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100F, (float) pitch);
            }
        } else {
            it = music.listIterator();
        }
    }
}
