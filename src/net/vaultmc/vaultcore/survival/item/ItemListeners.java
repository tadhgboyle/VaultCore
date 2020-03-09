package net.vaultmc.vaultcore.survival.item;

import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.stream.Collectors;

public class ItemListeners extends ConstructorRegisterListener {
    @EventHandler
    public void onEntityDamaged(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            double defense = 0;
            double toughness = 0;
            Player player = (Player) e.getEntity();
            for (int i = 36; i < 40; i++) {
                if (player.getInventory().getItem(i) != null) {
                    ItemStack item = player.getInventory().getItem(1);
                    Set<Double> ints = item.getItemMeta().getAttributeModifiers(Attribute.GENERIC_ARMOR).stream().map(AttributeModifier::getAmount).collect(Collectors.toSet());
                    for (double d : ints) {
                        defense += d;
                    }
                    Set<Double> toughnesses = item.getItemMeta().getAttributeModifiers(Attribute.GENERIC_ARMOR_TOUGHNESS).stream().map(AttributeModifier::getAmount).collect(Collectors.toSet());
                    for (double d : toughnesses) {
                        toughness += d;
                    }
                }
            }

            double actualDamage = e.getDamage() * (1 - ((Math.max(defense / 5D, defense - (e.getDamage() / (2 + (toughness / 4))))) / 25));
            e.setDamage(EntityDamageEvent.DamageModifier.ARMOR, e.getDamage() - actualDamage);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.isCancelled()) return;
        if (e.getWhoClicked().getWorld().getName().toLowerCase().contains("survival") && e.getClickedInventory() instanceof CraftingInventory && !e.getView().getTitle().equals("Recipe")) {
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.isCancelled()) return;
        if (e.getWhoClicked().getWorld().getName().toLowerCase().contains("survival") && e.getInventory() instanceof CraftingInventory && !e.getView().getTitle().equals("Recipe")) {
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
