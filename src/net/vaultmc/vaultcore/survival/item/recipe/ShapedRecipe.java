package net.vaultmc.vaultcore.survival.item.recipe;

import lombok.Getter;
import net.vaultmc.vaultcore.survival.item.Item;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShapedRecipe implements Recipe {
    @Getter
    private ItemStack[] recipe;

    @Getter
    private Set<Map<Integer, ItemStack>> possible = new HashSet<>();

    public ShapedRecipe(ItemStack[] recipe) {
        // Find all the possibilities of how the player will lay the crafting recipe out.
        this.recipe = recipe;

        Map<Integer, ItemStack> ingredients = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            if (recipe[i] != null) {
                ingredients.put(i, recipe[i]);
            }
        }
        possible.add(ingredients);
        for (int i = 0; i < 4; i++) {
            outerLoop:
            for (int j = 1; j <= 2; j++) {
                if (i == 0) {  // Right
                    Map<Integer, ItemStack> map = new HashMap<>();
                    for (Map.Entry<Integer, ItemStack> entry : ingredients.entrySet()) {
                        if (entry.getValue() == null) continue;
                        int key = entry.getKey() - j;
                        if ((entry.getKey() >= 0 && entry.getKey() <= 2 && key > 2) ||
                                (entry.getKey() >= 3 && entry.getKey() <= 5 && key > 5) ||  // Overflew to next row. Not a possibility.
                                (entry.getKey() >= 6 && entry.getKey() <= 8 && key > 8)) {
                            continue outerLoop;
                        }
                        map.put(entry.getKey() + j, entry.getValue());
                    }
                    possible.add(map);
                } else if (i == 1) {  // Left
                    Map<Integer, ItemStack> map = new HashMap<>();
                    for (Map.Entry<Integer, ItemStack> entry : ingredients.entrySet()) {
                        if (entry.getValue() == null) continue;
                        int key = entry.getKey() - j;
                        if ((entry.getKey() >= 0 && entry.getKey() <= 2 && key < 0) ||
                                (entry.getKey() >= 3 && entry.getKey() <= 5 && key < 3) ||  // Overflew to previous row. Not a possibility.
                                (entry.getKey() >= 6 && entry.getKey() <= 8 && key < 6)) {
                            continue outerLoop;
                        }
                        map.put(entry.getKey() - j, entry.getValue());
                    }
                    possible.add(map);
                } else if (i == 2) {  // Up
                    Map<Integer, ItemStack> map = new HashMap<>();
                    for (Map.Entry<Integer, ItemStack> entry : ingredients.entrySet()) {
                        if (entry.getValue() == null) continue;
                        if (entry.getKey() - 3 * j < 0)
                            continue outerLoop;  // Overflew outside of the inventory. Not a possibility.
                        map.put(entry.getKey() - 3 * j, entry.getValue());
                    }
                    possible.add(map);
                } else {  // Down
                    Map<Integer, ItemStack> map = new HashMap<>();
                    for (Map.Entry<Integer, ItemStack> entry : ingredients.entrySet()) {
                        if (entry.getValue() == null) continue;
                        if (entry.getKey() + 3 * j > 8)
                            continue outerLoop;  // Overflew outside of the inventory. Not a possibility.
                        map.put(entry.getKey() + 3 * j, entry.getValue());
                    }
                    possible.add(map);
                }
            }
        }

        for (Map<Integer, ItemStack> is : possible) {
            for (int i = 0; i < 9; i++) {
                if (!is.containsKey(i)) {
                    is.put(i, null);
                }
            }
        }
    }

    private static boolean isSimilar(ItemStack i1, ItemStack i2) {
        if (i1 == null && i2 == null) return true;
        if (i1 == null || i2 == null) return false;

        String id1 = Item.getId(i1);
        String id2 = Item.getId(i2);

        if (id1 != null && id2 != null) {
            return id1.equals(id2);
        }

        return i1.getType() == i2.getType() && i1.getDurability() == i2.getDurability();
    }

    @Override
    public boolean isValid(CraftingInventory inv) {
        outerLoop:
        for (Map<Integer, ItemStack> map : possible) {
            for (Map.Entry<Integer, ItemStack> entry : map.entrySet()) {
                // TODO: @yangyang200: ArrayIndexOutOfBound Exception here
                if (!isSimilar(entry.getValue(), inv.getMatrix()[entry.getKey()])) {
                    continue outerLoop;
                }
            }
            return true;
        }
        return false;
    }
}
