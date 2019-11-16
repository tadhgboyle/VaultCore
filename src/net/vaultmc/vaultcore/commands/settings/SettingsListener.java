package net.vaultmc.vaultcore.commands.settings;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.vaultmc.vaultcore.VaultCore;

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
		}
		else {
			if (event.getView().getTitle().equals("Settings")) {
				if (clicked.getType() == Material.ENDER_PEARL) {
					player.openInventory(SettingsInventories.TeleportationSettings(player));
					event.setCancelled(true);
				}
				if (clicked.getType() == Material.PAPER) {
					player.openInventory(SettingsInventories.ChatSettings(player));
					event.setCancelled(true);
				}
				else {
					event.setCancelled(true);
				}
			}
			if (event.getView().getTitle().equals("Teleportation Settings")) {

				if (clicked.getType() == Material.BOW) {
					boolean allowTPA = VaultCore.getInstance().getPlayerData()
							.getBoolean("players." + player.getUniqueId() + ".settings.tpa");
					boolean allowed;
					if (allowTPA == true) {
						allowed = false;
					} else {
						allowed = true;
					}
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
					if (autoTPA == true) {
						allowed = false;
					} else {
						allowed = true;
					}
					VaultCore.getInstance().getPlayerData().set("players." + player.getUniqueId() + ".settings.autotpa",
							allowed);
					VaultCore.getInstance().savePlayerData();
					event.setCancelled(true);
					SettingsInventories.init(player);
					player.openInventory(SettingsInventories.TeleportationSettings(player));
				}
				else {
					event.setCancelled(true);
				}
			}
			if (event.getView().getTitle().equals("Chat Settings")) {

				if (clicked.getType() == Material.FEATHER) {
					boolean allowMsg = VaultCore.getInstance().getPlayerData()
							.getBoolean("players." + player.getUniqueId() + ".settings.msg");
					boolean allowed;
					if (allowMsg == true) {
						allowed = false;
					} else {
						allowed = true;
					}
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
					if (allowPWC == true) {
						allowed = false;
					} else {
						allowed = true;
					}
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
					if (allowSwearFilter == true) {
						allowed = false;
					} else {
						allowed = true;
					}
					VaultCore.getInstance().getPlayerData()
							.set("players." + player.getUniqueId() + ".settings.swearfilter", allowed);
					VaultCore.getInstance().savePlayerData();
					event.setCancelled(true);
					SettingsInventories.init(player);
					player.openInventory(SettingsInventories.ChatSettings(player));
				}
				else {
					event.setCancelled(true);
				}
			}
			if (event.getView().getTitle().equals("Chat Settings")
					|| event.getView().getTitle().equals("Teleportation Settings")) {
				if (clicked.getType() == Material.BOOK) {
					player.openInventory(SettingsInventories.SettingsMain(player));
					event.setCancelled(true);
				}
				else {
					event.setCancelled(true);
				}
			}
		}
	}
}
