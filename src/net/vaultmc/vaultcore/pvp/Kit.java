package net.vaultmc.vaultcore.pvp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ds.ConfigurationSerializable;
import net.vaultmc.vaultloader.utils.ds.SerializableField;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@ConfigurationSerializable
@AllArgsConstructor
@NoArgsConstructor
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
