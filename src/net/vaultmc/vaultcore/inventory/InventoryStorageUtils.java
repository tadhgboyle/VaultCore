/*
 * VaultUtils: VaultMC functionalities provider.
 * Copyright (C) 2020 yangyang200
 *
 * VaultUtils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VaultUtils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VaultCore.  If not, see <https://www.gnu.org/licenses/>.
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

