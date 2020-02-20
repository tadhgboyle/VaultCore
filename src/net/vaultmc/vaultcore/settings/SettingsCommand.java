package net.vaultmc.vaultcore.settings;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.configuration.SQLPlayerData;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Collections;

@RootCommand(
        literal = "settings",
        description = "Manage your player settings."
)
@Permission(Permissions.SettingsCommand)
@PlayerOnly
public class SettingsCommand extends CommandExecutor implements Listener {
    public SettingsCommand() {
        register("settings", Collections.emptyList());
        VaultCore.getInstance().registerEvents(this);
    }

    @SubCommand("settings")
    public void settings(VLPlayer sender) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.RESET + "Settings");
        inv.setItem(11, new ItemStackBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + "Messaging")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Allow other players to send you",
                        ChatColor.GRAY + "private messages.",
                        "",
                        sender.getPlayerData().getBoolean("settings.msg") ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"
                ))
                .build());
        inv.setItem(13, new ItemStackBuilder(Material.BLAZE_ROD)
                .name(ChatColor.YELLOW + "Inventory Cycling")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Cycle through rows in your inventory when",
                        ChatColor.GRAY + "you cycle through your hot bar from first slot",
                        ChatColor.GRAY + "to last slot (or reversed).",
                        "",
                        sender.getPlayerData().getBoolean("settings.cycle") ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"
                ))
                .build());
        inv.setItem(15, new ItemStackBuilder(Material.COMPASS)
                .name(ChatColor.YELLOW + "TPA")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Allow other players to request to ",
                        ChatColor.GRAY + "teleport to you (or teleport to them).",
                        "",
                        sender.getPlayerData().getBoolean("settings.tpa") ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"
                ))
                .build());
        sender.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(ChatColor.RESET + "Settings")) {
            VLPlayer player = VLPlayer.getPlayer((Player) e.getWhoClicked());
            SQLPlayerData data = player.getPlayerData();
            if (e.getSlot() == 11) {
                data.set("settings.msg", !data.getBoolean("settings.msg"));
            } else if (e.getSlot() == 13) {
                data.set("settings.cycle", !data.getBoolean("settings.cycle"));
            } else if (e.getSlot() == 15) {
                data.set("settings.tpa", !data.getBoolean("settings.tpa"));
            }
            e.setCancelled(true);
            player.closeInventory();
            settings(player);
        }
    }
}
