package net.vaultmc.vaultcore.survival.item.recipe;

import org.bukkit.inventory.CraftingInventory;

public interface Recipe {
    boolean isValid(CraftingInventory inv);
}
