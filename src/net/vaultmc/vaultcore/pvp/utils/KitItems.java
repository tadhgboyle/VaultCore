package net.vaultmc.vaultcore.pvp.utils;

import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.Map;

public class KitItems {
    public static void clear(VLPlayer p) {
        for (PotionEffectType type : PotionEffectType.values()) {
            p.getPlayer().removePotionEffect(type);
        }
    }

    private static void use(VLPlayer p, String kit, long time) {
        Map<String, Long> l = KitGuis.delays.containsKey(p) ? KitGuis.delays.get(p) : new HashMap<>();
        l.put(kit, System.currentTimeMillis() + time);
        KitGuis.delays.put(p, l);
    }

    public static void getKitSwordsman(VLPlayer p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();
        clear(p);
        inv.addItem(new ItemStackBuilder(Material.DIAMOND_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 3)
                .unbreakable(true)
                .build());
        inv.addItem(new ItemStack(Material.GOLDEN_APPLE, 5));
        inv.addItem(new ItemStack(Material.COOKED_BEEF, 16));

        ItemStack[] armor = new ItemStack[4];
        armor[0] = new ItemStackBuilder(Material.IRON_BOOTS)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .build();
        armor[1] = new ItemStackBuilder(Material.IRON_LEGGINGS)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .build();
        armor[2] = new ItemStackBuilder(Material.IRON_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .unbreakable(true)
                .build();
        armor[3] = new ItemStackBuilder(Material.IRON_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .unbreakable(true)
                .build();
        inv.setArmorContents(armor);
    }

    public static void getKitAxeman(VLPlayer p) {
        PlayerInventory inv = p.getInventory();
        if (!KitGuis.canUse(p, "axeman")) {
            p.sendMessage(ChatColor.RED + "You can't use this kit yet!");
            return;
        }
        use(p, "axeman", 60000);
        inv.clear();
        clear(p);
        inv.addItem(new ItemStackBuilder(Material.IRON_AXE)
                .enchant(Enchantment.DAMAGE_ALL, 3)
                .unbreakable(true)
                .build());
        inv.addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1));
        inv.addItem(new ItemStack(Material.COOKED_BEEF, 16));

        ItemStack[] armor = new ItemStack[4];
        armor[0] = new ItemStackBuilder(Material.GOLDEN_BOOTS)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .build();
        armor[1] = new ItemStackBuilder(Material.GOLDEN_LEGGINGS)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .build();
        armor[2] = new ItemStackBuilder(Material.GOLDEN_CHESTPLATE)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .build();
        armor[3] = new ItemStackBuilder(Material.GOLDEN_HELMET)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .build();
        inv.setArmorContents(armor);
    }

    public static void getKitMage(VLPlayer p) {
        PlayerInventory inv = p.getInventory();
        if (!KitGuis.canUse(p, "mage")) {
            p.sendMessage(ChatColor.RED + "You can't use this kit yet!");
            return;
        }
        use(p, "mage", 60000);
        inv.clear();
        clear(p);
        inv.addItem(new ItemStackBuilder(Material.IRON_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 1)
                .unbreakable(true)
                .build());
        inv.addItem(new ItemStackBuilder(Material.BOW)
                .unbreakable(true)
                .build());
        inv.addItem(new ItemStack(Material.ARROW, 32));
        inv.addItem(new ItemStackBuilder(Material.POTION)
                .potion(new PotionData(PotionType.REGEN, false, false), Color.PURPLE)
                .build());
        inv.addItem(new ItemStackBuilder(Material.POTION)
                .potion(new PotionData(PotionType.INSTANT_HEAL, false, false), Color.RED)
                .build());
        inv.addItem(new ItemStackBuilder(Material.POTION)
                .potion(new PotionData(PotionType.STRENGTH, false, false), Color.MAROON)
                .build());
        inv.addItem(new ItemStackBuilder(Material.POTION)
                .potion(new PotionData(PotionType.SPEED, false, false), Color.BLUE)
                .build());
        inv.addItem(new ItemStackBuilder(Material.SPLASH_POTION)
                .amount(2)
                .potion(new PotionData(PotionType.INSTANT_DAMAGE, false, false), Color.MAROON)
                .build());

        ItemStack[] armor = new ItemStack[4];
        armor[0] = new ItemStackBuilder(Material.IRON_BOOTS)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .build();
        armor[1] = new ItemStackBuilder(Material.IRON_LEGGINGS)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .build();
        armor[2] = new ItemStackBuilder(Material.IRON_CHESTPLATE)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .build();
        armor[3] = new ItemStackBuilder(Material.IRON_HELMET)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .build();
        inv.setArmorContents(armor);
    }

    public static void getKitArcher(VLPlayer p) {
        PlayerInventory inv = p.getInventory();
        if (!KitGuis.canUse(p, "archer")) {
            p.sendMessage(ChatColor.RED + "You can't use this kit yet!");
            return;
        }
        use(p, "archer", 60000);
        inv.clear();
        clear(p);
        inv.addItem(new ItemStackBuilder(Material.BOW)
                .enchant(Enchantment.ARROW_DAMAGE, 1)
                .enchant(Enchantment.ARROW_INFINITE, 1)
                .unbreakable(true)
                .build());
        inv.addItem(new ItemStackBuilder(Material.STONE_SWORD)
                .unbreakable(true)
                .build());
        inv.addItem(new ItemStack(Material.COOKED_BEEF, 16));
        inv.addItem(new ItemStack(Material.ARROW));

        ItemStack[] armor = new ItemStack[4];
        armor[0] = new ItemStackBuilder(Material.IRON_BOOTS)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .build();
        armor[1] = new ItemStackBuilder(Material.IRON_LEGGINGS)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .build();
        armor[2] = new ItemStackBuilder(Material.IRON_CHESTPLATE)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .build();
        armor[3] = new ItemStackBuilder(Material.IRON_HELMET)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .build();
        inv.setArmorContents(armor);

    }

    public static void getKitHealer(VLPlayer p) {
        PlayerInventory inv = p.getInventory();
        if (!KitGuis.canUse(p, "healer")) {
            p.sendMessage(ChatColor.RED + "You can't use this kit yet!");
            return;
        }
        use(p, "healer", 60000);
        inv.clear();
        clear(p);
        inv.addItem(new ItemStackBuilder(Material.STONE_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 2)
                .unbreakable(true)
                .build());
        inv.addItem(new ItemStackBuilder(Material.SPLASH_POTION)
                .potion(new PotionData(PotionType.REGEN, true, false), Color.PURPLE)
                .build());
        inv.addItem(new ItemStackBuilder(Material.POTION)
                .potion(new PotionData(PotionType.INSTANT_HEAL, false, false), Color.RED)
                .build());
        inv.addItem(new ItemStackBuilder(Material.POTION)
                .potion(new PotionData(PotionType.STRENGTH, false, false), Color.MAROON)
                .build());
        inv.addItem(new ItemStackBuilder(Material.POTION)
                .potion(new PotionData(PotionType.SPEED, false, false), Color.BLUE)
                .build());
        inv.addItem(new ItemStack(Material.GOLDEN_APPLE));
        inv.addItem(new ItemStack(Material.COOKED_BEEF, 16));

        ItemStack[] armor = new ItemStack[4];
        armor[0] = new ItemStackBuilder(Material.IRON_BOOTS)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .build();
        armor[1] = new ItemStackBuilder(Material.IRON_LEGGINGS)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .build();
        armor[2] = new ItemStackBuilder(Material.IRON_CHESTPLATE)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .build();
        armor[3] = new ItemStackBuilder(Material.IRON_HELMET)
                .unbreakable(true)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .build();
        inv.setArmorContents(armor);

    }

    public static void getKitArmorer(VLPlayer p) {
        PlayerInventory inv = p.getInventory();
        if (!KitGuis.canUse(p, "armorer")) {
            p.sendMessage(ChatColor.RED + "You can't use this kit yet!");
            return;
        }
        use(p, "armorer", 3600000);
        inv.clear();
        clear(p);
        inv.addItem(new ItemStackBuilder(Material.IRON_AXE)
                .enchant(Enchantment.DAMAGE_ALL, 2)
                .unbreakable(true)
                .build());
        inv.addItem(new ItemStackBuilder(Material.BOW)
                .enchant(Enchantment.ARROW_DAMAGE, 3)
                .unbreakable(true)
                .build());
        inv.addItem(new ItemStack(Material.GOLDEN_APPLE, 3));
        inv.addItem(new ItemStack(Material.COOKED_BEEF, 16));
        inv.addItem(new ItemStack(Material.ARROW, 32));

        ItemStack[] armor = new ItemStack[4];
        armor[0] = new ItemStackBuilder(Material.DIAMOND_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .unbreakable(true)
                .build();
        armor[1] = new ItemStackBuilder(Material.DIAMOND_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .unbreakable(true)
                .build();
        armor[2] = new ItemStackBuilder(Material.DIAMOND_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .unbreakable(true)
                .build();
        armor[3] = new ItemStackBuilder(Material.DIAMOND_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .unbreakable(true)
                .build();
        inv.setArmorContents(armor);

    }

    public static void getKitWarlord(VLPlayer p) {
        PlayerInventory inv = p.getInventory();
        if (!KitGuis.canUse(p, "warlord")) {
            p.sendMessage(ChatColor.RED + "You can't use this kit yet!");
            return;
        }
        use(p, "warlord", 5400000);
        inv.clear();
        clear(p);
        inv.addItem(new ItemStackBuilder(Material.DIAMOND_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 4)
                .unbreakable(true)
                .build());
        inv.addItem(new ItemStackBuilder(Material.BOW)
                .enchant(Enchantment.ARROW_DAMAGE, 4)
                .enchant(Enchantment.ARROW_FIRE, 1)
                .unbreakable(true)
                .build());
        inv.addItem(new ItemStack(Material.GOLDEN_APPLE, 10));
        inv.addItem(new ItemStack(Material.COOKED_BEEF, 16));
        inv.addItem(new ItemStack(Material.ARROW, 64));

        ItemStack[] armor = new ItemStack[4];
        armor[0] = new ItemStackBuilder(Material.DIAMOND_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .unbreakable(true)
                .build();
        armor[1] = new ItemStackBuilder(Material.DIAMOND_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .unbreakable(true)
                .build();
        armor[2] = new ItemStackBuilder(Material.DIAMOND_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .unbreakable(true)
                .build();
        armor[3] = new ItemStackBuilder(Material.DIAMOND_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .unbreakable(true)
                .build();
        inv.setArmorContents(armor);

    }

    public static void getKitOverlord(VLPlayer p) {
        PlayerInventory inv = p.getInventory();
        if (!KitGuis.canUse(p, "overlord")) {
            p.sendMessage(ChatColor.RED + "You can't use this kit yet!");
            return;
        }
        use(p, "overlord", 7200000);
        inv.clear();
        clear(p);
        inv.addItem(new ItemStackBuilder(Material.DIAMOND_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 8)
                .enchant(Enchantment.FIRE_ASPECT, 3)
                .unbreakable(true)
                .build());
        inv.addItem(new ItemStackBuilder(Material.BOW)
                .enchant(Enchantment.ARROW_DAMAGE, 4)
                .enchant(Enchantment.ARROW_FIRE, 1)
                .enchant(Enchantment.ARROW_INFINITE, 1)
                .unbreakable(true)
                .build());
        inv.addItem(new ItemStack(Material.GOLDEN_APPLE, 16));
        inv.addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 4));
        inv.addItem(new ItemStack(Material.COOKED_BEEF, 16));
        inv.addItem(new ItemStack(Material.ARROW));

        ItemStack[] armor = new ItemStack[4];
        armor[0] = new ItemStackBuilder(Material.DIAMOND_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 7)
                .unbreakable(true)
                .build();
        armor[1] = new ItemStackBuilder(Material.DIAMOND_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 7)
                .unbreakable(true)
                .build();
        armor[2] = new ItemStackBuilder(Material.DIAMOND_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 7)
                .unbreakable(true)
                .build();
        armor[3] = new ItemStackBuilder(Material.DIAMOND_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 7)
                .unbreakable(true)
                .build();
        inv.setArmorContents(armor);
    }
}
