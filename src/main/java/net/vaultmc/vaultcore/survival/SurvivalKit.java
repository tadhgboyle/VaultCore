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

package net.vaultmc.vaultcore.survival;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ds.ConfigurationSerializable;
import net.vaultmc.vaultloader.utils.ds.SerializableField;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationSerializable
@AllArgsConstructor
@NoArgsConstructor
public class SurvivalKit {
    @Getter
    private static final Map<String, SurvivalKit> kits = new HashMap<>();

    static {
        for (SurvivalKit kit : VaultCore.getInstance().getKits().deserializeList("kits", SurvivalKit.class)) {
            kits.put(kit.name.toLowerCase(), kit);
        }
    }

    @SerializableField
    public String name;
    @SerializableField
    public String permission;
    @SerializableField
    public List<ItemStack> items;
    @SerializableField
    public long delay;

    public static void save() {
        VaultCore.getInstance().getKits().serializeList("kits", new ArrayList<>(kits.values()));
    }
}
