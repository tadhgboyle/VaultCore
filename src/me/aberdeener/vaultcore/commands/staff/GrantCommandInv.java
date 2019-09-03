package me.aberdeener.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class GrantCommandInv {

	private static ItemStack memberRank, patreonRank, trustedRank, moderatorRank, adminRank, noPermission1, noPermission2;

	public static Inventory getGrantInventoryAdmin(Player target) {
		Inventory rankGrantAdmin = Bukkit.createInventory(null, 9,
				ChatColor.DARK_GRAY + "Grant Rank to " + ChatColor.WHITE + "" + ChatColor.ITALIC + target.getName());

		rankGrantAdmin.setItem(0, null);
		rankGrantAdmin.setItem(1, null);
		rankGrantAdmin.setItem(2, memberRank);
		rankGrantAdmin.setItem(3, patreonRank);
		rankGrantAdmin.setItem(4, trustedRank);
		rankGrantAdmin.setItem(5, moderatorRank);
		rankGrantAdmin.setItem(6, adminRank);
		rankGrantAdmin.setItem(7, null);
		rankGrantAdmin.setItem(8, null);

		return rankGrantAdmin;
	}
	
	public static Inventory getGrantInventoryMod(Player target) {
		Inventory rankGrantMod = Bukkit.createInventory(null, 9,
				ChatColor.DARK_GRAY + "Grant Rank to " + ChatColor.WHITE + "" + ChatColor.ITALIC + target.getName());

		rankGrantMod.setItem(0, null);
		rankGrantMod.setItem(1, null);
		rankGrantMod.setItem(2, memberRank);
		rankGrantMod.setItem(3, patreonRank);
		rankGrantMod.setItem(4, trustedRank);
		rankGrantMod.setItem(5, noPermission1);
		rankGrantMod.setItem(6, noPermission2);
		rankGrantMod.setItem(7, null);
		rankGrantMod.setItem(8, null);

		return rankGrantMod;
	}

	// when onEnable is run, create the wool and shit
	public static void initAdmin() {

		// wool in inventory slot for each rank
		memberRank = new ItemStack(Material.LIGHT_GRAY_WOOL, 1);
		ItemMeta memberRankMeta = memberRank.getItemMeta();
		memberRankMeta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Member");
		memberRank.setItemMeta(memberRankMeta);

		patreonRank = new ItemStack(Material.WHITE_WOOL, 1);
		ItemMeta patreonRankMeta = patreonRank.getItemMeta();
		patreonRankMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Patreon");
		patreonRank.setItemMeta(patreonRankMeta);

		trustedRank = new ItemStack(Material.LIGHT_BLUE_WOOL, 1);
		ItemMeta trustedRankMeta = trustedRank.getItemMeta();
		trustedRankMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Trusted");
		trustedRank.setItemMeta(trustedRankMeta);

		moderatorRank = new ItemStack(Material.CYAN_WOOL, 1);
		ItemMeta moderatorRankMeta = moderatorRank.getItemMeta();
		moderatorRankMeta.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Moderator");
		moderatorRank.setItemMeta(moderatorRankMeta);

		adminRank = new ItemStack(Material.BLUE_WOOL, 1);
		ItemMeta adminRankMeta = adminRank.getItemMeta();
		adminRankMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Admin");
		adminRank.setItemMeta(adminRankMeta);

	}
	
	// when onEnable is run, create the wool and shit
	public static void initMod() {
		
		// wool in inventory slot for each rank
		memberRank = new ItemStack(Material.LIGHT_GRAY_WOOL, 1);
		ItemMeta memberRankMeta = memberRank.getItemMeta();
		memberRankMeta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Member");
		memberRank.setItemMeta(memberRankMeta);

		patreonRank = new ItemStack(Material.WHITE_WOOL, 1);
		ItemMeta patreonRankMeta = patreonRank.getItemMeta();
		patreonRankMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Patreon");
		patreonRank.setItemMeta(patreonRankMeta);

		trustedRank = new ItemStack(Material.LIGHT_BLUE_WOOL, 1);
		ItemMeta trustedRankMeta = trustedRank.getItemMeta();
		trustedRankMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Trusted");
		trustedRank.setItemMeta(trustedRankMeta);

		noPermission1 = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
		ItemMeta noPermission1Meta = noPermission1.getItemMeta();
		noPermission1Meta.setDisplayName(ChatColor.RED + "No Permission");
		noPermission1.setItemMeta(noPermission1Meta);

		noPermission2 = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
		ItemMeta noPermission2Meta = adminRank.getItemMeta();
		noPermission2Meta.setDisplayName(ChatColor.RED + "No Permission");
		noPermission2.setItemMeta(noPermission2Meta);

	}
}