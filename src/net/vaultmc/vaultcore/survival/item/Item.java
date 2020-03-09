package net.vaultmc.vaultcore.survival.item;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private static final Map<String, Item> items = new HashMap<>();
    @Getter
    private ItemStack item;
    @Getter
    private String id;

    public Item(ItemStack item, String id) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(item);
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        CompoundTag tag = nmsItem.getOrCreateTag();
        tag.set("Survival.ItemKey", StringTag.valueOf(id));
        nmsItem.setTag(tag);
        this.item = CraftItemStack.asBukkitCopy(nmsItem);
        this.id = id;
        items.put(id, this);
    }

    public static Item getBy(ItemStack item) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        CompoundTag tag = nmsItem.getOrCreateTag();
        if (tag.contains("Survival.ItemKey")) {
            return getBy(tag.getString("Survival.ItemKey"));
        }
        return null;
    }

    public static Item getBy(String id) {
        return items.get(id);
    }

    public static String getId(ItemStack item) {
        return getBy(item) != null ? getBy(item).getId() : null;
    }
}
