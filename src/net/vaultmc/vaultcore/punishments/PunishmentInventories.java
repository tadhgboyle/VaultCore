package net.vaultmc.vaultcore.punishments;

import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class PunishmentInventories {

    public static Player skullOwner;

    public static void openPunishMainInventory(Player target, Player executor) {
        Inventory inventory = Bukkit.createInventory(null, 18, "Punishments > Main");

        if (target == null) {
            return;
        }

        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (byte) 3);

        SkullMeta meta = (SkullMeta) item.getItemMeta();

        meta.setOwner(target.getName());

        skullOwner = Bukkit.getPlayer(meta.getOwner());

        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Click to see punishment history for " + ChatColor.YELLOW + skullOwner.getName()
        ));

        meta.setDisplayName(ChatColor.YELLOW + "Punishments for " + ChatColor.GOLD + skullOwner.getName());

        item.setItemMeta(meta);

        inventory.setItem(4, item);

        inventory.setItem(17, new ItemStackBuilder(Material.BARRIER)
                .name(ChatColor.RED + "Exit")
                .lore(Arrays.asList(ChatColor.GRAY + "Click to exit"

                )).build());

        inventory.setItem(10, new ItemStackBuilder(Material.STICK)
                .name(ChatColor.GREEN + "Warns")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Warn the specified player."
                )).build());

        inventory.setItem(14, new ItemStackBuilder(Material.IRON_AXE)
                .name(ChatColor.RED + "Bans")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Ban the specified player."
                )).build());

        inventory.setItem(12, new ItemStackBuilder(Material.BOOK)
                .name(ChatColor.YELLOW + "Mutes")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Mute the specified player."
                )).build());

        executor.openInventory(inventory);

    }

    public static void openPunishWarnInventory(Player target, Player executor) {

        Inventory inventory = Bukkit.createInventory(null, 9, "Punishments > Warns > Reasons");

        if (target == null) {
            return;
        }

        inventory.setItem(0, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Hacking Offenses")
                .build());

        inventory.setItem(1, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Staff/Player Disrespect")
                .build());


        inventory.setItem(2, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Unwanted in-game activity")
                .build());

        inventory.setItem(3, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Spamming")
                .build());


        inventory.setItem(4, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Chat Abuse")
                .build());

        inventory.setItem(5, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Bug Abuse")
                .build());

        inventory.setItem(6, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Custom")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Type your reason in chat!"
                ))
                .build());

        inventory.setItem(8, new ItemStackBuilder(Material.BARRIER)
                .name(ChatColor.RED + "Back")
                .build());

        executor.openInventory(inventory);

    }

    public static void openPunishMuteInventory(Player target, Player executor) {

        Inventory inventory = Bukkit.createInventory(null, 9, "Punishments > Mutes > Reasons");

        if (target == null) {
            return;
        }

        inventory.setItem(0, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Hacking Offenses")
                .build());

        inventory.setItem(1, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Staff/Player Disrespect")
                .build());


        inventory.setItem(2, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Unwanted in-game activity")
                .build());

        inventory.setItem(3, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Spamming")
                .build());


        inventory.setItem(4, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Chat Abuse")
                .build());

        inventory.setItem(5, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Bug Abuse")
                .build());

        inventory.setItem(6, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Custom")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Type your reason in chat!"
                ))
                .build());

        inventory.setItem(8, new ItemStackBuilder(Material.BARRIER)
                .name(ChatColor.RED + "Back")
                .build());

        executor.openInventory(inventory);

    }

    public static void openPunishBanInventory(Player target, Player executor) {

        Inventory inventory = Bukkit.createInventory(null, 9, "Punishments > Bans > Reasons");

        if (target == null) {
            return;
        }

        inventory.setItem(0, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Hacking Offenses")
                .build());

        inventory.setItem(1, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Staff/Player Disrespect")
                .build());


        inventory.setItem(2, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Unwanted in-game activity")
                .build());

        inventory.setItem(3, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Spamming")
                .build());


        inventory.setItem(4, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Chat Abuse")
                .build());

        inventory.setItem(5, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Bug Abuse")
                .build());

        inventory.setItem(6, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Custom")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Type your reason in chat!"
                ))
                .build());

        inventory.setItem(8, new ItemStackBuilder(Material.BARRIER)
                .name(ChatColor.RED + "Back")
                .build());

        executor.openInventory(inventory);

    }

    public static void openPunishBanDurationInventory(Player target, Player executor) {

        Inventory inventory = Bukkit.createInventory(null, 9, "Punishments > Bans > Durations");

        if (target == null) {
            return;
        }

        inventory.setItem(0, new ItemStackBuilder(Material.FEATHER)
                .name(ChatColor.YELLOW + "1 hour")
                .build());

        inventory.setItem(1, new ItemStackBuilder(Material.FEATHER)
                .name(ChatColor.YELLOW + "2 hours")
                .build());


        inventory.setItem(2, new ItemStackBuilder(Material.FEATHER)
                .name(ChatColor.YELLOW + "12 hours")
                .build());

        inventory.setItem(3, new ItemStackBuilder(Material.FEATHER)
                .name(ChatColor.YELLOW + "48 hours")
                .build());


        inventory.setItem(4, new ItemStackBuilder(Material.FEATHER)
                .name(ChatColor.YELLOW + "2 months")
                .build());

        inventory.setItem(5, new ItemStackBuilder(Material.FEATHER)
                .name(ChatColor.YELLOW + "6 months")
                .build());

        inventory.setItem(6, new ItemStackBuilder(Material.FEATHER)
                .name(ChatColor.YELLOW + "Permanent")
                .build());

        inventory.setItem(8, new ItemStackBuilder(Material.BARRIER)
                .name(ChatColor.RED + "Back")
                .build());

        executor.openInventory(inventory);

    }

    public static void openPunishMutesDurationInventory(Player target, Player executor) {

        Inventory inventory = Bukkit.createInventory(null, 9, "Punishments > Mutes > Durations");

        if (target == null) {
            return;
        }

        inventory.setItem(0, new ItemStackBuilder(Material.FEATHER)
                .name(ChatColor.YELLOW + "1 hour")
                .build());

        inventory.setItem(1, new ItemStackBuilder(Material.FEATHER)
                .name(ChatColor.YELLOW + "2 hours")
                .build());


        inventory.setItem(2, new ItemStackBuilder(Material.FEATHER)
                .name(ChatColor.YELLOW + "12 hours")
                .build());

        inventory.setItem(3, new ItemStackBuilder(Material.FEATHER)
                .name(ChatColor.YELLOW + "48 hours")
                .build());


        inventory.setItem(4, new ItemStackBuilder(Material.FEATHER)
                .name(ChatColor.YELLOW + "2 months")
                .build());

        inventory.setItem(5, new ItemStackBuilder(Material.FEATHER)
                .name(ChatColor.YELLOW + "6 months")
                .build());

        inventory.setItem(6, new ItemStackBuilder(Material.FEATHER)
                .name(ChatColor.YELLOW + "Permanent")
                .build());

        inventory.setItem(8, new ItemStackBuilder(Material.BARRIER)
                .name(ChatColor.RED + "Back")
                .build());

        executor.openInventory(inventory);

    }

}
