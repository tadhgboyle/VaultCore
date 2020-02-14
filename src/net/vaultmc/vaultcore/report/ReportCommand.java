package net.vaultmc.vaultcore.report;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.util.*;

@RootCommand(
        literal = "report",
        description = "Report a rule breaker."
)
@Permission(Permissions.ReportCommand)
@PlayerOnly
public class ReportCommand extends CommandExecutor implements Listener {
    private static final Map<UUID, ReportData> data = new HashMap<>();

    public ReportCommand() {
        register("report", Collections.singletonList(Arguments.createArgument("player", Arguments.offlinePlayerArgument())));
        VaultCore.getInstance().registerEvents(this);
    }

    private static Inventory generateInventory(ReportData data) {
        Inventory inv = Bukkit.createInventory(null, 54, VaultLoader.getMessage("report.inventory.title"));
        inv.setItem(13, new ItemStackBuilder(Material.PLAYER_HEAD)
                .name(ChatColor.GRAY + "Reporting: " + data.getTarget().getFormattedName())
                .skullOwner(data.getTarget())
                .build());
        for (int i = 0; i < Report.Reason.values().length; i++) {
            Report.Reason reason = Report.Reason.values()[i];
            if (data.getReasons().contains(reason)) {
                inv.setItem(28 + i, new ItemStackBuilder(reason.getItem())
                        .name(ChatColor.RESET + VaultLoader.getMessage(reason.getKey()))
                        .enchant(Enchantment.DURABILITY, 5)
                        .hideFlags(ItemFlag.HIDE_ENCHANTS)
                        .build());
            } else {
                inv.setItem(28 + i, new ItemStackBuilder(reason.getItem())
                        .name(ChatColor.RESET + VaultLoader.getMessage(reason.getKey()))
                        .build());
            }
        }
        inv.setItem(49, new ItemStackBuilder(Material.WRITABLE_BOOK)
                .name(VaultLoader.getMessage("report.inventory.finish"))
                .build());
        return inv;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getView().getTitle().equals(VaultLoader.getMessage("report.inventory.title"))) {
            data.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(VaultLoader.getMessage("report.inventory.title"))) {
            ReportData data = ReportCommand.data.get(e.getWhoClicked().getUniqueId());
            if (e.getSlot() >= 28 && e.getSlot() <= 34) {
                Report.Reason reason = Report.Reason.values()[e.getSlot() - 28];
                if (data.getReasons().contains(reason)) {
                    data.getReasons().remove(reason);
                } else {
                    data.getReasons().add(reason);
                }

                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> {
                    e.getWhoClicked().closeInventory();
                    ReportCommand.data.put(e.getWhoClicked().getUniqueId(), data);
                    e.getWhoClicked().openInventory(generateInventory(data));
                });
            }

            if (e.getSlot() == 49) {
                Report report = new Report(VLOfflinePlayer.getOfflinePlayer((Player) e.getWhoClicked()),
                        data.getTarget(), new ArrayList<>(), data.getReasons(), Report.Status.OPEN);
                Report.getReports().add(report);
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().sendMessage(VaultLoader.getMessage("report.reported")
                        .replace("{PLAYER}", data.getTarget().getFormattedName())
                        .replace("{ID}", report.getId()));
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        data.remove(e.getPlayer().getUniqueId());
    }

    @SubCommand("report")
    public void report(VLPlayer reporter, VLOfflinePlayer target) {
        if (!data.containsKey(reporter.getUniqueId())) {
            data.put(reporter.getUniqueId(), new ReportData(target, new ArrayList<>()));
        }
        reporter.openInventory(generateInventory(data.get(reporter.getUniqueId())));
    }
}
