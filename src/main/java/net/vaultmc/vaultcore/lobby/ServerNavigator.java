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

package net.vaultmc.vaultcore.lobby;

import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.misc.commands.SecLogCommand;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.Collections;

public class ServerNavigator extends ConstructorRegisterListener {
    private static final ItemStack paper = new ItemStackBuilder(Material.PAPER)
            .name(ChatColor.GREEN + "Server Navigator")
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Easily navigate through worlds",
                    ChatColor.GRAY + "and games on VaultMC."
            ))
            .build();

    private static final ItemStack yellow = new ItemStackBuilder(Material.YELLOW_STAINED_GLASS_PANE)
            .name(" ")
            .build();
    private static final ItemStack orange = new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE)
            .name(" ")
            .build();
    private static final ItemStack building = new ItemStackBuilder(Material.WOODEN_AXE)
            .name(ChatColor.YELLOW + "Building")
            .lore(Arrays.asList(
                    ChatColor.GRAY + "For development only.",
                    ChatColor.GRAY + "Do not disclose."
            ))
            .build();
    private static final ItemStack skyblock = new ItemStackBuilder(Material.OAK_SAPLING)
            .name(ChatColor.YELLOW + "SkyBlock")
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Survive on a tiny island and build up",
                    ChatColor.GRAY + "your own world."
            ))
            .build();
    private static final ItemStack creative = new ItemStackBuilder(Material.OAK_PLANKS)
            .name(ChatColor.YELLOW + "Creative")
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Expand your creativity by building in an ",
                    ChatColor.GRAY + "endless plot-based world."
            ))
            .build();
    private static final ItemStack survival = new ItemStackBuilder(Material.GRASS_BLOCK)
            .name(ChatColor.YELLOW + "Survival")
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Enjoy a partially vanilla survival",
                    ChatColor.GRAY + "experience, with custom items."
            ))
            .build();
    private static final ItemStack pvp = new ItemStackBuilder(Material.IRON_SWORD)
            .name(ChatColor.YELLOW + "PvP")
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Choose a kit, and fight to",
                    ChatColor.GRAY + "the death with other players!"
            ))
            .build();
    private static final ItemStack stayTuned = new ItemStackBuilder(Material.WHEAT)
            .name(ChatColor.YELLOW + "Stay Tuned!")
            .lore(Collections.singletonList(
                    ChatColor.GRAY + "This game is temporarily unavailable."
            ))
            .build();
    private static final ItemStack factions = new ItemStackBuilder(Material.RED_BANNER)
            .name(ChatColor.YELLOW + "Factions")
            .lore(Collections.singletonList(
                    ChatColor.GRAY + "Factions, but simplified."
            ))
            .build();
    private static final ItemStack exit = new ItemStackBuilder(Material.BARRIER)
            .name(ChatColor.YELLOW + "Exit")
            .lore(Collections.singletonList(
                    ChatColor.GRAY + "Click to close this menu."
            ))
            .build();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().getWorld().getName().equals("Lobby")) {
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () ->
                    e.getPlayer().getInventory().setItem(1, paper));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().equals("Lobby")) {
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () ->
                    e.getPlayer().getInventory().setItem(1, paper));
        } else {
            e.getPlayer().getInventory().clear(1);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Server Navigator"))
            e.setCancelled(true);
    }

    @EventHandler
    public void onClickCompass(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (e.getClickedInventory() instanceof PlayerInventory && e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Server Navigator"))
            e.setCancelled(true);
    }

    @EventHandler
    public void onNavigatorInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() instanceof PlayerInventory) return;
        if (e.getView().getTitle().equals(ChatColor.RESET + "Server Navigator")) {
            VLPlayer player = VLPlayer.getPlayer((Player) e.getWhoClicked());
            switch (e.getSlot()) {
                case 10:
                    player.getPlayer().performCommand("is");
                    break;
                case 12:
                    player.getPlayer().performCommand("cr");
                    break;
                case 14:
                    player.getPlayer().performCommand("sv");
                    break;
                case 16:
                    //player.getPlayer().performCommand("pvp");
                    break;
                case 22:
                        /*
                        SQLPlayerData data = player.getPlayerData();
                        if (!data.contains("locations.clans")) {
                            player.teleport(Bukkit.getWorld("clans").getSpawnLocation());
                        } else {
                            player.teleport(Utilities.deserializeLocation(data.getString("locations.clans")));
                        }
                         */
                    break;
                case 24:
                    if (player.hasPermission(Permissions.BuilderAccess)) {
                        player.teleport(Bukkit.getWorld("build").getSpawnLocation());
                    }
                    player.getPlayer().closeInventory();
                    break;
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (SecLogCommand.getLoggingPlayers().containsKey(e.getPlayer().getUniqueId()) || SecLogCommand.getResetingPlayers().containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            return;
        }

        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
                && e.getPlayer().getInventory().getItemInMainHand().hasItemMeta() &&
                (ChatColor.GREEN + "Server Navigator").equals(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()) &&
                e.getHand() == EquipmentSlot.HAND) {
            Inventory inv = Bukkit.createInventory(null, 36, ChatColor.RESET + "Server Navigator");
            inv.setItem(0, yellow);
            inv.setItem(1, orange);
            inv.setItem(7, orange);
            inv.setItem(8, yellow);
            inv.setItem(9, orange);
            inv.setItem(17, orange);
            inv.setItem(18, orange);
            inv.setItem(26, orange);
            inv.setItem(27, yellow);
            inv.setItem(28, orange);
            inv.setItem(34, orange);
            inv.setItem(35, yellow);

            inv.setItem(10, skyblock);
            inv.setItem(12, creative);
            inv.setItem(14, survival);
            inv.setItem(16, stayTuned);
            inv.setItem(20, stayTuned);
            inv.setItem(22, stayTuned);
            if (e.getPlayer().hasPermission(Permissions.BuilderAccess)) {
                inv.setItem(24, building);
            } else {
                inv.setItem(24, exit);
            }
            e.getPlayer().openInventory(inv);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if (e.getTo().getWorld().getName().toLowerCase().contains("build") && !e.getPlayer().hasPermission(Permissions.BuilderAccess)) {
            e.setCancelled(true);
        }
    }
}
