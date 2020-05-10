package net.vaultmc.vaultcore.pvp.utils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KitGuis extends ConstructorRegisterListener {
    public static Multimap<VLPlayer, String> hasKit = HashMultimap.create();
    public static Map<VLPlayer, Map<String, Long>> delays = new HashMap<>();

    public static void save() {
        for (VLPlayer player : hasKit.keySet()) {
            player.getDataConfig().set("has-kits", new ArrayList<>(hasKit.get(player)));
            player.saveData();
        }
        for (VLPlayer player : delays.keySet()) {
            player.getDataConfig().createSection("delays");
            for (String kit : delays.get(player).keySet()) {
                player.getDataConfig().set("delays." + kit, delays.get(player).get(kit));
            }
            player.saveData();
        }
    }

    public static boolean canUse(VLPlayer p, String kit) {
        if (!delays.containsKey(p)) return true;
        if (!delays.get(p).containsKey(kit)) return true;
        return System.currentTimeMillis() >= delays.get(p).get(kit);
    }

    public static void openKitGui(VLPlayer p) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.YELLOW + "Kit Selector");

        if (hasKit.get(p).contains("swordsman")) {
            inv.setItem(0, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                    .name(ChatColor.DARK_GREEN + "Swordsman")
                    .lore(Arrays.asList(
                            ChatColor.YELLOW + "Price: " + ChatColor.GOLD + "Free",
                            ChatColor.YELLOW + "Status: " + ChatColor.GREEN + "Owned",
                            ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + "None",
                            ChatColor.GREEN + "Click to use this kit"
                    ))
                    .build());
        } else {
            inv.setItem(0, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                    .name(ChatColor.DARK_GREEN + "Swordsman")
                    .lore(Arrays.asList(
                            ChatColor.YELLOW + "Price: " + ChatColor.GOLD + "Free",
                            ChatColor.YELLOW + "Status: " + ChatColor.RED + "Unowned",
                            ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + "None",
                            ChatColor.RED + "Click to purchase this kit"
                    ))
                    .build());
        }


        if (hasKit.get(p).contains("axeman")) {
            inv.setItem(1, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                    .name(ChatColor.DARK_GREEN + "Axeman")
                    .lore(Arrays.asList(
                            ChatColor.YELLOW + "Price: " + ChatColor.GOLD + "50C ",
                            ChatColor.YELLOW + "Status: " + ChatColor.GREEN + "Owned",
                            ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + "1 minute",
                            canUse(p, "axeman") ? ChatColor.GREEN + "Click to use this kit" :
                                    ChatColor.GOLD + "You must wait a while to use this kit"
                    ))
                    .build());
        } else {
            inv.setItem(1, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                    .name(ChatColor.DARK_GREEN + "Axeman")
                    .lore(Arrays.asList(
                            ChatColor.YELLOW + "Price: " + ChatColor.GOLD + "50C ",
                            ChatColor.YELLOW + "Status: " + ChatColor.RED + "Unowned",
                            ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + "1 minute",
                            ChatColor.RED + "Click to purchase this kit"
                    ))
                    .build());
        }


        if (hasKit.get(p).contains("mage")) {
            inv.setItem(2, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                    .name(ChatColor.DARK_GREEN + "Mage")
                    .lore(Arrays.asList(
                            ChatColor.YELLOW + "Price: " + ChatColor.GOLD + "85C ",
                            ChatColor.YELLOW + "Status: " + ChatColor.GREEN + "Owned",
                            ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + "1 minute",
                            canUse(p, "mage") ? ChatColor.GREEN + "Click to use this kit" :
                                    ChatColor.GOLD + "You must wait a while to use this kit"
                    ))
                    .build());
        } else {
            inv.setItem(2, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                    .name(ChatColor.DARK_GREEN + "Mage")
                    .lore(Arrays.asList(
                            ChatColor.YELLOW + "Price: " + ChatColor.GOLD + "85C ",
                            ChatColor.YELLOW + "Status: " + ChatColor.RED + "Unowned",
                            ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + "1 minute",
                            ChatColor.RED + "Click to purchase this kit"
                    ))
                    .build());
        }


        if (hasKit.get(p).contains("archer")) {
            inv.setItem(3, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                    .name(ChatColor.DARK_GREEN + "Archer")
                    .lore(Arrays.asList(
                            ChatColor.YELLOW + "Price: " + ChatColor.GOLD + "120C ",
                            ChatColor.YELLOW + "Status: " + ChatColor.GREEN + "Owned",
                            ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + "1 minute",
                            canUse(p, "archer") ? ChatColor.GREEN + "Click to use this kit" :
                                    ChatColor.GOLD + "You must wait a while to use this kit"
                    ))
                    .build());
        } else {
            inv.setItem(3, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                    .name(ChatColor.DARK_GREEN + "Archer")
                    .lore(Arrays.asList(
                            ChatColor.YELLOW + "Price: " + ChatColor.GOLD + "120C ",
                            ChatColor.YELLOW + "Status: " + ChatColor.RED + "Unowned",
                            ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + "1 minute",
                            ChatColor.RED + "Click to purchase this kit"
                    ))
                    .build());
        }


        if (hasKit.get(p).contains("healer")) {
            inv.setItem(4, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                    .name(ChatColor.DARK_GREEN + "Healer")
                    .lore(Arrays.asList(
                            ChatColor.YELLOW + "Price: " + ChatColor.GOLD + "200C ",
                            ChatColor.YELLOW + "Status: " + ChatColor.GREEN + "Owned",
                            ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + "1 minute",
                            canUse(p, "healer") ? ChatColor.GREEN + "Click to use this kit" :
                                    ChatColor.GOLD + "You must wait a while to use this kit"
                    ))
                    .build());
        } else {
            inv.setItem(4, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                    .name(ChatColor.DARK_GREEN + "Healer")
                    .lore(Arrays.asList(
                            ChatColor.YELLOW + "Price: " + ChatColor.GOLD + "200C ",
                            ChatColor.YELLOW + "Status: " + ChatColor.RED + "Unowned",
                            ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + "1 minute",
                            ChatColor.RED + "Click to purchase this kit"
                    ))
                    .build());
        }


        if (hasKit.get(p).contains("armorer")) {
            inv.setItem(5, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                    .name(ChatColor.DARK_GREEN + "Armorer")
                    .lore(Arrays.asList(
                            ChatColor.YELLOW + "Price: " + ChatColor.GOLD + "250C ",
                            ChatColor.YELLOW + "Status: " + ChatColor.GREEN + "Owned",
                            ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + "1 hour",
                            canUse(p, "armorer") ? ChatColor.GREEN + "Click to use this kit" :
                                    ChatColor.GOLD + "You must wait a while to use this kit"
                    ))
                    .build());
        } else {
            inv.setItem(5, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                    .name(ChatColor.DARK_GREEN + "Armorer")
                    .lore(Arrays.asList(
                            ChatColor.YELLOW + "Price: " + ChatColor.GOLD + "250C ",
                            ChatColor.YELLOW + "Status: " + ChatColor.RED + "Unowned",
                            ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + "1 hour",
                            ChatColor.RED + "Click to purchase this kit"
                    ))
                    .build());
        }


        if (hasKit.get(p).contains("warlord")) {
            inv.setItem(6, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                    .name(ChatColor.DARK_GREEN + "Warlord")
                    .lore(Arrays.asList(
                            ChatColor.YELLOW + "Price: " + ChatColor.GOLD + "400C ",
                            ChatColor.YELLOW + "Status: " + ChatColor.GREEN + "Owned",
                            ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + "1.5 hours",
                            canUse(p, "warlord") ? ChatColor.GREEN + "Click to use this kit" :
                                    ChatColor.GOLD + "You must wait a while to use this kit"
                    ))
                    .build());
        } else {
            inv.setItem(6, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                    .name(ChatColor.DARK_GREEN + "Warlord")
                    .lore(Arrays.asList(
                            ChatColor.YELLOW + "Price: " + ChatColor.GOLD + "400C ",
                            ChatColor.YELLOW + "Status: " + ChatColor.RED + "Unowned",
                            ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + "1.5 hours",
                            ChatColor.RED + "Click to purchase this kit"
                    ))
                    .build());
        }

        if (hasKit.get(p).contains("overlord")) {
            inv.setItem(7, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                    .name(ChatColor.DARK_GREEN + "Overlord")
                    .lore(Arrays.asList(
                            ChatColor.YELLOW + "Price: " + ChatColor.GOLD + "550C ",
                            ChatColor.YELLOW + "Status: " + ChatColor.GREEN + "Owned",
                            ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + "2 hours",
                            canUse(p, "overlord") ? ChatColor.GREEN + "Click to use this kit" :
                                    ChatColor.GOLD + "You must wait a while to use this kit"
                    ))
                    .build());
        } else {
            inv.setItem(7, new ItemStackBuilder(Material.ENCHANTED_BOOK)
                    .name(ChatColor.DARK_GREEN + "Overlord")
                    .lore(Arrays.asList(
                            ChatColor.YELLOW + "Price: " + ChatColor.GOLD + "550C ",
                            ChatColor.YELLOW + "Status: " + ChatColor.RED + "Unowned",
                            ChatColor.YELLOW + "Delay: " + ChatColor.GOLD + "2 hours",
                            ChatColor.RED + "Click to purchase this kit"
                    ))
                    .build());
        }

        inv.setItem(8, new ItemStackBuilder(Material.BARRIER)
                .name(ChatColor.RED + "Exit").build());
        p.openInventory(inv);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        hasKit.putAll(player, player.getDataConfig().contains("has-kits") ? player.getDataConfig().getStringList("has-kits") : new ArrayList<>());
        Map<String, Long> map = new HashMap<>();
        if (player.getDataConfig().contains("delays")) {
            for (String kit : player.getDataConfig().getConfigurationSection("delays").getKeys(false)) {
                map.put(kit, player.getDataConfig().getLong("delays." + kit));
            }
            delays.put(player, map);
        }
    }
}

