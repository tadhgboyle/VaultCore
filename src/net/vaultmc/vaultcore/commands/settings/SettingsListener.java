package net.vaultmc.vaultcore.commands.settings;

import net.vaultmc.vaultcore.VaultCore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SettingsListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        ItemStack clicked = event.getCurrentItem();

        if (clicked != null) {
            if (event.getView().getTitle().equals("Settings")) {
                if (clicked.getType() == Material.ENDER_PEARL) {
                    player.openInventory(SettingsInventories.teleportationSettings());
                    event.setCancelled(true);
                }
                if (clicked.getType() == Material.GRASS_BLOCK) {
                    player.openInventory(SettingsInventories.creativeSettings());
                    event.setCancelled(true);
                }
                if (clicked.getType() == Material.PAPER) {
                    player.openInventory(SettingsInventories.chatSettings());
                }
                event.setCancelled(true);
            }
            if (event.getView().getTitle().equals("Teleportation Settings")) {

                if (clicked.getType() == Material.BOW) {
                    boolean allowTPA = VaultCore.getInstance().getPlayerData()
                            .getBoolean("players." + player.getUniqueId() + ".settings.tpa");
                    boolean allowed = !allowTPA;
                    VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.tpa",
                            allowed);
                    VaultCore.getInstance().savePlayerData();
                    event.setCancelled(true);
                    SettingsInventories.init(player);
                    player.openInventory(SettingsInventories.teleportationSettings());
                }
                if (clicked.getType() == Material.ARROW) {
                    boolean autoTPA = VaultCore.getInstance().getPlayerData()
                            .getBoolean("players." + player.getUniqueId() + ".settings.autotpa");
                    boolean allowed = !autoTPA;
                    VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.autotpa",
                            allowed);
                    VaultCore.getInstance().savePlayerData();
                    event.setCancelled(true);
                    SettingsInventories.init(player);
                    player.openInventory(SettingsInventories.teleportationSettings());
                } else {
                    event.setCancelled(true);
                }
            }
            if (event.getView().getTitle().equals("Creative Settings")) {

                if (clicked.getType() == Material.REPEATER) {
                    boolean allowCycle = VaultCore.getInstance().getPlayerData()
                            .getBoolean("players." + player.getUniqueId() + ".settings.cycle");
                    boolean allowed = !allowCycle;
                    VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.cycle",
                            allowed);
                    VaultCore.getInstance().savePlayerData();
                    event.setCancelled(true);
                    SettingsInventories.init(player);
                    player.openInventory(SettingsInventories.creativeSettings());
                }
            }
            if (event.getView().getTitle().equals("Chat Settings")) {

                if (clicked.getType() == Material.FEATHER) {
                    boolean allowMsg = VaultCore.getInstance().getPlayerData()
                            .getBoolean("players." + player.getUniqueId() + ".settings.msg");
                    boolean allowed;
                    allowed = allowMsg != true;
                    VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.msg",
                            allowed);
                    VaultCore.getInstance().savePlayerData();
                    event.setCancelled(true);
                    SettingsInventories.init(player);
                    player.openInventory(SettingsInventories.chatSettings());
                }
                if (clicked.getType() == Material.FILLED_MAP) {
                    boolean allowPWC = VaultCore.getInstance().getPlayerData()
                            .getBoolean("players." + player.getUniqueId() + ".settings.pwc");
                    boolean allowed = !allowPWC;
                    VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.pwc",
                            allowed);
                    VaultCore.getInstance().savePlayerData();
                    event.setCancelled(true);
                    SettingsInventories.init(player);
                    player.openInventory(SettingsInventories.chatSettings());
                }
                if (clicked.getType() == Material.IRON_BARS) {
                    boolean allowSwearFilter = VaultCore.getInstance().getPlayerData()
                            .getBoolean("players." + player.getUniqueId() + ".settings.swearfilter");
                    boolean allowed = !allowSwearFilter;
                    VaultCore.getInstance().getPlayerData()
                            .set("players." + player.getUniqueId() + ".settings.swearfilter", allowed);
                    VaultCore.getInstance().savePlayerData();
                    event.setCancelled(true);
                    SettingsInventories.init(player);
                    player.openInventory(SettingsInventories.chatSettings());
                } else {
                    event.setCancelled(true);
                }
            }
            if (event.getView().getTitle().equals("Teleportation Settings")
                    || event.getView().getTitle().equals("Creative Settings") || event.getView().getTitle().equals("Chat Settings")) {
                if (clicked.getType() == Material.BOOK) {
                    player.openInventory(SettingsInventories.settingsMain());
                }
                event.setCancelled(true);
            }
        }
    }
}
