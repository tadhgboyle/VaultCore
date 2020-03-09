package net.vaultmc.vaultcore.survival.item;

import net.vaultmc.vaultcore.survival.item.recipe.ShapedRecipe;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ItemRegistry {
    static {
        ItemStack emerald = new ItemStack(Material.EMERALD);
        ItemStack stick = new ItemStack(Material.STICK);
        new Item(new ItemStackBuilder(Material.DIAMOND_SWORD)
                .name(ChatColor.RESET + "Emerald Sword")
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "Attack", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND))
                .build(), "survival:emerald_sword", new ShapedRecipe(new ItemStack[]{
                emerald, null, null,
                emerald, null, null,
                stick, null, null
        }));
    }

    public static void load() {
        // Call static initializer
    }
}
