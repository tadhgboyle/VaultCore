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
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

@AllArgsConstructor
public enum Cosmetic {
    FLAME_RING(new Consumer<VLPlayer>() {
        @Override
        public void accept(VLPlayer player) {
            t += Math.PI / 32;
            double x = Math.cos(t) * 0.8;
            if (x == 0.8) {
                t = 0;
            }
            double y = player.getPlayer().getEyeLocation().getY() - 1;
            double z = Math.sin(t) * 0.8;
            player.getWorld().spawnParticle(Particle.DRIP_LAVA, player.getLocation().getX() + x,
                    y, player.getLocation().getZ() + z, 3, null);
        }

        private double t = 0;
    }, Material.FIRE_CHARGE, "Flame Ring"),

    DRIP_LAVA_PARTICLE_PACK(new Consumer<VLPlayer>() {
        @Override
        public void accept(VLPlayer player) {
            t += Math.PI / 10;
            double x = Math.cos(t) * 0.5;
            if (x == 0.5) {
                t = 0;
            }
            double y = player.getPlayer().getEyeLocation().getY() + 0.2;
            double z = Math.sin(t) * 0.5;
            player.getWorld().spawnParticle(Particle.DRIP_LAVA, player.getLocation().getX() + x,
                    y, player.getLocation().getZ() + z, 2, null);
        }

        private double t = 0;
    }, Material.LAVA_BUCKET, "Drip Lava Particle Pack"),

    DRIP_WATER_PARTICLE_PACK(new Consumer<VLPlayer>() {
        @Override
        public void accept(VLPlayer player) {
            t += Math.PI / 10;
            double x = Math.cos(t) * 0.5;
            if (x == 0.5) {
                t = 0;
            }
            double y = player.getPlayer().getEyeLocation().getY() + 0.2;
            double z = Math.sin(t) * 0.5;
            player.getWorld().spawnParticle(Particle.DRIP_WATER, player.getLocation().getX() + x,
                    y, player.getLocation().getZ() + z, 2, null);
        }

        private double t = 0;
    }, Material.WATER_BUCKET, "Drip Water Particle Pack"),

    HEART_PARTICLE_PACK(player -> {
        player.getWorld().spawnParticle(Particle.HEART, player.getLocation().getX() +
                        ThreadLocalRandom.current().nextDouble(-0.5D, 0.5D), player.getPlayer().getEyeLocation().getY() + 0.2,
                player.getLocation().getZ() + ThreadLocalRandom.current().nextDouble(-0.5D, 0.5D), 1, null);
    }, Material.RED_WOOL, "Heart Particle Pack"),

    ENCHANTING_TABLE_PARTICLE_PACK(player -> {
        player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation().getX() +
                        ThreadLocalRandom.current().nextDouble(-1D, 1D), player.getPlayer().getEyeLocation().getY() -
                        ThreadLocalRandom.current().nextDouble(0D, 1D),
                player.getLocation().getZ() + ThreadLocalRandom.current().nextDouble(-1D, 1D), 1, null);
    }, Material.ENCHANTING_TABLE, "Enchanting Table Particle Pack"),

    SLIME_PARTICLE_PACK(player -> {
        player.getWorld().spawnParticle(Particle.SLIME, player.getLocation().getX() +
                        ThreadLocalRandom.current().nextDouble(-1D, 1D), player.getPlayer().getEyeLocation().getY() -
                        ThreadLocalRandom.current().nextDouble(0D, 1D),
                player.getLocation().getZ() + ThreadLocalRandom.current().nextDouble(-1D, 1D), 1, null);
    }, Material.SLIME_BALL, "Slime Particle Pack"),

    CRITICAL_ATTACK_PARTICLE_PACK(player -> {
        player.getWorld().spawnParticle(Particle.CRIT, player.getLocation().getX() +
                        ThreadLocalRandom.current().nextDouble(-1D, 1D), player.getPlayer().getEyeLocation().getY() -
                        ThreadLocalRandom.current().nextDouble(0D, 1D),
                player.getLocation().getZ() + ThreadLocalRandom.current().nextDouble(-1D, 1D), 1, null);
    }, Material.DIAMOND_SWORD, "Critcal Attack Particle Pack"),

    MAGIC_PARTICLE_PACK(player -> {
        player.getWorld().spawnParticle(Particle.SPELL_WITCH, player.getLocation().getX() +
                        ThreadLocalRandom.current().nextDouble(-1D, 1D), player.getPlayer().getEyeLocation().getY() -
                        ThreadLocalRandom.current().nextDouble(0D, 1D),
                player.getLocation().getZ() + ThreadLocalRandom.current().nextDouble(-1D, 1D), 1, null);
    }, Material.POTION, "Magic Particle Pack"),

    DRIPPING_HONEY_PARTICLE_PACK(new Consumer<VLPlayer>() {
        @Override
        public void accept(VLPlayer player) {
            t += Math.PI / 10;
            double x = Math.cos(t) * 0.5;
            if (x == 0.5) {
                t = 0;
            }
            double y = player.getPlayer().getEyeLocation().getY() + 0.2;
            double z = Math.sin(t) * 0.5;
            player.getWorld().spawnParticle(Particle.DRIPPING_HONEY, player.getLocation().getX() + x,
                    y, player.getLocation().getZ() + z, 2, null);
        }

        private double t = 0;
    }, Material.HONEY_BLOCK, "Honey Particle Pack"),

    BUBBLE_PARTICLE_PACK(player -> {
        player.getWorld().spawnParticle(Particle.BUBBLE_POP, player.getLocation().getX() +
                        ThreadLocalRandom.current().nextDouble(-1D, 1D), player.getPlayer().getEyeLocation().getY() -
                        ThreadLocalRandom.current().nextDouble(0D, 1D),
                player.getLocation().getZ() + ThreadLocalRandom.current().nextDouble(-1D, 1D), 1, null);
    }, Material.LILY_PAD, "Bubble Particle Pack"),

    DRAGON_BREATH_PARTICLE_PACK(player -> {
        player.getWorld().spawnParticle(Particle.DRAGON_BREATH, player.getLocation().getX(), player.getPlayer().getEyeLocation().getY() - 1,
                player.getLocation().getZ(), 1, null);
    }, Material.DRAGON_HEAD, "Dragon Breath Particle Pack"),

    END_ROD_PARTICLE_PACK(player -> {
        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation().getX() +
                        ThreadLocalRandom.current().nextDouble(-1D, 1D), player.getPlayer().getEyeLocation().getY() -
                        ThreadLocalRandom.current().nextDouble(0D, 1D),
                player.getLocation().getZ() + ThreadLocalRandom.current().nextDouble(-1D, 1D), 1, null);
    }, Material.END_ROD, "End Rod Particle Pack"),

    ACTIONBAR_JOIN_MESSAGE(null, Material.FIREWORK_ROCKET, "Floating Join Message");

    @Getter
    private final Consumer<VLPlayer> tick;
    @Getter
    private final Material item;
    @Getter
    private final String name;

    public String getPermission() {
        return Permissions.CosmeticsCommand + "." + toString().replace("_", "").toLowerCase();
    }
}
