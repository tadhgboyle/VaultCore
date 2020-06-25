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

package net.vaultmc.vaultcore.pvp;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class KitGuis extends ConstructorRegisterListener {
    public static Multimap<VLPlayer, String> hasKit = HashMultimap.create();
    public static Map<VLPlayer, Map<Kit, Long>> delays = new HashMap<>();

    public static void save() {
        for (VLPlayer player : hasKit.keySet()) {
            player.getDataConfig().set("has-kits", new ArrayList<>(hasKit.get(player)));
            player.saveData();
        }
        for (VLPlayer player : delays.keySet()) {
            player.getDataConfig().createSection("delays");
            for (Kit kit : delays.get(player).keySet()) {
                player.getDataConfig().set("delays." + kit.name, delays.get(player).get(kit));
            }
            player.saveData();
        }
    }

    public static boolean canUse(VLPlayer p, Kit kit) {
        if (!delays.containsKey(p)) return true;
        if (!delays.get(p).containsKey(kit)) return true;
        return (System.currentTimeMillis() / 1000) >= delays.get(p).get(kit);
    }

    public static void openKitGui(VLPlayer p) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.YELLOW + "Kit Selector");

        for (Kit kit : Kit.getKits()) {
            if (hasKit.get(p).contains(kit.name)) {
                inv.addItem(new ItemStackBuilder(Material.ENCHANTED_BOOK)
                        .name(ChatColor.DARK_GREEN + kit.name)
                        .lore(Arrays.asList(
                                ChatColor.YELLOW + "Price: " + ChatColor.GOLD + (kit.price == 0 ? "Free" : "$" + kit.price),
                                ChatColor.YELLOW + "Status: " + ChatColor.GREEN + "Purchased",
                                ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + Utilities.humanReadableTime(kit.delay),
                                canUse(p, kit) ? ChatColor.GREEN + "Left click to use" : ChatColor.RED + "This kit is currently unavailable"
                        ))
                        .build());
            } else {
                inv.addItem(new ItemStackBuilder(Material.ENCHANTED_BOOK)
                        .name(ChatColor.DARK_GREEN + kit.name)
                        .lore(Arrays.asList(
                                ChatColor.YELLOW + "Price: " + ChatColor.GOLD + (kit.price == 0 ? "Free" : "$" + kit.price),
                                ChatColor.YELLOW + "Status: " + ChatColor.GOLD + "Available",
                                ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + Utilities.humanReadableTime(kit.delay),
                                ChatColor.AQUA + "Left click to purchase"
                        ))
                        .build());
            }
        }

        inv.setItem(8, new ItemStackBuilder(Material.BARRIER)
                .name(ChatColor.RED + "Exit").build());
        p.openInventory(inv);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        hasKit.putAll(player, player.getDataConfig().contains("has-kits") ? player.getDataConfig().getStringList("has-kits") : new ArrayList<>());
        Map<Kit, Long> map = new HashMap<>();
        if (player.getDataConfig().contains("delays")) {
            for (String kit : player.getDataConfig().getConfigurationSection("delays").getKeys(false)) {
                map.put(Kit.getKits().stream().filter(k -> k.name.equalsIgnoreCase(kit)).collect(Collectors.toList()).get(0),
                        player.getDataConfig().getLong("delays." + kit));
            }
            delays.put(player, map);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getWhoClicked().getWorld().getName().equalsIgnoreCase("PvP")) {
            return;
        }

        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Kit Selector")) {
            VLPlayer p = VLPlayer.getPlayer((Player) e.getWhoClicked());
            int slot = e.getSlot();
            e.setCancelled(true);
            Kit kit;
            try {
                kit = Kit.getKits().get(slot);
            } catch (IndexOutOfBoundsException ex) {
                return;
            }
            if (!hasKit.get(p).contains(kit.name)) {
                if (canUse(p, kit)) {
                    if (p.hasMoney(Bukkit.getWorld("pvp"), kit.price)) {
                        hasKit.put(p, kit.name);
                        p.closeInventory();
                        p.sendMessage(ChatColor.YELLOW + "Successfully purchased kit!");
                        openKitGui(p);
                    } else {
                        p.sendMessage(ChatColor.RED + "You don't have enough money!");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "This kit is currently unavailable! You must wait " +
                            Utilities.humanReadableTime(delays.get(p).get(kit) - (System.currentTimeMillis() / 1000)) + " before using it.");
                }
            } else {
                for (PotionEffectType type : PotionEffectType.values()) {
                    p.getPlayer().removePotionEffect(type);
                }
                p.sendMessage(ChatColor.YELLOW + "Received kit " + ChatColor.GOLD + kit.name + ChatColor.YELLOW + ".");
                p.getInventory().addItem(kit.items.toArray(new ItemStack[0]));
                Map<Kit, Long> l = delays.containsKey(p) ? delays.get(p) : new HashMap<>();
                l.put(kit, (System.currentTimeMillis() / 1000) + kit.delay);
                delays.put(p, l);
            }
        }
    }
}
