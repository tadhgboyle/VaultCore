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

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Particle;

import java.util.function.Consumer;

@AllArgsConstructor
public enum Cosmetic {
    FIRE_RING(new Consumer<VLPlayer>() {
        @Override
        public void accept(VLPlayer player) {
            t += Math.PI / 32;
            double x = Math.cos(t) * 0.8;
            double y = player.getPlayer().getEyeLocation().getY() - 1;
            double z = Math.sin(t) * 0.8;
            player.getWorld().spawnParticle(Particle.DRIP_LAVA, player.getLocation().getX() + x,
                    y, player.getLocation().getZ() + z, 3, null);
        }

        private double t = 0;
    });
    @Getter
    private final Consumer<VLPlayer> tick;
}
