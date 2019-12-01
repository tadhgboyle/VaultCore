package net.vaultmc.vaultcore.commands.settings;

import net.vaultmc.vaultcore.VaultCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SettingsListener implements Listener {

    static String string = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("string"));
    static String variable1 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-1"));
    static String variable2 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-2"));

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        ItemStack clicked = event.getCurrentItem();

        if (clicked == null) {
            return;
        } else {
            if (event.getView().getTitle().equals("Settings")) {
                if (clicked.getType() == Material.ENDER_PEARL) {
                    player.openInventory(SettingsInventories.TeleportationSettings(player));
                    event.setCancelled(true);
                }
                if (clicked.getType() == Material.GRASS_BLOCK) {
                    player.openInventory(SettingsInventories.CreativeSettings(player));
                    event.setCancelled(true);
                }
                if (clicked.getType() == Material.PAPER) {
                    player.openInventory(SettingsInventories.ChatSettings(player));
                    event.setCancelled(true);
                } else {
                    event.setCancelled(true);
                }
            }
            if (event.getView().getTitle().equals("Teleportation Settings")) {

                if (clicked.getType() == Material.BOW) {
                    boolean allowTPA = VaultCore.getInstance().getPlayerData()
                            .getBoolean("players." + player.getUniqueId() + ".settings.tpa");
                    boolean allowed;
                    allowed = allowTPA != true;
                    VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.tpa",
                            allowed);
                    VaultCore.getInstance().savePlayerData();
                    event.setCancelled(true);
                    SettingsInventories.init(player);
                    player.openInventory(SettingsInventories.TeleportationSettings(player));
                }
                if (clicked.getType() == Material.ARROW) {
                    boolean autoTPA = VaultCore.getInstance().getPlayerData()
                            .getBoolean("players." + player.getUniqueId() + ".settings.autotpa");
                    boolean allowed;
                    allowed = autoTPA != true;
                    VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.autotpa",
                            allowed);
                    VaultCore.getInstance().savePlayerData();
                    event.setCancelled(true);
                    SettingsInventories.init(player);
                    player.openInventory(SettingsInventories.TeleportationSettings(player));
                } else {
                    event.setCancelled(true);
                }
            }
            if (event.getView().getTitle().equals("Creative Settings")) {

                if (clicked.getType() == Material.REPEATER) {
                    boolean allowCycle = VaultCore.getInstance().getPlayerData()
                            .getBoolean("players." + player.getUniqueId() + ".settings.cycle");
                    boolean allowed;
                    allowed = allowCycle != true;
                    VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.cycle",
                            allowed);
                    VaultCore.getInstance().savePlayerData();
                    event.setCancelled(true);
                    SettingsInventories.init(player);
                    player.openInventory(SettingsInventories.CreativeSettings(player));
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
                    player.openInventory(SettingsInventories.ChatSettings(player));
                }
                if (clicked.getType() == Material.FILLED_MAP) {
                    boolean allowPWC = VaultCore.getInstance().getPlayerData()
                            .getBoolean("players." + player.getUniqueId() + ".settings.pwc");
                    boolean allowed;
                    allowed = allowPWC != true;
                    VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.pwc",
                            allowed);
                    VaultCore.getInstance().savePlayerData();
                    event.setCancelled(true);
                    SettingsInventories.init(player);
                    player.openInventory(SettingsInventories.ChatSettings(player));
                }
                if (clicked.getType() == Material.IRON_BARS) {
                    boolean allowSwearFilter = VaultCore.getInstance().getPlayerData()
                            .getBoolean("players." + player.getUniqueId() + ".settings.swearfilter");
                    boolean allowed;
                    allowed = allowSwearFilter != true;
                    VaultCore.getInstance().getPlayerData()
                            .set("players." + player.getUniqueId() + ".settings.swearfilter", allowed);
                    VaultCore.getInstance().savePlayerData();
                    event.setCancelled(true);
                    SettingsInventories.init(player);
                    player.openInventory(SettingsInventories.ChatSettings(player));
                } else {
                    event.setCancelled(true);
                }
            }
            if (event.getView().getTitle().equals("Teleportation Settings")
                    || event.getView().getTitle().equals("Creative Settings") || event.getView().getTitle().equals("Chat Settings")) {
                if (clicked.getType() == Material.BOOK) {
                    player.openInventory(SettingsInventories.SettingsMain(player));
                    event.setCancelled(true);
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }
}