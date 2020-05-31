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

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.misc.commands.SecLogCommand;
import net.vaultmc.vaultcore.tour.Tour;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.configuration.SQLPlayerData;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
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
    private static final Inventory inv = Bukkit.createInventory(null, 36, "Server Navigator");

    static {

        inv.setItem(0, new ItemStackBuilder(Material.PURPLE_STAINED_GLASS_PANE)
                .name("").build());
        inv.setItem(1, new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE)
                .name("").build());
        inv.setItem(7, new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE)
                .name("").build());
        inv.setItem(8, new ItemStackBuilder(Material.PURPLE_STAINED_GLASS_PANE)
                .name("").build());
        inv.setItem(9, new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE)
                .name("").build());
        inv.setItem(17, new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE)
                .name("").build());
        inv.setItem(18, new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE)
                .name("").build());
        inv.setItem(26, new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE)
                .name("").build());
        inv.setItem(27, new ItemStackBuilder(Material.PURPLE_STAINED_GLASS_PANE)
                .name("").build());
        inv.setItem(28, new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE)
                .name("").build());
        inv.setItem(34, new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE)
                .name("").build());
        inv.setItem(35, new ItemStackBuilder(Material.PURPLE_STAINED_GLASS_PANE)
                .name("").build());

        inv.setItem(10, new ItemStackBuilder(Material.OAK_SAPLING)
                .name(ChatColor.YELLOW + "SkyBlock")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Survive on a tiny island and build up",
                        ChatColor.GRAY + "your own world."
                ))
                .build());

        inv.setItem(12, new ItemStackBuilder(Material.OAK_PLANKS)
                .name(ChatColor.YELLOW + "Creative")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Expand your creativity by building in an ",
                        ChatColor.GRAY + "endless plot-based world."
                ))
                .build());


        inv.setItem(14, new ItemStackBuilder(Material.GRASS_BLOCK)
                .name(ChatColor.YELLOW + "Survival")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Enjoy a partially vanilla survival experience",
                        ChatColor.GRAY + "with custom items. Custom items does not interfere",
                        ChatColor.GRAY + "with the vanilla experience."
                ))
                .build());

        inv.setItem(16, new ItemStackBuilder(Material.IRON_SWORD)
                .name(ChatColor.YELLOW + "Pvp")
                .lore(Collections.singletonList(
                        ChatColor.GRAY + "Choose a kit, and fight to the death with other players!"
                ))
                .build());

        inv.setItem(20, new ItemStackBuilder(Material.WHEAT)
                .name(ChatColor.YELLOW + "Kingdoms")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Build a kingdom, and recruit some members to help you build!",
                        ChatColor.GRAY + "After you're done building, fight with other kingdoms to",
                        ChatColor.GRAY + "receive a reward!"
                ))
                .build());

        inv.setItem(22, new ItemStackBuilder(Material.RED_BANNER)
                .name(ChatColor.YELLOW + "Factions")
                .lore(Collections.singletonList(
                        ChatColor.GRAY + "Factions, but simplified."
                ))
                .build());

        inv.setItem(24, new ItemStackBuilder(Material.BARRIER)
                .name(ChatColor.YELLOW + "Exit")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Click to close this menu."
                ))
                .build());
    }

    @EventHandler
    public void onNavigatorInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() instanceof PlayerInventory) return;
        if (e.getInventory() == inv) {
            VLPlayer player = VLPlayer.getPlayer((Player) e.getWhoClicked());
            if (!Tour.getTouringPlayers().contains(player.getUniqueId())) {
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
                        player.getPlayer().performCommand("pvp");
                        break;

                    case 20:
                        player.performCommand("kd spawn");
                        break;

                    case 22:
                        SQLPlayerData data = player.getPlayerData();
                        if (!data.contains("locations.clans")) {
                            player.teleport(Bukkit.getWorld("clans").getSpawnLocation());
                        } else {
                            player.teleport(Utilities.deserializeLocation(data.getString("locations.clans")));
                        }
                        break;
                    case 24:
                        player.getPlayer().closeInventory();
                        break;
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().getWorld().getName().equals("Lobby")) {
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () ->
                    e.getPlayer().getInventory().setItem(3, paper));
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().equals("Lobby")) {
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () ->
                    e.getPlayer().getInventory().setItem(3, paper));
        } else {
            e.getPlayer().getInventory().clear(3);
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
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (SecLogCommand.getLoggingPlayers().containsKey(e.getPlayer().getUniqueId()) || SecLogCommand.getResetingPlayers().containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            return;
        }

        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
                && e.getPlayer().getInventory().getItemInMainHand().hasItemMeta() &&
                (ChatColor.GREEN + "Server Navigator").equals(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()) &&
                e.getHand() == EquipmentSlot.HAND) {
            e.getPlayer().openInventory(inv);
        }
    }
}
