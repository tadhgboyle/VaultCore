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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RootCommand(
        literal = "reports",
        description = "View existing reports"
)
@Permission(Permissions.ReportsCommand)
@PlayerOnly
public class ReportsCommand extends CommandExecutor implements Listener {
    private static final ItemStack next = new ItemStackBuilder(Material.ARROW)
            .name(ChatColor.GOLD + "Next")
            .build();
    private static final ItemStack previous = new ItemStackBuilder(Material.ARROW)
            .name(ChatColor.GOLD + "Previous")
            .build();
    private static final ItemStack opened = new ItemStackBuilder(Material.LIME_WOOL)
            .name(ChatColor.GREEN + "Opened Only")
            .lore(Collections.singletonList(ChatColor.YELLOW + "Click to toggle"))
            .build();
    private static final ItemStack all = new ItemStackBuilder(Material.YELLOW_WOOL)
            .name(ChatColor.YELLOW + "All")
            .lore(Collections.singletonList(ChatColor.YELLOW + "Click to toggle"))
            .build();

    public ReportsCommand() {
        register("reports", Collections.emptyList());
        register("reportsB", Collections.singletonList(Arguments.createArgument("openedOnly", Arguments.boolArgument())));
        VaultCore.getInstance().registerEvents(this);
    }

    private static ItemStack getItemStack(Report report) {
        return new ItemStackBuilder(report.getStatus() == Report.Status.OPEN ? Material.WRITTEN_BOOK : Material.BOOK)
                .name(ChatColor.YELLOW + report.getId())
                .lore(Arrays.asList(
                        VaultLoader.getMessage("report.inventory.reporter")
                                .replace("{REPORTER}", report.getReporter().getFormattedName()),
                        VaultLoader.getMessage("report.inventory.target")
                                .replace("{TARGET}", report.getTarget().getFormattedName()),
                        VaultLoader.getMessage("report.inventory.reasons")
                                .replace("{REASONS}", listToString(report.getReasons().stream().map(Report.Reason::toString).collect(Collectors.toList()))),
                        VaultLoader.getMessage("report.inventory.assignees")
                                .replace("{ASSIGNEES}", listToString(report.getAssignees().stream().map(VLOfflinePlayer::getFormattedName).collect(Collectors.toList()))),
                        VaultLoader.getMessage("report.inventory.status").replace("{STATUS}", VaultLoader.getMessage(report.getStatus().getKey())),
                        "",
                        VaultLoader.getMessage("report.inventory.left-click"),
                        VaultLoader.getMessage("report.inventory.shift-click"),
                        VaultLoader.getMessage("report.inventory.right-click")
                ))
                .build();
    }

    private static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : list) {
            if (first) {
                sb.append(s);
                first = false;
                continue;
            }
            sb.append(", ").append(s);
        }
        return sb.toString();
    }

    @SubCommand("reports")
    public void reports(VLPlayer sender) {
        reports(sender, true);
    }

    @SubCommand("reportsB")
    public void reports(VLPlayer sender, boolean openedOnly) {
        reports(sender, openedOnly, 0);
    }

    public void reports(VLPlayer sender, boolean openedOnly, int page) {
        int pages;

        if (openedOnly) {
            pages = Report.getReports().size() / 36;
            if (Report.getReports().size() % 36 != 0) pages++;
        } else {
            pages = Report.getActiveReports().size() / 36;
            if (Report.getActiveReports().size() % 36 != 0) pages++;
        }

        pages--;
        if (pages < 0) {
            sender.sendMessage(VaultLoader.getMessage("report.no-reports"));
        }

        if (page > pages) {
            throw new IllegalArgumentException("page (max = " + pages + ")");
        }

        List<Report> reports = openedOnly ? Report.getReports().subList(page * 36, Math.min(Report.getReports().size(), page * 36 + 35)) :
                Report.getActiveReports().subList(page * 36, Math.min(Report.getActiveReports().size(), page * 36 + 35));
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.RESET + "Reports (" + (page + 1) + "/" + (pages + 1) + ")");
        for (Report report : reports) {
            inv.addItem(getItemStack(report));
        }

        if (page != pages) {
            inv.setItem(52, next);
        }
        if (page != 0) {
            inv.setItem(46, previous);
        }
        if (openedOnly) {
            inv.setItem(49, opened);
        } else {
            inv.setItem(49, all);
        }
        sender.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() instanceof PlayerInventory) return;
        if (e.getView().getTitle().startsWith(ChatColor.RESET + "Reports")) {
            if (e.getCurrentItem() == null) return;
            int page = Integer.parseInt(e.getView().getTitle().split("\\(")[1].split("/")[0]);
            boolean openedOnly = e.getInventory().getItem(49).getType() == Material.LIME_WOOL;
            if (e.getSlot() == 49) openedOnly = !openedOnly;
            page--;

            VLPlayer player = VLPlayer.getPlayer((Player) e.getWhoClicked());
            if (e.getSlot() == 52 && e.getInventory().getItem(52) != null) {
                player.closeInventory();
                reports(player, openedOnly, page + 1);
            } else if (e.getSlot() == 46 && e.getInventory().getItem(46) != null) {
                player.closeInventory();
                reports(player, openedOnly, page - 1);
            } else if (e.getSlot() == 49) {
                player.closeInventory();
                reports(player, openedOnly, page);
            } else {
                Report report = Report.getReport(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                if (e.isLeftClick()) {
                    int index = Arrays.asList(Report.Status.values()).indexOf(report.getStatus());
                    if (index == Report.Status.values().length - 1) {
                        index = 0;
                    }
                    report.setStatus(Report.Status.values()[index]);
                    player.sendMessage(VaultLoader.getMessage("report.set-status")
                            .replace("{ID}", report.getId())
                            .replace("{STATUS}", VaultLoader.getMessage(report.getStatus().getKey())));
                    player.closeInventory();
                    reports(player, openedOnly, page);
                } else if (e.isRightClick()) {
                    report.getAssignees().add(player);
                    player.closeInventory();
                    player.sendMessage(VaultLoader.getMessage("report.assigned").replace("{ID}", report.getId()));
                    reports(player, openedOnly, page);
                } else if (e.isShiftClick()) {
                    report.getAssignees().remove(player);
                    player.closeInventory();
                    player.sendMessage(VaultLoader.getMessage("report.unassigned").replace("{ID}", report.getId()));
                    reports(player, openedOnly, page);
                }
            }
            e.setCancelled(true);
        }
    }
}
