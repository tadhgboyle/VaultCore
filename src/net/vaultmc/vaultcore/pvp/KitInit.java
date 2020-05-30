/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

package net.vaultmc.vaultcore.pvp;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.Arrays;

public class KitInit {
    public static void init() {
        if (!Kit.getKits().isEmpty()) return;
        Kit.getKits().add(new Kit(
                "Swordsman",
                Arrays.asList(
                        new ItemStackBuilder(Material.DIAMOND_SWORD)
                                .enchant(Enchantment.DAMAGE_ALL, 3)
                                .unbreakable(true)
                                .build(),
                        new ItemStack(Material.GOLDEN_APPLE, 5),
                        new ItemStack(Material.COOKED_BEEF, 16),
                        new ItemStackBuilder(Material.IRON_HELMET)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .unbreakable(true)
                                .build(),
                        new ItemStackBuilder(Material.IRON_CHESTPLATE)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .unbreakable(true)
                                .build(),
                        new ItemStackBuilder(Material.IRON_LEGGINGS)
                                .unbreakable(true)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .build(),
                        new ItemStackBuilder(Material.IRON_BOOTS)
                                .unbreakable(true)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .build()
                ),
                0, 0
        ));
        Kit.getKits().add(new Kit(
                "Axeman",
                Arrays.asList(
                        new ItemStackBuilder(Material.IRON_AXE)
                                .enchant(Enchantment.DAMAGE_ALL, 3)
                                .unbreakable(true)
                                .build(),
                        new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1),
                        new ItemStack(Material.COOKED_BEEF, 16),
                        new ItemStackBuilder(Material.GOLDEN_HELMET)
                                .unbreakable(true)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .build(),
                        new ItemStackBuilder(Material.GOLDEN_CHESTPLATE)
                                .unbreakable(true)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .build(),
                        new ItemStackBuilder(Material.GOLDEN_LEGGINGS)
                                .unbreakable(true)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .build(),
                        new ItemStackBuilder(Material.GOLDEN_BOOTS)
                                .unbreakable(true)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .build()
                ),
                60,
                75
        ));
        Kit.getKits().add(new Kit(
                "Archer",
                Arrays.asList(
                        new ItemStackBuilder(Material.BOW)
                                .enchant(Enchantment.ARROW_DAMAGE, 1)
                                .enchant(Enchantment.ARROW_INFINITE, 1)
                                .unbreakable(true)
                                .build(),
                        new ItemStackBuilder(Material.STONE_SWORD)
                                .unbreakable(true)
                                .build(),
                        new ItemStack(Material.COOKED_BEEF, 16),
                        new ItemStack(Material.ARROW),
                        new ItemStackBuilder(Material.IRON_HELMET)
                                .unbreakable(true)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                                .build(),
                        new ItemStackBuilder(Material.IRON_CHESTPLATE)
                                .unbreakable(true)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                                .build(),
                        new ItemStackBuilder(Material.IRON_LEGGINGS)
                                .unbreakable(true)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                                .build(),
                        new ItemStackBuilder(Material.IRON_BOOTS)
                                .unbreakable(true)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                                .build()
                ),
                60,
                120
        ));
        Kit.getKits().add(new Kit(
                "Healer",
                Arrays.asList(
                        new ItemStackBuilder(Material.STONE_SWORD)
                                .enchant(Enchantment.DAMAGE_ALL, 2)
                                .unbreakable(true)
                                .build(),
                        new ItemStackBuilder(Material.SPLASH_POTION)
                                .potion(new PotionData(PotionType.REGEN, true, false), Color.PURPLE)
                                .build(),
                        new ItemStackBuilder(Material.POTION)
                                .potion(new PotionData(PotionType.INSTANT_HEAL, false, false), Color.RED)
                                .build(),
                        new ItemStackBuilder(Material.POTION)
                                .potion(new PotionData(PotionType.STRENGTH, false, false), Color.MAROON)
                                .build(),
                        new ItemStackBuilder(Material.POTION)
                                .potion(new PotionData(PotionType.SPEED, false, false), Color.BLUE)
                                .build(),
                        new ItemStack(Material.GOLDEN_APPLE),
                        new ItemStack(Material.COOKED_BEEF, 16),
                        new ItemStackBuilder(Material.IRON_HELMET)
                                .unbreakable(true)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                                .build(),
                        new ItemStackBuilder(Material.IRON_CHESTPLATE)
                                .unbreakable(true)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                                .build(),
                        new ItemStackBuilder(Material.IRON_LEGGINGS)
                                .unbreakable(true)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                                .build(),
                        new ItemStackBuilder(Material.IRON_BOOTS)
                                .unbreakable(true)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                                .build()
                ),
                60,
                350
        ));
        Kit.getKits().add(new Kit(
                "Armorer",
                Arrays.asList(
                        new ItemStackBuilder(Material.IRON_AXE)
                                .enchant(Enchantment.DAMAGE_ALL, 2)
                                .unbreakable(true)
                                .build(),
                        new ItemStackBuilder(Material.BOW)
                                .enchant(Enchantment.ARROW_DAMAGE, 3)
                                .unbreakable(true)
                                .build(),
                        new ItemStack(Material.GOLDEN_APPLE, 3),
                        new ItemStack(Material.COOKED_BEEF, 16),
                        new ItemStack(Material.ARROW, 32),
                        new ItemStackBuilder(Material.DIAMOND_HELMET)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                                .unbreakable(true)
                                .build(),
                        new ItemStackBuilder(Material.DIAMOND_CHESTPLATE)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                                .unbreakable(true)
                                .build(),
                        new ItemStackBuilder(Material.DIAMOND_LEGGINGS)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                                .unbreakable(true)
                                .build(),
                        new ItemStackBuilder(Material.DIAMOND_BOOTS)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                                .unbreakable(true)
                                .build()
                ),
                3600,
                400
        ));
        Kit.getKits().add(new Kit(
                "Warlord",
                Arrays.asList(
                        new ItemStackBuilder(Material.DIAMOND_SWORD)
                                .enchant(Enchantment.DAMAGE_ALL, 4)
                                .unbreakable(true)
                                .build(),
                        new ItemStackBuilder(Material.BOW)
                                .enchant(Enchantment.ARROW_DAMAGE, 4)
                                .enchant(Enchantment.ARROW_FIRE, 1)
                                .unbreakable(true)
                                .build(),
                        new ItemStack(Material.GOLDEN_APPLE, 10),
                        new ItemStack(Material.COOKED_BEEF, 16),
                        new ItemStack(Material.ARROW, 64),
                        new ItemStackBuilder(Material.DIAMOND_HELMET)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                                .unbreakable(true)
                                .build(),
                        new ItemStackBuilder(Material.DIAMOND_CHESTPLATE)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                                .unbreakable(true)
                                .build(),
                        new ItemStackBuilder(Material.DIAMOND_LEGGINGS)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                                .unbreakable(true)
                                .build(),
                        new ItemStackBuilder(Material.DIAMOND_BOOTS)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                                .unbreakable(true)
                                .build()
                ),
                5400,
                750
        ));
        Kit.getKits().add(new Kit(
                "Overlord",
                Arrays.asList(
                        new ItemStackBuilder(Material.DIAMOND_SWORD)
                                .enchant(Enchantment.DAMAGE_ALL, 8)
                                .enchant(Enchantment.FIRE_ASPECT, 3)
                                .unbreakable(true)
                                .build(),
                        new ItemStackBuilder(Material.BOW)
                                .enchant(Enchantment.ARROW_DAMAGE, 4)
                                .enchant(Enchantment.ARROW_FIRE, 1)
                                .enchant(Enchantment.ARROW_INFINITE, 1)
                                .unbreakable(true)
                                .build(),
                        new ItemStack(Material.GOLDEN_APPLE, 16),
                        new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 4),
                        new ItemStack(Material.COOKED_BEEF, 16),
                        new ItemStack(Material.ARROW),
                        new ItemStackBuilder(Material.DIAMOND_HELMET)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 7)
                                .unbreakable(true)
                                .build(),
                        new ItemStackBuilder(Material.DIAMOND_CHESTPLATE)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 7)
                                .unbreakable(true)
                                .build(),
                        new ItemStackBuilder(Material.DIAMOND_LEGGINGS)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 7)
                                .unbreakable(true)
                                .build(),
                        new ItemStackBuilder(Material.DIAMOND_BOOTS)
                                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 7)
                                .unbreakable(true)
                                .build()
                ),
                7200,
                1100
        ));
        VaultCore.getInstance().getVLData().serializeList("kits", Kit.getKits());
    }
}
