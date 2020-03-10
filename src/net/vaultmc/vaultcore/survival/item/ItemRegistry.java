package net.vaultmc.vaultcore.survival.item;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import net.vaultmc.vaultcore.survival.item.recipe.ShapedRecipe;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import java.util.*;

public class ItemRegistry extends ConstructorRegisterListener {
    private static final Set<Integer> reinforcedBowArrows = new HashSet<>();

    static {
        ItemStack iron = new ItemStack(Material.IRON_INGOT);
        ItemStack stick = new ItemStack(Material.STICK);
        ItemStack leather = new ItemStack(Material.LEATHER);
        ItemStack dirt = new ItemStack(Material.DIRT);
        ItemStack itemFrame = new ItemStack(Material.ITEM_FRAME);
        ItemStack nameTag = new ItemStack(Material.NAME_TAG);
        new Item(new ItemStack(Material.PLAYER_HEAD), "minecraft:player_head", new ShapedRecipe(new ItemStack[]{
                itemFrame, null, null,
                nameTag, null, null,
                null, null, null
        }));
        ItemStack flint = new ItemStack(Material.FLINT);
        ItemStack coal = new ItemStack(Material.COAL);
        ItemStack tnt = new ItemStack(Material.TNT);
        new Item(new ItemStackBuilder(Material.PLAYER_HEAD)
                // https://minecraft-heads.com/custom-heads/decoration/2934-bomb
                .skullOwner("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWIyMGZmMTczYmQxN2IyYzRmMmViMjFmM2M0YjQzODQxYTE0YjMxZGZiZmQzNTRhM2JlYzgyNjNhZjU2MmIifX19")
                .name(ChatColor.RESET + "Bomb")
                .lore(Collections.singletonList(ChatColor.GRAY + "Explodes after being placed for 5 seconds."))
                .build(), "survival:bomb", new ShapedRecipe(new ItemStack[]{
                flint, tnt, coal,
                null, null, null,
                null, null, null
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_HELMET)
                .name(ChatColor.RESET + "Soil Helmet")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "" + ChatColor.BOLD + "Full Set Bonus:",
                        ChatColor.YELLOW + "Remove Fall Damage"
                ))
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD))
                .build(), "survival:dirt_helmet", new ShapedRecipe(new ItemStack[]{
                dirt, dirt, dirt,
                dirt, null, dirt,
                null, null, null
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_CHESTPLATE)
                .name(ChatColor.RESET + "Soil Chestplate")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "" + ChatColor.BOLD + "Full Set Bonus:",
                        ChatColor.YELLOW + "Remove Fall Damage"
                ))
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST))
                .build(), "survival:dirt_chestplate", new ShapedRecipe(new ItemStack[]{
                dirt, null, dirt,
                dirt, dirt, dirt,
                dirt, dirt, dirt
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_LEGGINGS)
                .name(ChatColor.RESET + "Soil Leggings")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "" + ChatColor.BOLD + "Full Set Bonus:",
                        ChatColor.YELLOW + "Remove Fall Damage"
                ))
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS))
                .build(), "survival:dirt_leggings", new ShapedRecipe(new ItemStack[]{
                dirt, dirt, dirt,
                dirt, null, dirt,
                dirt, null, dirt
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_BOOTS)
                .name(ChatColor.RESET + "Soil Boots")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "" + ChatColor.BOLD + "Full Set Bonus:",
                        ChatColor.YELLOW + "Remove Fall Damage"
                ))
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET))
                .build(), "survival:dirt_boots", new ShapedRecipe(new ItemStack[]{
                dirt, null, dirt,
                dirt, null, dirt,
                null, null, null
        }));
        new Item(new ItemStackBuilder(Material.IRON_SWORD)
                .name(ChatColor.RESET + "Chainmail Sword")
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "Attack", 5.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND))
                .maxDurability(200)
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
        new Item(new ItemStack(Material.CHAINMAIL_BOOTS), "survival:chainmail_boots", new ShapedRecipe(new ItemStack[]{
                iron, null, leather,
                leather, null, iron,
                null, null, null
        }));
        ItemStack cactus = new ItemStack(Material.CACTUS);
        new Item(new ItemStackBuilder(Material.LEATHER_HELMET)
                .leatherColor(Color.fromRGB(0, 185, 0))
                .name(ChatColor.RESET + "Cactus Helmet")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Damage the attacker by 1/4 of",
                        ChatColor.GRAY + "the damage you receieved."
                ))
                .maxDurability(150)
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD))
                .build(), "survival:cactus_helmet", new ShapedRecipe(new ItemStack[]{
                cactus, cactus, cactus,
                cactus, null, cactus,
                null, null, null
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_CHESTPLATE)
                .leatherColor(Color.fromRGB(0, 185, 0))
                .name(ChatColor.RESET + "Cactus Chestplate")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Damage the attacker by 1/4 of",
                        ChatColor.GRAY + "the damage you receieved."
                ))
                .maxDurability(150)
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST))
                .build(), "survival:cactus_chestplate", new ShapedRecipe(new ItemStack[]{
                cactus, null, cactus,
                cactus, cactus, cactus,
                cactus, cactus, cactus
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_LEGGINGS)
                .leatherColor(Color.fromRGB(0, 185, 0))
                .name(ChatColor.RESET + "Cactus Leggings")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Damage the attacker by 1/4 of",
                        ChatColor.GRAY + "the damage you receieved."
                ))
                .maxDurability(150)
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS))
                .build(), "survival:cactus_leggings", new ShapedRecipe(new ItemStack[]{
                cactus, cactus, cactus,
                cactus, null, cactus,
                cactus, null, cactus
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_BOOTS)
                .leatherColor(Color.fromRGB(0, 185, 0))
                .name(ChatColor.RESET + "Cactus Boots")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Damage the attacker by 1/4 of",
                        ChatColor.GRAY + "the damage you receieved."
                ))
                .maxDurability(150)
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET))
                .build(), "survival:cactus_boots", new ShapedRecipe(new ItemStack[]{
                cactus, null, cactus,
                cactus, null, cactus,
                null, null, null
        }));
        ItemStack lapis = new ItemStack(Material.LAPIS_LAZULI);
        new Item(new ItemStackBuilder(Material.LEATHER_HELMET)
                .leatherColor(Color.BLUE)
                .name(ChatColor.RESET + "Lapis Helmet")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "" + ChatColor.BOLD + "Full Set Bonus:",
                        ChatColor.YELLOW + "2x Experience Everywhere"
                ))
                .maxDurability(384)
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD))
                .build(), "survival:lapis_helmet", new ShapedRecipe(new ItemStack[]{
                lapis, lapis, lapis,
                lapis, null, lapis,
                null, null, null
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_CHESTPLATE)
                .leatherColor(Color.BLUE)
                .name(ChatColor.RESET + "Lapis Chestplate")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "" + ChatColor.BOLD + "Full Set Bonus:",
                        ChatColor.YELLOW + "2x Experience Everywhere"
                ))
                .maxDurability(384)
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST))
                .build(), "survival:lapis_chestplate", new ShapedRecipe(new ItemStack[]{
                lapis, null, lapis,
                lapis, lapis, lapis,
                lapis, lapis, lapis
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_LEGGINGS)
                .leatherColor(Color.BLUE)
                .name(ChatColor.RESET + "Lapis Leggings")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "" + ChatColor.BOLD + "Full Set Bonus:",
                        ChatColor.YELLOW + "2x Experience Everywhere"
                ))
                .maxDurability(384)
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS))
                .build(), "survival:lapis_leggings", new ShapedRecipe(new ItemStack[]{
                lapis, lapis, lapis,
                lapis, null, lapis,
                lapis, null, lapis
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_BOOTS)
                .leatherColor(Color.BLUE)
                .name(ChatColor.RESET + "Lapis Boots")
                .lore(Arrays.asList(
                        ChatColor.GOLD + "" + ChatColor.BOLD + "Full Set Bonus:",
                        ChatColor.YELLOW + "2x Experience Everywhere"
                ))
                .maxDurability(384)
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET))
                .build(), "survival:lapis_boots", new ShapedRecipe(new ItemStack[]{
                lapis, null, lapis,
                lapis, null, lapis,
                null, null, null
        }));
        ItemStack diamond = new ItemStack(Material.DIAMOND);
        ItemStack ironBlock = new ItemStack(Material.IRON_BLOCK);
        ItemStack diaBlock = new ItemStack(Material.DIAMOND_BLOCK);
        new Item(new ItemStackBuilder(Material.IRON_HELMET)
                .name(ChatColor.RESET + "Reinforced Iron Helmet")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD))
                .build(), "survival:reinforced_iron_helmet", new ShapedRecipe(new ItemStack[]{
                iron, ironBlock, iron,
                ironBlock, null, iron,
                null, null, null
        }));
        new Item(new ItemStackBuilder(Material.IRON_CHESTPLATE)
                .name(ChatColor.RESET + "Reinforced Iron Chestplate")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST))
                .build(), "survival:reinforced_iron_chestplate", new ShapedRecipe(new ItemStack[]{
                iron, null, iron,
                ironBlock, iron, ironBlock,
                iron, ironBlock, iron
        }));
        new Item(new ItemStackBuilder(Material.IRON_LEGGINGS)
                .name(ChatColor.RESET + "Reinforced Iron Leggings")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 7, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS))
                .build(), "survival:reinforced_iron_leggings", new ShapedRecipe(new ItemStack[]{
                iron, ironBlock, iron,
                ironBlock, null, iron,
                iron, null, ironBlock
        }));
        new Item(new ItemStackBuilder(Material.IRON_BOOTS)
                .name(ChatColor.RESET + "Reinforced Iron Boots")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET))
                .build(), "survival:reinforced_iron_boots", new ShapedRecipe(new ItemStack[]{
                iron, null, ironBlock,
                ironBlock, null, iron,
                null, null, null
        }));
        new Item(new ItemStackBuilder(Material.DIAMOND_HELMET)
                .name(ChatColor.RESET + "Reinforced Diamond Helmet")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD))
                .build(), "survival:reinforced_diamond_helmet", new ShapedRecipe(new ItemStack[]{
                diamond, diaBlock, diamond,
                diaBlock, null, diamond,
                null, null, null
        }));
        new Item(new ItemStackBuilder(Material.DIAMOND_CHESTPLATE)
                .name(ChatColor.RESET + "Reinforced Diamond Chestplate")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 15, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST))
                .build(), "survival:reinforced_diamond_chestplate", new ShapedRecipe(new ItemStack[]{
                diamond, null, diamond,
                diaBlock, diamond, diaBlock,
                diamond, diaBlock, diamond
        }));
        new Item(new ItemStackBuilder(Material.DIAMOND_LEGGINGS)
                .name(ChatColor.RESET + "Reinforced Diamond Leggings")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 12, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS))
                .build(), "survival:reinforced_diamond_leggings", new ShapedRecipe(new ItemStack[]{
                diamond, diaBlock, diamond,
                diaBlock, null, diamond,
                diamond, null, diaBlock
        }));
        new Item(new ItemStackBuilder(Material.DIAMOND_BOOTS)
                .name(ChatColor.RESET + "Reinforced Diamond Boots")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET))
                .build(), "survival:reinforced_diamond_boots", new ShapedRecipe(new ItemStack[]{
                diamond, null, diaBlock,
                diaBlock, null, diamond,
                null, null, null
        }));
        ItemStack stone = new ItemStack(Material.STONE);
        ItemStack stoneSlab = new ItemStack(Material.STONE_SLAB);
        new Item(new ItemStackBuilder(Material.STONE_SWORD)
                .name(ChatColor.RESET + "Reinforced Stone Sword")
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "Attack", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND))
                .build(), "survival:reinforced_stone_sword", new ShapedRecipe(new ItemStack[]{
                stoneSlab, null, null,
                stone, null, null,
                stick, null, null
        }));
        new Item(new ItemStackBuilder(Material.IRON_SWORD)
                .name(ChatColor.RESET + "Reinforced Iron Sword")
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "Attack", 8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND))
                .build(), "survival:reinforced_iron_sword", new ShapedRecipe(new ItemStack[]{
                iron, null, null,
                ironBlock, null, null,
                stick, null, null
        }));
        new Item(new ItemStackBuilder(Material.DIAMOND_SWORD)
                .name(ChatColor.RESET + "Reinforced Diamond Sword")
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "Attack", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND))
                .build(), "survival:reinforced_diamond_sword", new ShapedRecipe(new ItemStack[]{
                diamond, null, null,
                diaBlock, null, null,
                stick, null, null
        }));
        new Item(new ItemStackBuilder(Material.BOW)
                .name(ChatColor.RESET + "Reinforced Bow")
                .build(), "survival:reinforced_bow", new ShapedRecipe(new ItemStack[]{
                null, stick, iron,
                stick, null, diamond,
                null, stick, iron
        }));
        ItemStack obsidian = new ItemStack(Material.OBSIDIAN);
        new Item(new ItemStackBuilder(Material.IRON_HELMET)
                .name(ChatColor.RESET + "Magic Proof Iron Helmet")
                .lore(Collections.singletonList(ChatColor.GRAY + "Reduce damage from potion by 1/4."))
                .build(), "survival:magic_proof_iron_helmet", new ShapedRecipe(new ItemStack[]{
                iron, obsidian, iron,
                iron, null, iron,
                null, null, null
        }));
        new Item(new ItemStackBuilder(Material.IRON_CHESTPLATE)
                .name(ChatColor.RESET + "Magic Proof Iron Chestplate")
                .lore(Collections.singletonList(ChatColor.GRAY + "Reduce damage from potion by 1/4."))
                .build(), "survival:magic_proof_iron_chestplate", new ShapedRecipe(new ItemStack[]{
                iron, null, iron,
                iron, obsidian, iron,
                iron, iron, iron
        }));
        new Item(new ItemStackBuilder(Material.IRON_LEGGINGS)
                .name(ChatColor.RESET + "Magic Proof Iron Leggings")
                .lore(Collections.singletonList(ChatColor.GRAY + "Reduce damage from potion by 1/4."))
                .build(), "survival:magic_proof_iron_leggings", new ShapedRecipe(new ItemStack[]{
                iron, obsidian, iron,
                iron, null, iron,
                iron, null, iron
        }));
        new Item(new ItemStackBuilder(Material.IRON_BOOTS)
                .name(ChatColor.RESET + "Magic Proof Iron Boots")
                .lore(Collections.singletonList(ChatColor.GRAY + "Reduce damage from potion by 1/4."))
                .build(), "survival:magic_proof_iron_boots", new ShapedRecipe(new ItemStack[]{
                iron, null, obsidian,
                iron, null, iron,
                null, null, null
        }));
        new Item(new ItemStackBuilder(Material.DIAMOND_HELMET)
                .name(ChatColor.RESET + "Magic Proof Diamond Helmet")
                .lore(Collections.singletonList(ChatColor.GRAY + "Reduce damage from potion by 1/4."))
                .build(), "survival:magic_proof_diamond_helmet", new ShapedRecipe(new ItemStack[]{
                diamond, obsidian, diamond,
                diamond, null, diamond,
                null, null, null
        }));
        new Item(new ItemStackBuilder(Material.DIAMOND_CHESTPLATE)
                .name(ChatColor.RESET + "Magic Proof Diamond Chestplate")
                .lore(Collections.singletonList(ChatColor.GRAY + "Reduce damage from potion by 1/4."))
                .build(), "survival:magic_proof_diamond_chestplate", new ShapedRecipe(new ItemStack[]{
                diamond, null, diamond,
                diamond, obsidian, diamond,
                diamond, diamond, diamond
        }));
        new Item(new ItemStackBuilder(Material.DIAMOND_LEGGINGS)
                .name(ChatColor.RESET + "Magic Proof Diamond Leggings")
                .lore(Collections.singletonList(ChatColor.GRAY + "Reduce damage from potion by 1/4."))
                .build(), "survival:magic_proof_diamond_leggings", new ShapedRecipe(new ItemStack[]{
                diamond, obsidian, diamond,
                diamond, null, diamond,
                diamond, null, diamond
        }));
        new Item(new ItemStackBuilder(Material.DIAMOND_BOOTS)
                .name(ChatColor.RESET + "Magic Proof Diamond Boots")
                .lore(Collections.singletonList(ChatColor.GRAY + "Reduce damage from potion by 1/4."))
                .build(), "survival:magic_proof_diamond_boots", new ShapedRecipe(new ItemStack[]{
                diamond, null, obsidian,
                diamond, null, diamond,
                null, null, null
        }));
        ItemStack emerald = new ItemStack(Material.EMERALD);
        new Item(new ItemStackBuilder(Material.DIAMOND_SWORD)
                .name(ChatColor.RESET + "Emermond Sword")
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "Attack", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND))
                .maxDurability(2000)
                .build(), "survival:emerald_diamond_mixed_sword", new ShapedRecipe(new ItemStack[]{
                emerald, null, null,
                diamond, null, null,
                stick, null, null
        }));
        new Item(new ItemStackBuilder(Material.DIAMOND_SWORD)
                .name(ChatColor.RESET + "Emerald Sword")
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "Attack", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND))
                .maxDurability(2500)
                .build(), "survival:emerald_sword", new ShapedRecipe(new ItemStack[]{
                emerald, null, null,
                emerald, null, null,
                stick, null, null
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_HELMET)
                .leatherColor(Color.LIME)
                .name(ChatColor.RESET + "Emerald Helmet")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD))
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "Toughness", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD))
                .maxDurability(550)
                .build(), "survival:emerald_helmet", new ShapedRecipe(new ItemStack[]{
                emerald, emerald, emerald,
                emerald, null, emerald,
                null, null, null
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_CHESTPLATE)
                .leatherColor(Color.LIME)
                .name(ChatColor.RESET + "Emerald Chestplate")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 15, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST))
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "Toughness", 8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST))
                .maxDurability(750)
                .build(), "survival:emerald_chestplate", new ShapedRecipe(new ItemStack[]{
                emerald, null, emerald,
                emerald, emerald, emerald,
                emerald, emerald, emerald
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_LEGGINGS)
                .leatherColor(Color.LIME)
                .name(ChatColor.RESET + "Emerald Leggings")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS))
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "Toughness", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS))
                .maxDurability(530)
                .build(), "survival:emerald_leggings", new ShapedRecipe(new ItemStack[]{
                emerald, emerald, emerald,
                emerald, null, emerald,
                emerald, null, emerald
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_BOOTS)
                .leatherColor(Color.LIME)
                .name(ChatColor.RESET + "Emerald Boots")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET))
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "Toughness", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET))
                .maxDurability(500)
                .build(), "survival:emerald_boots", new ShapedRecipe(new ItemStack[]{
                emerald, null, emerald,
                emerald, null, emerald,
                null, null, null
        }));
        new Item(new ItemStackBuilder(Material.DIAMOND_SWORD)
                .name(ChatColor.RESET + "Obsidian Sword")
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "Attack", 15, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND))
                .maxDurability(3000)
                .build(), "survival:obsidian_sword", new ShapedRecipe(new ItemStack[]{
                obsidian, null, null,
                obsidian, null, null,
                stick, null, null
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_HELMET)
                .leatherColor(Color.BLACK)
                .name(ChatColor.RESET + "Obsidian Helmet")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD))
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "Toughness", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD))
                .maxDurability(900)
                .build(), "survival:obsidian_helmet", new ShapedRecipe(new ItemStack[]{
                obsidian, obsidian, obsidian,
                obsidian, null, obsidian,
                null, null, null
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_CHESTPLATE)
                .leatherColor(Color.BLACK)
                .name(ChatColor.RESET + "Obsidian Chestplate")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 30, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST))
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "Toughness", 16, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST))
                .maxDurability(1500)
                .build(), "survival:obsidian_chestplate", new ShapedRecipe(new ItemStack[]{
                obsidian, null, obsidian,
                obsidian, obsidian, obsidian,
                obsidian, obsidian, obsidian
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_LEGGINGS)
                .leatherColor(Color.BLACK)
                .name(ChatColor.RESET + "Obsidian Leggings")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 16, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS))
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "Toughness", 12, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS))
                .maxDurability(750)
                .build(), "survival:obsidian_leggings", new ShapedRecipe(new ItemStack[]{
                obsidian, obsidian, obsidian,
                obsidian, null, obsidian,
                obsidian, null, obsidian
        }));
        new Item(new ItemStackBuilder(Material.LEATHER_BOOTS)
                .leatherColor(Color.BLACK)
                .name(ChatColor.RESET + "Obsidian Boots")
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET))
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "Toughness", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET))
                .maxDurability(580)
                .build(), "survival:obsidian_boots", new ShapedRecipe(new ItemStack[]{
                obsidian, null, obsidian,
                obsidian, null, obsidian,
                null, null, null
        }));
        ItemStack netherStar = new ItemStack(Material.NETHER_STAR);
        new Item(new ItemStackBuilder(Material.IRON_SWORD)
                .name(ChatColor.RESET + "Sword of the Nether Lord")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Ignites target.",
                        ChatColor.GRAY + "Right click to shoot fireball.",
                        ChatColor.GRAY + "Shooting fireball consumes 1 level."
                ))
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "Damage",
                        30, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND))
                .maxDurability(2000)
                .build(), "survival:star_thin_sword", new ShapedRecipe(new ItemStack[]{
                netherStar, null, null,
                netherStar, null, null,
                stick, null, null
        }));
        ItemStack ePearl = new ItemStack(Material.ENDER_PEARL);
        ItemStack eEye = new ItemStack(Material.ENDER_EYE);
        new Item(new ItemStackBuilder(Material.IRON_SWORD)
                .name(ChatColor.RESET + "Ender Sword")
                .lore(Arrays.asList(
                        ChatColor.GRAY + "Right click to teleport 8 blocks in front.",
                        ChatColor.GRAY + "Teleporting consumes 1 level."
                ))
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "Damage", 25, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND))
                .maxDurability(1750)
                .build(), "survival:ender_sword", new ShapedRecipe(new ItemStack[]{
                ePearl, null, null,
                eEye, null, null,
                stick, null, null
        }));
    }

    public static void load() {
        // Call static initializer
        new ItemRegistry();
    }

    @EventHandler
    public void reinforcedBow(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player && e.getEntity().getWorld().getName().toLowerCase().contains("survival")) {
            if ("survival:reinforced_bow".equals(Item.getId(e.getBow()))) {
                reinforcedBowArrows.add(e.getProjectile().getEntityId());
            }
        }
    }

    @EventHandler
    public void reinforcedBow2(EntityDamageByEntityEvent e) {
        if (reinforcedBowArrows.contains(e.getDamager().getEntityId())) {
            e.setDamage(e.getDamage() + 6);
            reinforcedBowArrows.remove(e.getDamager().getEntityId());
        }
    }

    @EventHandler
    public void reinforcedBow3(EntityRemoveFromWorldEvent e) {
        reinforcedBowArrows.remove(e.getEntity().getEntityId());
    }

    @EventHandler
    public void starSwordIgnite(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getDamager().getWorld().getName().toLowerCase().contains("survival")) {
            if ("survival:star_thin_sword".equals(Item.getId(((Player) e.getDamager()).getInventory().getItemInMainHand()))) {
                e.getEntity().setFireTicks(200);
            }
        }
    }

    @EventHandler
    public void starSwordShootFireball(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR && e.getPlayer().getWorld().getName().toLowerCase().contains("survival") &&
                "survival:star_thin_sword".equals(Item.getId(e.getPlayer().getInventory().getItemInMainHand()))) {
            if (e.getPlayer().getLevel() >= 1) {
                Vector direction = e.getPlayer().getEyeLocation().getDirection().multiply(2);
                Projectile projectile = e.getPlayer().getWorld().spawn(e.getPlayer().getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), Fireball.class);
                projectile.setShooter(e.getPlayer());
                projectile.setVelocity(direction);
                e.getPlayer().setLevel(e.getPlayer().getLevel() - 1);
            }
        }
    }

    @EventHandler
    public void enderSword(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getPlayer().getWorld().getName().toLowerCase().contains("survival") &&
                "survival:ender_sword".equals(Item.getId(e.getPlayer().getInventory().getItemInMainHand()))) {
            if (e.getPlayer().getLevel() >= 1) {
                Vector direction = e.getPlayer().getLocation().getDirection().normalize().multiply(8);
                Location loc = e.getPlayer().getLocation().clone();
                loc.add(direction);
                loc.setY(loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()));
                e.getPlayer().teleport(loc);
                e.getPlayer().setLevel(e.getPlayer().getLevel() - 1);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void bomb(BlockPlaceEvent e) {
        if (e.isCancelled()) return;
        if ("survival:bomb".equals(Item.getId(e.getItemInHand()))) {
            Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
                if (e.getBlock().getType() == Material.PLAYER_HEAD || e.getBlock().getType() == Material.PLAYER_WALL_HEAD) {
                    e.getBlock().setType(Material.AIR);
                    TNTPrimed tnt = (TNTPrimed) e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.PRIMED_TNT);
                    tnt.setFuseTicks(1);
                }
            }, 100L);
        }
    }

    @EventHandler
    public void onExperienceGet(PlayerPickupExperienceEvent e) {
        PlayerInventory inv = e.getPlayer().getInventory();
        int match = 0;
        for (ItemStack is : inv.getArmorContents()) {
            if (Item.getId(is) != null && Item.getId(is).matches("survival:lapis_(helmet|chestplate|leggings|boots)")) {
                match++;
            }
        }

        if (match == 4) {
            e.getExperienceOrb().setExperience(e.getExperienceOrb().getExperience() * 2);
        }
    }

    @EventHandler
    public void cactusArmorEffect(EntityDamageByEntityEvent e) {
        if (e.getEntity().getWorld().getName().toLowerCase().contains("survival") && e.getEntity() instanceof Player && e.getDamager() instanceof LivingEntity) {
            double toReflect = 0;
            for (ItemStack is : ((Player) e.getEntity()).getInventory().getArmorContents()) {
                if (Item.getId(is) != null && Item.getId(is).contains("cactus_")) {
                    toReflect += e.getFinalDamage() / 4D;
                }
            }
            if (toReflect != 0) {
                ((LivingEntity) e.getDamager()).damage(toReflect);
            }
        }
    }

    @EventHandler
    public void onEntityFall(EntityDamageEvent e) {
        if (e.getEntity().getWorld().getName().toLowerCase().contains("survival") && e.getEntity() instanceof Player) {
            PlayerInventory inv = ((Player) e.getEntity()).getInventory();
            if ("survival:dirt_helmet".equals(Item.getId(inv.getHelmet())) && "survival:dirt_chestplate".equals(Item.getId(inv.getChestplate())) &&
                    "survival:dirt_leggings".equals(Item.getId(inv.getLeggings())) && "survival:dirt_boots".equals(Item.getId(inv.getBoots()))) {
                if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void magicProofArmor(EntityDamageEvent e) {
        if (e.getEntity().getWorld().getName().toLowerCase().contains("survival") && e.getEntity() instanceof Player) {
            double magic = e.getDamage(EntityDamageEvent.DamageModifier.MAGIC);
            if (magic == 0) return;
            for (ItemStack is : (((Player) e.getEntity()).getInventory().getArmorContents())) {
                if (Item.getId(is) != null && Item.getId(is).contains("magic_proof")) {
                    magic = Math.round((magic - magic / 4) * 100) / 100D;
                }
            }
            e.setDamage(EntityDamageEvent.DamageModifier.MAGIC, magic);
        }
    }
}
