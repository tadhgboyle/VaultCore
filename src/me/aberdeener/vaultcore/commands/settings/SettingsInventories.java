package me.aberdeener.vaultcore.commands.settings;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.aberdeener.vaultcore.VaultCore;

public class SettingsInventories {

	private static ItemStack teleportMain, chatMain, back;
	private static ItemStack toggleTPA, acceptTPA;
	private static ItemStack toggleMsg, togglePWC, toggleSwear;

	static String string = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("string"));
	static String variable1 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-1"));
	static String variable2 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-2"));

	public static Inventory SettingsMain(Player player) {
		Inventory SettingsMain = Bukkit.createInventory(null, 27, "Settings");

		SettingsMain.setItem(11, teleportMain);
		SettingsMain.setItem(15, chatMain);

		return SettingsMain;
	}

	public static Inventory TeleportationSettings(Player player) {
		Inventory TeleportationSettings = Bukkit.createInventory(null, 36, "Teleportation Settings");

		TeleportationSettings.setItem(11, toggleTPA);
		TeleportationSettings.setItem(15, acceptTPA);
		TeleportationSettings.setItem(31, back);

		return TeleportationSettings;
	}

	public static Inventory ChatSettings(Player player) {
		Inventory ChatSettings = Bukkit.createInventory(null, 36, "Chat Settings");

		ChatSettings.setItem(10, toggleMsg);
		ChatSettings.setItem(13, togglePWC);
		ChatSettings.setItem(16, toggleSwear);
		ChatSettings.setItem(31, back);

		return ChatSettings;
	}
	
	public static void init(Player player) {

		teleportMain = new ItemStack(Material.ENDER_PEARL, 1);
		ItemMeta teleportMainMeta = teleportMain.getItemMeta();
		teleportMainMeta.setDisplayName(variable1 + "Teleportation " + string + "settings...");
		teleportMain.setItemMeta(teleportMainMeta);

		chatMain = new ItemStack(Material.PAPER, 1);
		ItemMeta chatMainMeta = chatMain.getItemMeta();
		chatMainMeta.setDisplayName(variable1 + "Chat " + string + "settings...");
		chatMain.setItemMeta(chatMainMeta);

		back = new ItemStack(Material.BOOK, 1);
		ItemMeta backMeta = back.getItemMeta();
		backMeta.setDisplayName(string + "Back...");
		back.setItemMeta(backMeta);

		toggleTPA = new ItemStack(Material.BOW, 1);
		ItemMeta toggleTPAMeta = toggleTPA.getItemMeta();
		toggleTPAMeta.setDisplayName(string + "Allow " + variable1 + "TPA" + string + "s");
		ArrayList<String> toggleTPALore = new ArrayList<String>();
		toggleTPALore.add(string + "Enabled: " + variable2
				+ VaultCore.getInstance().getPlayerData().get("players." + player.getUniqueId() + ".settings.tpa"));
		if (VaultCore.getInstance().getPlayerData()
				.getBoolean("players." + player.getUniqueId() + ".settings.tpa") == true) {
			toggleTPAMeta.addEnchant(Enchantment.DURABILITY, 5, true);
		}
		toggleTPAMeta.setLore(toggleTPALore);
		toggleTPA.setItemMeta(toggleTPAMeta);

		acceptTPA = new ItemStack(Material.ARROW, 1);
		ItemMeta acceptTPAMeta = acceptTPA.getItemMeta();
		acceptTPAMeta.setDisplayName(string + "Auto Accept " + variable1 + "TPA" + string + "s");
		ArrayList<String> acceptTPALore = new ArrayList<String>();
		acceptTPALore.add(string + "Enabled: " + variable2
				+ VaultCore.getInstance().getPlayerData().get("players." + player.getUniqueId() + ".settings.autotpa"));
		if (VaultCore.getInstance().getPlayerData()
				.getBoolean("players." + player.getUniqueId() + ".settings.autotpa") == true) {
			acceptTPAMeta.addEnchant(Enchantment.DURABILITY, 5, true);
		}
		acceptTPAMeta.setLore(acceptTPALore);
		acceptTPA.setItemMeta(acceptTPAMeta);

		toggleMsg = new ItemStack(Material.FEATHER, 1);
		ItemMeta toggleMsgMeta = toggleMsg.getItemMeta();
		toggleMsgMeta.setDisplayName(string + "Allow " + variable1 + "Messages");
		ArrayList<String> toggleMsgLore = new ArrayList<String>();
		toggleMsgLore.add(string + "Enabled: " + variable2
				+ VaultCore.getInstance().getPlayerData().get("players." + player.getUniqueId() + ".settings.msg"));
		if (VaultCore.getInstance().getPlayerData()
				.getBoolean("players." + player.getUniqueId() + ".settings.msg") == true) {
			toggleMsgMeta.addEnchant(Enchantment.DURABILITY, 5, true);
		}
		toggleMsgMeta.setLore(toggleMsgLore);
		toggleMsg.setItemMeta(toggleMsgMeta);
		
		togglePWC = new ItemStack(Material.FILLED_MAP, 1);
		ItemMeta togglePWCMeta = togglePWC.getItemMeta();
		togglePWCMeta.setDisplayName(string + "Use " + variable1 + "Per World Chat");
		ArrayList<String> togglePWCLore = new ArrayList<String>();
		togglePWCLore.add(string + "Enabled: " + variable2
				+ VaultCore.getInstance().getPlayerData().get("players." + player.getUniqueId() + ".settings.pwc"));
		if (VaultCore.getInstance().getPlayerData()
				.getBoolean("players." + player.getUniqueId() + ".settings.pwc") == true) {
			togglePWCMeta.addEnchant(Enchantment.DURABILITY, 5, true);
		}
		togglePWCMeta.setLore(togglePWCLore);
		togglePWC.setItemMeta(togglePWCMeta);

		toggleSwear = new ItemStack(Material.IRON_BARS, 1);
		ItemMeta toggleSwearMeta = toggleSwear.getItemMeta();
		toggleSwearMeta.setDisplayName(string + "Toggle " + variable1 + "Swear Filter");
		ArrayList<String> toggleSwearLore = new ArrayList<String>();
		toggleSwearLore.add(string + "Enabled: " + variable2 + VaultCore.getInstance().getPlayerData()
				.get("players." + player.getUniqueId() + ".settings.swearfilter"));
		if (VaultCore.getInstance().getPlayerData()
				.getBoolean("players." + player.getUniqueId() + ".settings.swearfilter") == true) {
			toggleSwearMeta.addEnchant(Enchantment.DURABILITY, 5, true);
		}
		toggleSwearMeta.setLore(toggleSwearLore);
		toggleSwear.setItemMeta(toggleSwearMeta);

	}
}
