package net.vaultmc.vaultcore.survival.item;

import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.CraftingInventory;

public class CraftingListener extends ConstructorRegisterListener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked().getWorld().getName().toLowerCase().contains("survival") && e.getClickedInventory() instanceof CraftingInventory) {
            for (Item item : Item.getItems().values()) {
                if (item.getRecipe() != null) {
                    if (item.getRecipe().isValid((CraftingInventory) e.getClickedInventory())) {
                        Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> ((CraftingInventory) e.getClickedInventory()).setResult(item.getItem()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getWhoClicked().getWorld().getName().toLowerCase().contains("survival") && e.getInventory() instanceof CraftingInventory) {
            for (Item item : Item.getItems().values()) {
                if (item.getRecipe() != null) {
                    if (item.getRecipe().isValid((CraftingInventory) e.getInventory())) {
                        Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> ((CraftingInventory) e.getInventory()).setResult(item.getItem()));
                    }
                }
            }
        }
    }
}
