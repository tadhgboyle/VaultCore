package net.vaultmc.vaultcore.survival.item;

import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class CraftingListener extends ConstructorRegisterListener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked().getWorld().getName().toLowerCase().contains("survival") && e.getClickedInventory() instanceof CraftingInventory) {
            if (e.getSlot() == 0 && e.getClickedInventory().getItem(0) != null && Item.getId(e.getClickedInventory().getItem(0)) != null) {
                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> {
                    for (int i = 1; i <= 9; i++) {
                        if (e.getClickedInventory().getItem(i) == null) continue;
                        ItemStack item = e.getClickedInventory().getItem(i).clone();
                        if (item.getAmount() == 1) {
                            e.getClickedInventory().clear(i);
                        } else {
                            item.setAmount(item.getAmount() - 1);
                            e.getClickedInventory().setItem(i, item);
                        }
                    }
                });
            }

            Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
                for (Item item : Item.getItems().values()) {
                    if (item.getRecipe() != null) {
                        if (item.getRecipe().isValid((CraftingInventory) e.getClickedInventory())) {
                            ((CraftingInventory) e.getClickedInventory()).setResult(item.getItem());
                            ((Player) e.getWhoClicked()).updateInventory();
                        }
                    }
                }
            }, 1);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getWhoClicked().getWorld().getName().toLowerCase().contains("survival") && e.getInventory() instanceof CraftingInventory) {
            Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
                for (Item item : Item.getItems().values()) {
                    if (item.getRecipe() != null) {
                        if (item.getRecipe().isValid((CraftingInventory) e.getInventory())) {
                            ((CraftingInventory) e.getInventory()).setResult(item.getItem());
                            ((Player) e.getWhoClicked()).updateInventory();
                        }
                    }
                }
            }, 1);
        }
    }
}
