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

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class CosmeticsInvListener extends ConstructorRegisterListener {

    public static HashMap<VLPlayer, Particle> particles = new HashMap<>();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        particles.remove(VLPlayer.getPlayer(e.getPlayer()));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getWhoClicked().getUniqueId());

        if (e.getView().getTitle().equals("Cosmetics")) {
            switch (e.getSlot()) {
                case 1:
                    // Particles
                    particleMenu(player);
                    break;
                case 4:
                    // Disguises
                    break;
                case 7:
                    // ???
                    break;
                default:
            }
        } else if (e.getView().getTitle().equals("Cosmetics: Particles")) {
            switch (e.getSlot()) {
                case 1:
                    // Flames
                    e.setCancelled(true);
                    player.closeInventory();
                    if (particles.containsKey(player)) {
                        if (particles.get(player) == Particle.FLAME) {
                            particles.remove(player);
                            player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.cosmetics.particles.toggled"), "Flamboyant Flame", "off"));
                        } else {
                            particles.put(player, Particle.FLAME);
                            player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.cosmetics.particles.toggled"), "Flamboyant Flame", "on"));
                        }
                    } else {
                        particles.put(player, Particle.FLAME);
                        player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.cosmetics.particles.toggled"), "Flamboyant Flame", "on"));
                    }
                    break;
                case 4:
                    // Rainbow
                    e.setCancelled(true);
                    player.closeInventory();
                    if (particles.containsKey(player)) {
                        if (particles.get(player) == Particle.REDSTONE) {
                            particles.remove(player);
                            player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.cosmetics.particles.toggled"), "Ridiculous Rainbow", "off"));
                        } else {
                            particles.put(player, Particle.REDSTONE);
                            player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.cosmetics.particles.toggled"), "Ridiculous Rainbow", "on"));
                        }
                    } else {
                        particles.put(player, Particle.REDSTONE);
                        player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.cosmetics.particles.toggled"), "Ridiculous Rainbow", "on"));
                    }
                    break;
                case 7:
                    // Smoke
                    e.setCancelled(true);
                    player.closeInventory();
                    if (particles.containsKey(player)) {
                        if (particles.get(player) == Particle.SMOKE_LARGE) {
                            particles.remove(player);
                            player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.cosmetics.particles.toggled"), "Smoggy Smoke", "off"));
                        } else {
                            particles.put(player, Particle.SMOKE_LARGE);
                            player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.cosmetics.particles.toggled"), "Smoggy Smoke", "on"));
                        }
                    } else {
                        particles.put(player, Particle.SMOKE_LARGE);
                        player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.cosmetics.particles.toggled"), "Smoggy Smoke", "on"));
                    }
                    break;
                case 22:
                    // Go Back
                    Bukkit.dispatchCommand(Bukkit.getPlayer(player.getUniqueId()), "cosmetics");
                    break;
                default:
            }
        }
    }

    private void particleMenu(VLPlayer player) {
        Inventory particleMenu = Bukkit.createInventory(null, 27, "Cosmetics: Particles");
        particleMenu.setItem(1, new ItemStackBuilder(Material.LAVA_BUCKET)
                .name(ChatColor.GOLD + "Flamboyant " + ChatColor.RED + "Flames")
                .build());
        particleMenu.setItem(4, new ItemStackBuilder(Material.REDSTONE)
                // Make these letters rainbow
                .name(ChatColor.RED + "R" + ChatColor.GOLD + "i" + ChatColor.YELLOW + "d" + ChatColor.GREEN + "i" + ChatColor.BLUE + "c" + ChatColor.LIGHT_PURPLE + "u" + ChatColor.DARK_PURPLE + "l" + ChatColor.RED + "o" + ChatColor.GOLD + "u" + ChatColor.YELLOW + "s  " + ChatColor.GREEN + "R" + ChatColor.GREEN + "a" + ChatColor.BLUE + "i" + ChatColor.LIGHT_PURPLE + "n" + ChatColor.DARK_PURPLE + "b" + ChatColor.RED + "o" + ChatColor.GOLD + "w")
                .build());
        particleMenu.setItem(7, new ItemStackBuilder(Material.COBWEB)
                .name(ChatColor.GRAY + "Smoggy " + ChatColor.DARK_GRAY + "Smoke")
                .build());
        particleMenu.setItem(22, new ItemStackBuilder(Material.BOOK)
                .name(ChatColor.YELLOW + "Go Back...")
                .build());
        player.openInventory(particleMenu);
    }
}
