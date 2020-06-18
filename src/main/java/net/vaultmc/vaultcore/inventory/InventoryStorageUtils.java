/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

package net.vaultmc.vaultcore.inventory;

import net.vaultmc.vaultcore.VaultCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

public class InventoryStorageUtils {
    public static void storePlayerInventory(PlayerInventory inv, String location) {
        FileConfiguration config = VaultCore.getInstance().getInventoryData();
        for (int i = 0; i <= 40; i++) {
            config.set(location + "." + i, inv.getItem(i));
        }
        VaultCore.getInstance().saveConfig();
    }

    public static void restorePlayerInventory(PlayerInventory inv, String location) {
        FileConfiguration config = VaultCore.getInstance().getInventoryData();
        for (int i = 0; i <= 40; i++) {
            inv.setItem(i, config.getItemStack(location + "." + i));
        }
    }

    public static void storeChest(Inventory inv, String location) {
        FileConfiguration config = VaultCore.getInstance().getInventoryData();
        for (int i = 0; i <= 26; i++) {
            config.set(location + "." + i, inv.getItem(i));
        }
        VaultCore.getInstance().saveConfig();
    }

    public static void restoreChest(Inventory inv, String location) {
        FileConfiguration config = VaultCore.getInstance().getInventoryData();
        for (int i = 0; i <= 26; i++) {
            inv.setItem(i, config.getItemStack(location + "." + i));
        }
    }
}
