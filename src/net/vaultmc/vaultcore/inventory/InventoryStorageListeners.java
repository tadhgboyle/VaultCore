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

package net.vaultmc.vaultcore.inventory;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryStorageListeners extends ConstructorRegisterListener {
    public static final Map<String, String[]> worldGroups = new HashMap<>();

    static {
        worldGroups.put("lobby", new String[]{"Lobby", "newSpawn", "tour", "legacy_lobby"});
        worldGroups.put("survival", new String[]{"Survival", "Survival_Nether", "Survival_the_end", "legacy_survival"});
        worldGroups.put("skyblock", new String[]{"skyblock", "skyblock_nether", "legacy_sb"});
        worldGroups.put("clans", new String[]{"clans"});
        worldGroups.put("creative", new String[]{"creative", "legacy_creative"});
        worldGroups.put("rpg", new String[]{"throwback", "throwback2"});
        worldGroups.put("pvp", new String[]{"pvp"});
        worldGroups.put("kingdoms", new String[]{"kingdoms"});
        worldGroups.put("build", new String[]{"build"});
    }

    public static String getGroupOf(String world) {
        for (Map.Entry<String, String[]> entry : worldGroups.entrySet()) {
            for (String s : entry.getValue()) {
                if (s.equalsIgnoreCase(world))
                    return entry.getKey();
            }
        }
        return null;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if ("vaultmc".equalsIgnoreCase(VaultCore.getInstance().getConfig().getString("server"))) {
            String to = getGroupOf(e.getPlayer().getWorld().getName());
            InventoryStorageUtils.restorePlayerInventory(e.getPlayer().getInventory(),
                    e.getPlayer().getUniqueId().toString() + "." + to + ".inventory");
            InventoryStorageUtils.restoreChest(e.getPlayer().getEnderChest(),
                    e.getPlayer().getUniqueId().toString() + "." + to + ".enderchest");
            FileConfiguration data = VaultCore.getInstance().getInventoryData();
            e.getPlayer().setHealth(data.getDouble(e.getPlayer().getUniqueId().toString() + "." + to + ".health",
                    e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
            e.getPlayer().setSaturation((float) data.getDouble(e.getPlayer().getUniqueId().toString() + "." + to + ".saturation", 20));
            e.getPlayer().setLevel(data.getInt(e.getPlayer().getUniqueId().toString() + "." + to + ".level", 0));
            e.getPlayer().setExp((float) data.getDouble(e.getPlayer().getUniqueId().toString() + "." + to + ".exp", 0));
            e.getPlayer().setFoodLevel(data.getInt(e.getPlayer().getUniqueId().toString() + "." + to + ".foodlevel",
                    20));
            e.getPlayer().setBedSpawnLocation(data.getLocation(e.getPlayer().getUniqueId().toString() + "." + to + ".spawn"), true);
            e.getPlayer().getActivePotionEffects().clear();

            if (data.contains(e.getPlayer().getUniqueId().toString() + "." + to + ".potion")) {
                for (String tag : data.getConfigurationSection(e.getPlayer().getUniqueId().toString() + "." + to + ".potion").getKeys(false)) {
                    try {
                        e.getPlayer().addPotionEffect(new PotionEffect(
                                PotionEffectType.getByName(data.getString(e.getPlayer().getUniqueId().toString() + "." + to + ".potion." + tag + ".type")),
                                data.getInt(e.getPlayer().getUniqueId().toString() + "." + to + ".potion." + tag + ".duration"),
                                data.getInt(e.getPlayer().getUniqueId().toString() + "." + to + ".potion." + tag + ".amplifier"),
                                data.getBoolean(e.getPlayer().getUniqueId().toString() + "." + to + ".potion." + tag + ".ambient"),
                                data.getBoolean(e.getPlayer().getUniqueId().toString() + "." + to + ".potion." + tag + ".particles"),
                                data.getBoolean(e.getPlayer().getUniqueId().toString() + "." + to + ".potion." + tag + ".icon")
                        ));
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            } else {
                for (PotionEffectType type : PotionEffectType.values()) {
                    e.getPlayer().removePotionEffect(type);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        String from = getGroupOf(e.getPlayer().getWorld().getName());
        InventoryStorageUtils.storePlayerInventory(e.getPlayer().getInventory(),
                e.getPlayer().getUniqueId().toString() + "." + from + ".inventory");
        InventoryStorageUtils.storeChest(e.getPlayer().getEnderChest(),
                e.getPlayer().getUniqueId().toString() + "." + from + ".enderchest");
        FileConfiguration data = VaultCore.getInstance().getInventoryData();
        data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".health", e.getPlayer().getHealth());
        data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".saturation", e.getPlayer().getSaturation());
        data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".level", e.getPlayer().getLevel());
        data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".exp", e.getPlayer().getExp());
        data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".foodlevel", e.getPlayer().getFoodLevel());
        data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".spawn", e.getPlayer().getBedSpawnLocation());
        data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".potion", null);
        for (PotionEffect effect : e.getPlayer().getActivePotionEffects()) {
            String tag = UUID.randomUUID().toString();
            data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".type", effect.getType().toString());
            data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".amplifier", effect.getAmplifier());
            data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".duration", effect.getDuration());
            data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".particles", effect.hasParticles());
            data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".ambient", effect.isAmbient());
            data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".icon", effect.hasIcon());
        }
        e.getPlayer().getActivePotionEffects().clear();
        VaultCore.getInstance().saveConfig();
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        if (VaultCore.getInstance().getConfig().getString("server").equalsIgnoreCase("vaultmc") &&
                !getGroupOf(e.getFrom().getName()).equals(getGroupOf(e.getPlayer().getWorld().getName()))) {
            String from = getGroupOf(e.getFrom().getName());
            String to = getGroupOf(e.getPlayer().getWorld().getName());
            InventoryStorageUtils.storePlayerInventory(e.getPlayer().getInventory(),
                    e.getPlayer().getUniqueId().toString() + "." + from + ".inventory");
            InventoryStorageUtils.restorePlayerInventory(e.getPlayer().getInventory(),
                    e.getPlayer().getUniqueId().toString() + "." + to + ".inventory");
            InventoryStorageUtils.storeChest(e.getPlayer().getEnderChest(),
                    e.getPlayer().getUniqueId().toString() + "." + from + ".enderchest");
            InventoryStorageUtils.restoreChest(e.getPlayer().getEnderChest(),
                    e.getPlayer().getUniqueId().toString() + "." + to + ".enderchest");
            FileConfiguration data = VaultCore.getInstance().getInventoryData();
            data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".health", e.getPlayer().getHealth());
            e.getPlayer().setHealth(data.getDouble(e.getPlayer().getUniqueId().toString() + "." + to + ".health",
                    e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
            data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".saturation", e.getPlayer().getSaturation());
            e.getPlayer().setSaturation((float) data.getDouble(e.getPlayer().getUniqueId().toString() + "." + to + ".saturation", 20));
            data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".level", e.getPlayer().getLevel());
            e.getPlayer().setLevel(data.getInt(e.getPlayer().getUniqueId().toString() + "." + to + ".level", 0));
            data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".exp", e.getPlayer().getExp());
            e.getPlayer().setExp((float) data.getDouble(e.getPlayer().getUniqueId().toString() + "." + to + ".exp", 0));
            data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".foodlevel", e.getPlayer().getFoodLevel());
            e.getPlayer().setFoodLevel(data.getInt(e.getPlayer().getUniqueId().toString() + "." + to + ".foodlevel",
                    20));
            data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".spawn", e.getPlayer().getBedSpawnLocation());
            e.getPlayer().setBedSpawnLocation(data.getLocation(e.getPlayer().getUniqueId().toString() + "." + to + ".spawn"), true);
            data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".potion", null);
            for (PotionEffect effect : e.getPlayer().getActivePotionEffects()) {
                String tag = UUID.randomUUID().toString();
                data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".type", effect.getType().toString());
                data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".amplifier", effect.getAmplifier());
                data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".duration", effect.getDuration());
                data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".particles", effect.hasParticles());
                data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".ambient", effect.isAmbient());
                data.set(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".icon", effect.hasIcon());
            }

            e.getPlayer().getActivePotionEffects().clear();

            if (data.contains(e.getPlayer().getUniqueId().toString() + "." + to + ".potion")) {
                for (String tag : data.getConfigurationSection(e.getPlayer().getUniqueId().toString() + "." + to + ".potion").getKeys(false)) {
                    try {
                        e.getPlayer().addPotionEffect(new PotionEffect(
                                PotionEffectType.getByName(data.getString(e.getPlayer().getUniqueId().toString() + "." + to + ".potion." + tag + ".type")),
                                data.getInt(e.getPlayer().getUniqueId().toString() + "." + to + ".potion." + tag + ".duration"),
                                data.getInt(e.getPlayer().getUniqueId().toString() + "." + to + ".potion." + tag + ".amplifier"),
                                data.getBoolean(e.getPlayer().getUniqueId().toString() + "." + to + ".potion." + tag + ".ambient"),
                                data.getBoolean(e.getPlayer().getUniqueId().toString() + "." + to + ".potion." + tag + ".particles"),
                                data.getBoolean(e.getPlayer().getUniqueId().toString() + "." + to + ".potion." + tag + ".icon")
                        ));
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            } else {
                for (PotionEffectType type : PotionEffectType.values()) {
                    e.getPlayer().removePotionEffect(type);
                }
            }
            VaultCore.getInstance().saveConfig();
        }
    }
}

