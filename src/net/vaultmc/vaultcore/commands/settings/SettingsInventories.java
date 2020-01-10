package net.vaultmc.vaultcore.commands.settings;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.vaultmc.vaultcore.*;

public class SettingsInventories {

	static String string = Utilities.string;
	static String variable1 = Utilities.variable1;
	static String variable2 = Utilities.variable2;

	private static ItemStack teleportMain, creativeMain, chatMain, back;
	private static ItemStack toggleTPA, acceptTPA;
	private static ItemStack toggleCycle;
	private static ItemStack toggleMsg, toggleSwear;

	public static Inventory SettingsMain(Player player) {
		Inventory SettingsMain = Bukkit.createInventory(null, 27, "Settings");

		SettingsMain.setItem(11, teleportMain);
		SettingsMain.setItem(13, creativeMain);
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

	public static Inventory CreativeSettings(Player player) {
		Inventory CreativeSettings = Bukkit.createInventory(null, 36, "Creative Settings");

		CreativeSettings.setItem(13, toggleCycle);
		CreativeSettings.setItem(31, back);

		return CreativeSettings;
	}

	public static Inventory ChatSettings(Player player) {
		Inventory ChatSettings = Bukkit.createInventory(null, 36, "Chat Settings");

		ChatSettings.setItem(11, toggleMsg);
		ChatSettings.setItem(15, toggleSwear);
		ChatSettings.setItem(31, back);

		return ChatSettings;
	}

	public static void init(Player player) {

		itemStackBuilder(teleportMain, Material.ENDER_PEARL, "" + variable1 + "Teleportation " + string + "settings...",
				player, "");

		itemStackBuilder(creativeMain, Material.GRASS_BLOCK, "" + variable1 + "Creative " + string + "settings...",
				player, "");

		itemStackBuilder(chatMain, Material.PAPER, "" + variable1 + "Chat " + string + "settings...", player, "");

		itemStackBuilder(back, Material.BOOK, "" + string + "Back...", player, "");

		itemStackBuilder(toggleTPA, Material.BOW, "" + string + "Allow " + variable1 + "TPA" + string + "s", player,
				"tpa");

		itemStackBuilder(acceptTPA, Material.BOW, "" + string + "Auto Accept " + variable1 + "TPA" + string + "s",
				player, "autotpa");

		itemStackBuilder(toggleCycle, Material.REPEATER, "" + string + "Enable" + variable1 + "Cycle", player, "cycle");

		itemStackBuilder(toggleMsg, Material.FEATHER, "" + string + "Allow " + variable1 + "Messages", player, "msg");

		itemStackBuilder(toggleSwear, Material.IRON_BARS, "" + string + "Toggle " + variable1 + "Swear Filter", player,
				"swearfilter");
	}

	public static ItemStack itemStackBuilder(ItemStack item, Material material, String displayName, Player player,
			String setting) {
		item = new ItemStack(material, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(displayName);
		if (setting != "") {
			ArrayList<String> itemLore = new ArrayList<String>();
			itemLore.add(string + "Enabled: " + variable2 + VaultCore.getInstance().getPlayerData()
					.get("players." + player.getUniqueId() + ".settings." + setting));
			if (VaultCore.getInstance().getPlayerData()
					.getBoolean("players." + player.getUniqueId() + ".settings." + setting)) {
				itemMeta.addEnchant(Enchantment.DURABILITY, 5, true);
			}
			item.setLore(itemLore);
		}
		item.setItemMeta(itemMeta);
		return item;
	}
}
