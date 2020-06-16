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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ds.ConfigurationSerializable;
import net.vaultmc.vaultloader.utils.ds.SerializableField;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@ConfigurationSerializable
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Kit {
    // Order is important as that determines the order shown in the GUI.
    @Getter
    private static final List<Kit> kits;

    static {
        kits = VaultCore.getInstance().getVLData().deserializeList("kits", Kit.class);
    }

    @SerializableField
    public String name;
    @SerializableField
    public List<ItemStack> items;
    @SerializableField
    public long delay;  // Seconds
    @SerializableField
    public double price;
}
