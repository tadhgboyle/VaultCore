/*
 * VaultUtils: VaultMC functionalities provider.
 * Copyright (C) 2020 yangyang200
 *
 * VaultUtils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VaultUtils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VaultCore.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.vaultmc.vaultcore.ported.inventory;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryStorageListeners extends ConstructorRegisterListener {
    public static final Map<String, String[]> worldGroups = new HashMap<>();

    static {
        worldGroups.put("lobby", new String[]{"Lobby", "newSpawn"});
        worldGroups.put("survival", new String[]{"Survival", "Survival_Nether", "Survival_End"});
        worldGroups.put("skyblock", new String[]{"skyblock", "skyblock_nether"});
        worldGroups.put("clans", new String[]{"clans", "clans_nether", "clans_the_end"});
        worldGroups.put("creative", new String[]{"creative"});
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
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        if (!getGroupOf(e.getFrom().getName()).equals(getGroupOf(e.getPlayer().getWorld().getName()))) {
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
                                PotionEffectType.getByName(data.getString(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".type")),
                                data.getInt(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".duration"),
                                data.getInt(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".amplifier"),
                                data.getBoolean(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".ambient"),
                                data.getBoolean(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".particles"),
                                data.getBoolean(e.getPlayer().getUniqueId().toString() + "." + from + ".potion." + tag + ".icon")
                        ));
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }

            VaultCore.getInstance().saveConfig();
        }
    }
}

