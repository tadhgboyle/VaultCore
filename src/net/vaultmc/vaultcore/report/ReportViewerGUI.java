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

package net.vaultmc.vaultcore.report;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.PagedInventory;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReportViewerGUI implements Listener {
    private PagedInventory inv;

    public ReportViewerGUI() {
        VaultCore.getInstance().registerEvents(this);

        int page;
        if (Report.reports.size() % 36 == 0) {
            page = Report.reports.size() / 36;
        } else {
            page = Report.reports.size() / 36 + 1;
        }

        this.inv = new PagedInventory(page, "Reports (%s/%s)", 54);

        for (int i = 0; i < page; i++) {
            Inventory inventory = this.inv.getInventory(i);
            if (i != 0)
                inventory.setItem(48, new ItemStackBuilder(Material.ARROW)
                        .name(ChatColor.GOLD + "Previous Page")
                        .lore(Collections.singletonList(ChatColor.GRAY + "" + (i - 1)))
                        .build());

            if (i != page - 1)
                inventory.setItem(50, new ItemStackBuilder(Material.ARROW)
                        .name(ChatColor.GOLD + "Next Page")
                        .lore(Collections.singletonList(ChatColor.GRAY + "" + (i + 1)))
                        .build());

            generatePageItems(i);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() instanceof PlayerInventory) return;
        if (inv.getInventories().contains(e.getInventory()) && e.getWhoClicked() instanceof VLPlayer) {
            VLPlayer player = (VLPlayer) e.getWhoClicked();
            e.setCancelled(true);

            if (e.getCurrentItem() == null) return;

            if (e.getCurrentItem().getType() != Material.ARROW) {
                Report report;

                // STUPID METHOD INCOMING

                List<String> lore = e.getCurrentItem().getItemMeta().getLore();
                for (int i = 0; i < lore.size(); i++)
                    lore.set(i, ChatColor.stripColor(lore.get(i)));
                VLOfflinePlayer reporter = VLOfflinePlayer.getOfflinePlayer(lore.get(1).replace("Reporter: ", ""));
                VLOfflinePlayer victim = VLOfflinePlayer.getOfflinePlayer(lore.get(2).replace("Victim: ", ""));
                report = Report.getReport(reporter, victim);

                if (report == null) {
                    player.sendMessage(ChatColor.RED + "This report is already removed by another user!");
                    return;
                }

                // STUPID METHOD ENDING

                // Order is important!
                if (e.isShiftClick()) {
                    report.setAssignee(player);
                    player.sendMessage(ChatColor.GREEN + "You self-assigned the report by " + ChatColor.YELLOW + report.getReporter().getFormattedName() + ChatColor.GREEN + "!");
                    if (VLPlayer.getPlayer(report.getReporter().getUniqueId()) != null) {
                        VLPlayer.getPlayer(report.getReporter().getUniqueId()).sendMessage(ChatColor.YELLOW + player.getFormattedName() + ChatColor.GREEN + " " +
                                "assigned themselves to your report! They will be responsible to resolve your report.");
                    }
                } else if (e.isLeftClick()) {
                    report.setOpen(false);
                    player.sendMessage(ChatColor.GREEN + "You have closed " + ChatColor.YELLOW + report.getReporter().getFormattedName() + ChatColor.GREEN + "'s report!");
                    if (VLPlayer.getPlayer(report.getReporter().getUniqueId()) != null) {
                        VLPlayer.getPlayer(report.getReporter().getUniqueId()).sendMessage(ChatColor.YELLOW + player.getFormattedName() + ChatColor.GREEN + " " +
                                "closed your report!");
                    }
                } else if (e.isRightClick()) {
                    if (VLPlayer.getPlayer(report.getReporter().getUniqueId()) != null) {
                        VLPlayer.getPlayer(report.getReporter().getUniqueId()).sendMessage(ChatColor.YELLOW + player.getFormattedName() + ChatColor.GREEN + " " +
                                "removed your report!");
                    }
                    Report.reports.remove(report);
                    player.closeInventory();
                    if (Report.reports.size() >= 1) {
                        new ReportViewerGUI().open(player);
                    }
                }
            } else {
                // STUPID METHOD INCOMING
                int updated = Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getLore().get(0)));

                inv.open(player.getPlayer(), updated);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (this.inv.getInventories().contains(e.getInventory())) {
            HandlerList.unregisterAll(this);
        }
    }

    private void generatePageItems(int page) {
        List<Report> reports = Report.reports;

        int start = page * 36;
        int end = page * 36 + 35;

        Inventory inv_ = inv.getInventory(page);

        for (int i = start; i <= end; i++) {
            try {
                inv_.addItem(getItemStack(reports.get(i)));
            } catch (IndexOutOfBoundsException ex) {
                break;
            }
        }
    }

    private ItemStack getItemStack(Report report) {
        List<String> lore = new ArrayList<>(Arrays.asList(
                "",
                ChatColor.GREEN + "Reporter: " + ChatColor.YELLOW + report.getReporter().getFormattedName(),
                ChatColor.GREEN + "Victim: " + ChatColor.YELLOW + report.getVictim().getFormattedName(),
                ChatColor.GREEN + "Assignee: " + ChatColor.YELLOW + ((report.getAssignee() != null) ? report.getAssignee().getFormattedName() : "Not assigned yet"),
                ChatColor.GREEN + "Reason: "
        ));  // What? Arrays.asList is fixed-size or somewhat immutable?

        for (String reason : report.getReasons())
            lore.add(ChatColor.YELLOW + reason);

        if (report.isOpen()) {
            lore.add("");
            lore.add(ChatColor.AQUA + "Left click to close!");
            lore.add(ChatColor.AQUA + "Shift click to self-assign!");
            lore.add(ChatColor.AQUA + "Right click to remove!");

            return new ItemStackBuilder(Material.WRITTEN_BOOK)
                    .name(ChatColor.GOLD + "Open Report")
                    .lore(lore).build();
        } else {
            lore.add("");
            lore.add(ChatColor.AQUA + "Left click to reopen!");
            lore.add(ChatColor.AQUA + "Shift click to self-assign!");
            lore.add(ChatColor.AQUA + "Right click to remove!");

            return new ItemStackBuilder(Material.BOOK)
                    .name(ChatColor.GREEN + "Closed Report")
                    .lore(lore).build();
        }
    }

    public void open(VLPlayer player) {
        if (Report.reports.size() == 0) {
            player.sendMessage(ChatColor.RED + "There are no reports!");
            return;
        }
        inv.open(player.getPlayer(), 0);
    }
}
