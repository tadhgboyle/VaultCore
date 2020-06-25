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

import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;

public class StarterGearExperience extends ConstructorRegisterListener {
    private static final ItemStack[] starterGear = {
            new ItemStack(Material.IRON_SWORD),
            new ItemStack(Material.BOW),
            new ItemStack(Material.STONE_AXE),
            new ItemStack(Material.STONE_PICKAXE),
            new ItemStack(Material.OAK_LOG, 16),
            new ItemStack(Material.IRON_HELMET),
            new ItemStack(Material.IRON_CHESTPLATE),
            new ItemStack(Material.IRON_LEGGINGS),
            new ItemStack(Material.IRON_BOOTS),
            new ItemStack(Material.DIAMOND, 5),
            new ItemStack(Material.ARROW, 32),
            new ItemStackBuilder(Material.COMPASS)
                    .name(ChatColor.YELLOW + "Use /crafting to check crafting recipes")
                    .build()
    };

    @EventHandler
    public void svStarterGear(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().toLowerCase().contains("survival")) {
            VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
            if (!player.getPlayerData().contains("svstartergear")) {
                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> player.getInventory().addItem(starterGear));
                player.getPlayerData().set("svstartergear", true);
            }
        }
    }
}
