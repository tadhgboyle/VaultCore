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

package net.vaultmc.vaultcore.cosmetics;

import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;

public class Cosmetics {
    public Cosmetics() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(VaultLoader.getInstance(), this::tickCosmetics, 1, 1);
    }

    public void tickCosmetics() {
        for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
            //Cosmetic.FIRE_RING.getTick().accept(player);
            //Cosmetic.DRIP_LAVA_PARTICLE_PACK.getTick().accept(player);
        }
    }
}
