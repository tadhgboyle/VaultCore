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
        ItemStack iron = new ItemStack(Material.IRON_INGOT);
        ItemStack stick = new ItemStack(Material.STICK);
        ItemStack leather = new ItemStack(Material.LEATHER);
        ItemStack dirt = new ItemStack(Material.DIRT);
        new Item(new ItemStackBuilder(Material.LEATHER_HELMET)
                .name(ChatColor.RESET + "Soil Helmet")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD))
                .build(), "survival:dirt_helmet", new ShapedRecipe(new ItemStack[]{
                dirt, dirt, dirt,
                dirt, null, dirt,
                null, null, null
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_CHESTPLATE)
                .name(ChatColor.RESET + "Soil Chestplate")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST))
                .build(), "survival:dirt_chestplate", new ShapedRecipe(new ItemStack[]{
                dirt, null, dirt,
                dirt, dirt, dirt,
                dirt, dirt, dirt
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_LEGGINGS)
                .name(ChatColor.RESET + "Soil Leggings")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS))
                .build(), "survival:dirt_leggings", new ShapedRecipe(new ItemStack[]{
                dirt, dirt, dirt,
                dirt, null, dirt,
                dirt, null, dirt
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_BOOTS)
                .name(ChatColor.RESET + "Soil Boots")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET))
                .build(), "survival:dirt_boots", new ShapedRecipe(new ItemStack[]{
                dirt, null, dirt,
                dirt, null, dirt,
                null, null, null
        }));
        new Item(new ItemStackBuilder(Material.IRON_SWORD)
                .name(ChatColor.RESET + "Chainmail Sword")
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "Attack", 5.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND))
                .build(), "survival:chainmail_sword", new ShapedRecipe(new ItemStack[]{
                iron, null, null,
                leather, null, null,
                stick, null, null
        }));
        new Item(new ItemStack(Material.CHAINMAIL_HELMET), "survival:chainmail_helmet", new ShapedRecipe(new ItemStack[]{
                iron, leather, iron,
                leather, null, iron,
                null, null, null
        }));
        new Item(new ItemStack(Material.CHAINMAIL_CHESTPLATE), "survival:chainmail_chestplate", new ShapedRecipe(new ItemStack[]{
                iron, null, iron,
                leather, iron, leather,
                iron, leather, iron
        }));
        new Item(new ItemStack(Material.CHAINMAIL_LEGGINGS), "survival:chainmail_leggings", new ShapedRecipe(new ItemStack[]{
                iron, leather, iron,
                leather, null, iron,
                iron, null, leather
        }));
        new Item(new ItemStack(Material.CHAINMAIL_HELMET), "survival:chainmail_boots", new ShapedRecipe(new ItemStack[]{
                iron, null, leather,
                leather, null, iron,
                null, null, null
        }));
        new Item(new ItemStackBuilder(Material.IRON_SWORD)
                .name(ChatColor.RESET + "Forged Iron Sword")
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "Attack", 6.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND))
                .build(), "survival:forged_iron_sword", new ShapedRecipe(new ItemStack[]{
                iron, iron, null,
                iron, iron, null,
                stick, null, null
        }));
        ItemStack diamond = new ItemStack(Material.DIAMOND);
        ItemStack emerald = new ItemStack(Material.EMERALD);
        new Item(new ItemStackBuilder(Material.DIAMOND_SWORD)
                .name(ChatColor.RESET + "Emermond Sword")
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "Attack", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND))
                .build(), "survival:emerald_diamond_mixed_sword", new ShapedRecipe(new ItemStack[]{
                emerald, null, null,
                diamond, null, null,
                stick, null, null
        }));
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
