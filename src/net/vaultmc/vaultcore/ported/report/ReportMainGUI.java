/*
 * VaultUtils: VaultMC functionalities provider.
 * Copyright (C) 2020 yangyang200
 *
 * VaultUtils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VaultUtils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VaultCore.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.vaultmc.vaultcore.ported.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportMainGUI implements Listener {
    @Getter
    private Inventory inv;
    @Getter
    private VLPlayer reporter;
    @Getter
    private VLOfflinePlayer victim;
    @Getter
    private MainGUIOptions options;

    public ReportMainGUI(VLPlayer reporter, VLOfflinePlayer victim, MainGUIOptions options) {
        this.reporter = reporter;
        this.victim = victim;
        this.options = options;

        this.buildInventory();
        VaultCore.getInstance().registerEvents(this);
    }

    public static String listToString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String x : list) {
            if (first) {
                builder.append(x);
                first = false;
                continue;
            }
            builder.append(", ").append(x);
        }
        return builder.toString();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        if (e.getInventory() == inv) {
            e.setCancelled(true);
            VLPlayer player = VLPlayer.getPlayer((Player) e.getWhoClicked());

            if (e.getSlot() == 28) {
                options.setKillAura(!options.isKillAura());
            } else if (e.getSlot() == 29) {
                options.setSpeed(!options.isSpeed());
            } else if (e.getSlot() == 30) {
                options.setDolphin(!options.isDolphin());
            } else if (e.getSlot() == 31) {
                options.setFly(!options.isFly());
            } else if (e.getSlot() == 32) {
                options.setOtherCheats(!options.isOtherCheats());
            } else if (e.getSlot() == 33) {
                options.setBadSkin(!options.isBadSkin());
            } else if (e.getSlot() == 34) {
                options.setChat(!options.isChat());
            } else if (e.getSlot() == 52) {
                if (options.noReasons()) {
                    player.sendMessage(VaultLoader.getMessage("report.no-reasons"));
                    player.closeInventory();
                    return;
                }
                Report report = new Report(reporter, victim, options.toStringList());
                reporter.sendMessage(VaultLoader.getMessage("report.reported").replace("{VICTIM}", victim.getFormattedName()));

                for (VLPlayer i : VLPlayer.getOnlinePlayers()) {
                    if (i.hasPermission("vaultutils.staff.report.notify")) {
                        i.sendMessage(VaultLoader.getMessage("report.player-report")
                                .replace("{REPORTER}", reporter.getFormattedName())
                                .replace("{VICTIM}", victim.getFormattedName())
                                .replace("{REASONS}", listToString(report.getReasons()))
                        );
                    }
                }
                player.closeInventory();
                return;
            } else if (e.getSlot() == 53) {
                player.closeInventory();
                return;
            }
            player.closeInventory();
            new ReportMainGUI(reporter, victim, options).open(player);
        }
    }

    private void buildInventory() {
        inv = Bukkit.createInventory(null, 54, "Report: " + victim.getFormattedName());

        inv.setItem(13, new ItemStackBuilder(Material.PLAYER_HEAD)
                .skullOwner(victim)
                .name(ChatColor.GOLD + victim.getFormattedName())
                .lore(Arrays.asList("",
                        ChatColor.GRAY + "You are reporting this player.")).build());
        inv.setItem(28, new ItemStackBuilder(Material.IRON_SWORD)
                .name((options.isKillAura() ? ChatColor.GREEN : ChatColor.GOLD) + "Kill Aura")
                .lore(Arrays.asList(
                        "",
                        options.isKillAura() ? ChatColor.GREEN + "Enabled" : ChatColor.GOLD + "Disabled",
                        ChatColor.GRAY + "Targeting all nearby players and ",
                        ChatColor.GRAY + "hitting them with an extreme speed."
                )).build());
        inv.setItem(29, new ItemStackBuilder(Material.RABBIT_FOOT)
                .name((options.isSpeed() ? ChatColor.GREEN : ChatColor.GOLD) + "Speed")
                .lore(Arrays.asList(
                        "",
                        options.isSpeed() ? ChatColor.GREEN + "Enabled" : ChatColor.GOLD + "Disabled",
                        ChatColor.GRAY + "Walking faster than normal. Please",
                        ChatColor.GRAY + "make sure the player doesn't have any",
                        ChatColor.GRAY + "potion effects."
                )).build());
        inv.setItem(30, new ItemStackBuilder(Material.DIAMOND_BOOTS)
                .name((options.isDolphin() ? ChatColor.GREEN : ChatColor.GOLD) + "Dolphin")
                .lore(Arrays.asList(
                        "",
                        options.isDolphin() ? ChatColor.GREEN + "Enabled" : ChatColor.GOLD + "Disabled",
                        ChatColor.GRAY + "aka. Jesus. Ability to walk on",
                        ChatColor.GRAY + "water without the body being in",
                        ChatColor.GRAY + "the water."
                )).build());
        inv.setItem(31, new ItemStackBuilder(Material.ARROW)
                .name((options.isFly() ? ChatColor.GREEN : ChatColor.GOLD) + "Fly")
                .lore(Arrays.asList(
                        "",
                        options.isFly() ? ChatColor.GREEN + "Enabled" : ChatColor.GOLD + "Disabled",
                        ChatColor.GRAY + "Being able to float in the air ",
                        ChatColor.GRAY + "without falling or taking damage.",
                        "",
                        ChatColor.GRAY + "This could be caused of lag!"
                )).build());
        inv.setItem(32, new ItemStackBuilder(Material.TURTLE_EGG)
                .name((options.isOtherCheats() ? ChatColor.GREEN : ChatColor.GOLD) + "Other cheats")
                .lore(Arrays.asList(
                        "",
                        options.isOtherCheats() ? ChatColor.GREEN + "Enabled" : ChatColor.GOLD + "Disabled",
                        ChatColor.GRAY + "Other cheats that isn't mentioned",
                        ChatColor.GRAY + "here."
                )).build());
        inv.setItem(33, new ItemStackBuilder(Material.REDSTONE_BLOCK)
                .name((options.isBadSkin() ? ChatColor.GREEN : ChatColor.GOLD) + "Inappropriate skin")
                .lore(Arrays.asList(
                        "",
                        options.isBadSkin() ? ChatColor.GREEN + "Enabled" : ChatColor.GOLD + "Disabled",
                        ChatColor.GRAY + "Having sexual or inappropriate contents",
                        ChatColor.GRAY + "on their skins."
                )).build());
        inv.setItem(34, new ItemStackBuilder(Material.PLAYER_HEAD)
                .name((options.isChat() ? ChatColor.GREEN : ChatColor.GOLD) + "Chat")
                .lore(Arrays.asList(
                        "",
                        options.isChat() ? ChatColor.GREEN + "Enabled" : ChatColor.GOLD + "Disabled",
                        ChatColor.GRAY + "Bullying, trolling or swearing in both",
                        ChatColor.GRAY + "public or private chat."
                ))
                .build());
        inv.setItem(52, new ItemStackBuilder(Material.ORANGE_WOOL)
                .name(ChatColor.GOLD + "Submit")
                .build());
        inv.setItem(53, new ItemStackBuilder(Material.BARRIER)
                .name(ChatColor.RED + "Cancel")
                .build());

        for (int i = 0; i <= 53; i++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build());
        }
    }

    public void open(VLPlayer player) {
        player.closeInventory();
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory() == this.inv) {
            HandlerList.unregisterAll(this);
        }
    }

    @ToString
    @NoArgsConstructor
    public static class MainGUIOptions {
        /*
         * Iron sword:     Kill aura
         * D boots:        Dolphin
         * Rabbit:         Speed
         * Arrow:          Fly
         * Turtle Egg:     Other cheats
         * Redstone Block: Bad skin
         * Player Skull:   Chat (Public or private)
         */

        @Getter
        @Setter
        private boolean killAura;
        @Getter
        @Setter
        private boolean speed;
        @Getter
        @Setter
        private boolean dolphin;
        @Getter
        @Setter
        private boolean fly;
        @Getter
        @Setter
        private boolean otherCheats;
        @Getter
        @Setter
        private boolean badSkin;
        @Getter
        @Setter
        private boolean chat;

        public boolean noReasons() {
            return !killAura && !speed && !dolphin && !fly && !otherCheats && !badSkin && !chat;
        }

        public List<String> toStringList() {
            List<String> list = new ArrayList<>();
            if (killAura) list.add("Kill Aura");
            if (speed) list.add("Speed");
            if (dolphin) list.add("Dolphin (Jesus)");
            if (fly) list.add("Fly");
            if (otherCheats) list.add("Other cheats");
            if (badSkin) list.add("Inappropriate skin");
            if (chat) list.add("Chat");
            return list;
        }
    }
}
