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
import org.bukkit.entity.Player;

public class Cosmetics {
    public Cosmetics() {
        Bukkit.getScheduler().runTaskTimer(VaultLoader.getInstance(), this::tickCosmetics, 1, 1);
    }

    public void tickCosmetics() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Cosmetic.FIRE_RING.getTick().accept(VLPlayer.getPlayer(player));
        }
    }
}
