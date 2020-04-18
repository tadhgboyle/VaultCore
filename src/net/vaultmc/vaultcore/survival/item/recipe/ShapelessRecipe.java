package net.vaultmc.vaultcore.survival.item.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.vaultmc.vaultcore.survival.item.Item;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ShapelessRecipe implements Recipe {
    @Getter
    private final ItemStack[] ingredients;

    @Override
    public boolean isValid(CraftingInventory inv) {
        List<String> ingredients = Arrays.stream(this.ingredients).map(Item::getId).collect(Collectors.toList());
        List<String> matrix = Arrays.stream(inv.getMatrix()).map(Item::getId).collect(Collectors.toList());
        return matrix.containsAll(ingredients) && ingredients.containsAll(matrix);
    }
}
