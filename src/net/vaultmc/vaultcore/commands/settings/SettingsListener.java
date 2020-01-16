package net.vaultmc.vaultcore.commands.settings;

import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SettingsListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        VLPlayer player = VLPlayer.getPlayer((Player) e.getWhoClicked());

        ItemStack clicked = e.getCurrentItem();

        if (clicked != null) {
            if (e.getView().getTitle().equals("Settings")) {
                if (clicked.getType() == Material.ENDER_PEARL) {
                    player.openInventory(SettingsInventories.teleportationSettings());
                    e.setCancelled(true);
                }
                if (clicked.getType() == Material.GRASS_BLOCK) {
                    player.openInventory(SettingsInventories.creativeSettings());
                    e.setCancelled(true);
                }
                if (clicked.getType() == Material.PAPER) {
                    player.openInventory(SettingsInventories.chatSettings());
                }
                e.setCancelled(true);
            }
            if (e.getView().getTitle().equals("Teleportation Settings")) {

                if (clicked.getType() == Material.BOW) {
                    player.getDataConfig().set("settings.tpa", !player.getDataConfig().getBoolean("settings.tpa"));
                    player.saveData();
                    e.setCancelled(true);
                    SettingsInventories.init(player);
                    player.openInventory(SettingsInventories.teleportationSettings());
                }
                if (clicked.getType() == Material.ARROW) {
                    player.getDataConfig().set("settings.autotpa", !player.getDataConfig().getBoolean("settings.autotpa"));
                    player.saveData();
                    e.setCancelled(true);
                    SettingsInventories.init(player);
                    player.openInventory(SettingsInventories.teleportationSettings());
                } else {
                    e.setCancelled(true);
                }
            }
            if (e.getView().getTitle().equals("Creative Settings")) {

                if (clicked.getType() == Material.REPEATER) {
                    player.getDataConfig().set("settings.cycle", !player.getDataConfig().getBoolean("settings.cycle"));
                    player.saveData();
                    e.setCancelled(true);
                    SettingsInventories.init(player);
                    player.openInventory(SettingsInventories.creativeSettings());
                }
            }
            if (e.getView().getTitle().equals("Chat Settings")) {

                if (clicked.getType() == Material.FEATHER) {
                    player.getDataConfig().set("settings.msg", !player.getDataConfig().getBoolean("settings.msg"));
                    player.saveData();
                    e.setCancelled(true);
                    SettingsInventories.init(player);
                    player.openInventory(SettingsInventories.chatSettings());
                }
                if (clicked.getType() == Material.FILLED_MAP) {
                    player.getDataConfig().set("settings.pwc", !player.getDataConfig().getBoolean("settings.pwc"));
                    player.saveData();
                    e.setCancelled(true);
                    SettingsInventories.init(player);
                    player.openInventory(SettingsInventories.chatSettings());
                }
                if (clicked.getType() == Material.IRON_BARS) {
                    player.getDataConfig().set("settings.swearfilter", !player.getDataConfig().getBoolean("settings.swearfilter"));
                    player.saveData();
                    e.setCancelled(true);
                    SettingsInventories.init(player);
                    player.openInventory(SettingsInventories.chatSettings());
                } else {
                    e.setCancelled(true);
                }
            }
            if (e.getView().getTitle().equals("Teleportation Settings")
                    || e.getView().getTitle().equals("Creative Settings") || e.getView().getTitle().equals("Chat Settings")) {
                if (clicked.getType() == Material.BOOK) {
                    player.openInventory(SettingsInventories.settingsMain());
                }
                e.setCancelled(true);
            }
        }
    }
}
