package net.vaultmc.vaultcore.commands.settings;

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class SettingsInventories {
    static String string = Utilities.string;
    static String variable1 = Utilities.variable1;
    static String variable2 = Utilities.variable2;
    private static ItemStack teleportMain, creativeMain, chatMain, back;
    private static ItemStack toggleTPA, acceptTPA;
    private static ItemStack toggleCycle;
    private static ItemStack toggleMsg, togglePWC, toggleSwear;

    public static Inventory settingsMain() {
        Inventory inv = Bukkit.createInventory(null, 27, "Settings");

        inv.setItem(11, teleportMain);
        inv.setItem(13, creativeMain);
        inv.setItem(15, chatMain);

        return inv;
    }

    public static Inventory teleportationSettings() {
        Inventory inv = Bukkit.createInventory(null, 36, "Teleportation Settings");

        inv.setItem(11, toggleTPA);
        inv.setItem(15, acceptTPA);
        inv.setItem(31, back);

        return inv;
    }

    public static Inventory creativeSettings() {
        Inventory inv = Bukkit.createInventory(null, 36, "Creative Settings");

        inv.setItem(13, toggleCycle);
        inv.setItem(31, back);

        return inv;
    }

    public static Inventory chatSettings() {
        Inventory inv = Bukkit.createInventory(null, 36, "Chat Settings");

        inv.setItem(10, toggleMsg);
        inv.setItem(13, togglePWC);
        inv.setItem(16, toggleSwear);
        inv.setItem(31, back);

        return inv;
    }

    public static void init(VLPlayer player) {
        // FIXME from @yangyang200
        //  What if two players executed /settings at the same time?
        //  Won't this cause issues and make one of the players see the wrong value?

        teleportMain = new ItemStack(Material.ENDER_PEARL, 1);
        ItemMeta teleportMainMeta = teleportMain.getItemMeta();
        teleportMainMeta.setDisplayName(variable1 + "Teleportation " + string + "settings...");
        teleportMain.setItemMeta(teleportMainMeta);

        creativeMain = new ItemStack(Material.GRASS_BLOCK, 1);
        ItemMeta creativeMainMeta = creativeMain.getItemMeta();
        creativeMainMeta.setDisplayName(variable1 + "Creative " + string + "settings...");
        creativeMain.setItemMeta(creativeMainMeta);

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
        ArrayList<String> toggleTPALore = new ArrayList<>();
        toggleTPALore.add(string + "Enabled: " + variable2
                + VaultCore.getInstance().getPlayerData().get("players." + player.getUniqueId() + ".settings.tpa"));
        if (VaultCore.getInstance().getPlayerData().getBoolean("players." + player.getUniqueId() + ".settings.tpa")) {
            toggleTPAMeta.addEnchant(Enchantment.DURABILITY, 5, true);
        }
        toggleTPAMeta.setLore(toggleTPALore);
        toggleTPA.setItemMeta(toggleTPAMeta);

        acceptTPA = new ItemStack(Material.ARROW, 1);
        ItemMeta acceptTPAMeta = acceptTPA.getItemMeta();
        acceptTPAMeta.setDisplayName(string + "Auto Accept " + variable1 + "TPA" + string + "s");
        ArrayList<String> acceptTPALore = new ArrayList<>();
        acceptTPALore.add(string + "Enabled: " + variable2
                + VaultCore.getInstance().getPlayerData().get("players." + player.getUniqueId() + ".settings.autotpa"));
        if (VaultCore.getInstance().getPlayerData()
                .getBoolean("players." + player.getUniqueId() + ".settings.autotpa")) {
            acceptTPAMeta.addEnchant(Enchantment.DURABILITY, 5, true);
        }
        acceptTPAMeta.setLore(acceptTPALore);
        acceptTPA.setItemMeta(acceptTPAMeta);

        toggleCycle = new ItemStack(Material.REPEATER, 1);
        ItemMeta toggleCycleMeta = toggleCycle.getItemMeta();
        toggleCycleMeta.setDisplayName(string + "Enable " + variable1 + "Cycle");
        ArrayList<String> toggleCycleLore = new ArrayList<>();
        toggleCycleLore.add(string + "Enabled: " + variable2
                + VaultCore.getInstance().getPlayerData().get("players." + player.getUniqueId() + ".settings.cycle"));
        if (VaultCore.getInstance().getPlayerData().getBoolean("players." + player.getUniqueId() + ".settings.cycle")) {
            toggleCycleMeta.addEnchant(Enchantment.DURABILITY, 5, true);
        }
        toggleCycleMeta.setLore(toggleCycleLore);
        toggleCycle.setItemMeta(toggleCycleMeta);

        toggleMsg = new ItemStack(Material.FEATHER, 1);
        ItemMeta toggleMsgMeta = toggleMsg.getItemMeta();
        toggleMsgMeta.setDisplayName(string + "Allow " + variable1 + "Messages");
        ArrayList<String> toggleMsgLore = new ArrayList<>();
        toggleMsgLore.add(string + "Enabled: " + variable2
                + VaultCore.getInstance().getPlayerData().get("players." + player.getUniqueId() + ".settings.msg"));
        if (VaultCore.getInstance().getPlayerData().getBoolean("players." + player.getUniqueId() + ".settings.msg")) {
            toggleMsgMeta.addEnchant(Enchantment.DURABILITY, 5, true);
        }
        toggleMsgMeta.setLore(toggleMsgLore);
        toggleMsg.setItemMeta(toggleMsgMeta);

        togglePWC = new ItemStack(Material.FILLED_MAP, 1);
        ItemMeta togglePWCMeta = togglePWC.getItemMeta();
        togglePWCMeta.setDisplayName(string + "Use " + variable1 + "Per World Chat");
        ArrayList<String> togglePWCLore = new ArrayList<>();
        togglePWCLore.add(string + "Enabled: " + variable2
                + VaultCore.getInstance().getPlayerData().get("players." + player.getUniqueId() + ".settings.pwc"));
        if (VaultCore.getInstance().getPlayerData().getBoolean("players." + player.getUniqueId() + ".settings.pwc")) {
            togglePWCMeta.addEnchant(Enchantment.DURABILITY, 5, true);
        }
        togglePWCMeta.setLore(togglePWCLore);
        togglePWC.setItemMeta(togglePWCMeta);

        toggleSwear = new ItemStack(Material.IRON_BARS, 1);
        ItemMeta toggleSwearMeta = toggleSwear.getItemMeta();
        toggleSwearMeta.setDisplayName(string + "Toggle " + variable1 + "Swear Filter");
        ArrayList<String> toggleSwearLore = new ArrayList<>();
        toggleSwearLore.add(string + "Enabled: " + variable2 + VaultCore.getInstance().getPlayerData()
                .get("players." + player.getUniqueId() + ".settings.swearfilter"));
        if (VaultCore.getInstance().getPlayerData()
                .getBoolean("players." + player.getUniqueId() + ".settings.swearfilter")) {
            toggleSwearMeta.addEnchant(Enchantment.DURABILITY, 5, true);
        }
        toggleSwearMeta.setLore(toggleSwearLore);
        toggleSwear.setItemMeta(toggleSwearMeta);
    }
}
