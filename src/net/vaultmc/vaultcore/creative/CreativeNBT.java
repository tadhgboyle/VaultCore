package net.vaultmc.vaultcore.creative;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.collect.Iterables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.vaultmc.vaultloader.VaultLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;

public class CreativeNBT extends PacketAdapter {
    // Disable getting custom NBT items from creative inventory.
    // This couldn't be done in the vanilla client however is often used to create "Crash Book" etc.
    // SEE: Create book that contains custom NBT tag that aren't pages, author etc.

    public CreativeNBT() {
        super(VaultLoader.getInstance(), ListenerPriority.HIGHEST, PacketType.Play.Client.SET_CREATIVE_SLOT);
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    private static boolean isDurable(Material type) {
        return type.toString().matches(".*(AXE|SWORD|PICKAXE|SHOVEL|HOE|HELMET|CHESTPLATE|LEGGINGS|BOOTS|BOW)");
    }

    @Override
    public void onPacketReceiving(PacketEvent e) {
        PacketContainer packet = e.getPacket();
        if (e.getPacketType() == PacketType.Play.Client.SET_CREATIVE_SLOT) {
            ItemStack stack = CraftItemStack.asNMSCopy(packet.getItemModifier().read(0));
            if (stack.getBukkitStack().getType() == Material.AIR) return;
            if (stack.getBukkitStack().getType().toString().endsWith("POTION")) {
                CompoundTag tag = stack.getOrCreateTag();
                if (tag.contains("Detected")) return;
                if (tag.size() != 1 && !"Potion".equals(Iterables.getFirst(tag.getAllKeys(), "Default"))) {
                    Bukkit.getScheduler().runTask(VaultLoader.getInstance(),
                            () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban console1 " + e.getPlayer().getName()));
                    e.setCancelled(true);
                }
            } else if (stack.getBukkitStack().getType() == Material.ENCHANTED_BOOK) {
                CompoundTag tag = stack.getOrCreateTag();
                if (tag.contains("Detected")) return;
                if (tag.size() != 1 && !"StoredEnchantments".equals(Iterables.getFirst(tag.getAllKeys(), "Default"))) {
                    Bukkit.getScheduler().runTask(VaultLoader.getInstance(),
                            () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban console1 " + e.getPlayer().getName()));
                    e.setCancelled(true);
                }
            } else if (stack.getBukkitStack().getType().toString().endsWith("_SIGN") || stack.getBukkitStack().getType().toString().endsWith("CHEST") ||
                    stack.getBukkitStack().getType() == Material.BEEHIVE || stack.getBukkitStack().getType() == Material.BEE_NEST ||
                    stack.getBukkitStack().getType() == Material.DISPENSER || stack.getBukkitStack().getType() == Material.FURNACE ||
                    stack.getBukkitStack().getType() == Material.BREWING_STAND || stack.getBukkitStack().getType() == Material.HOPPER ||
                    stack.getBukkitStack().getType() == Material.DROPPER || stack.getBukkitStack().getType() == Material.BARREL ||
                    stack.getBukkitStack().getType() == Material.SMOKER || stack.getBukkitStack().getType() == Material.BLAST_FURNACE ||
                    stack.getBukkitStack().getType() == Material.CAMPFIRE || stack.getBukkitStack().getType() == Material.LECTERN ||
                    stack.getBukkitStack().getType() == Material.BEACON || stack.getBukkitStack().getType() == Material.SPAWNER ||
                    stack.getBukkitStack().getType() == Material.PLAYER_HEAD || stack.getBukkitStack().getType() == Material.STRUCTURE_BLOCK ||
                    stack.getBukkitStack().getType() == Material.JIGSAW || stack.getBukkitStack().getType() == Material.DAYLIGHT_DETECTOR ||
                    stack.getBukkitStack().getType().toString().endsWith("BED") || stack.getBukkitStack().getType() == Material.CAULDRON ||
                    stack.getBukkitStack().getType() == Material.CONDUIT || stack.getBukkitStack().getType() == Material.BELL ||
                    stack.getBukkitStack().getType() == Material.COMMAND_BLOCK || stack.getBukkitStack().getType().toString().endsWith("SHULKER_BOX") ||
                    stack.getBukkitStack().getType().toString().endsWith("BANNER")) {
                CompoundTag tag = stack.getOrCreateTag();
                if (tag.contains("Detected")) return;
                Tag display = null;
                if (tag.contains("display")) {
                    display = tag.get("display");
                    tag.remove("display");
                }
                if (tag.size() > 1 || !"BlockEntityTag".equals(Iterables.getFirst(tag.getAllKeys(), "BlockEntityTag"))) {
                    Bukkit.getScheduler().runTask(VaultLoader.getInstance(),
                            () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban console1 " + e.getPlayer().getName()));
                    e.setCancelled(true);
                }
                if (display != null)
                    tag.set("display", display);
            } else if (isDurable(stack.getBukkitStack().getType())) {
                CompoundTag tag = stack.getOrCreateTag();
                if (tag.contains("Detected")) return;
                if (tag.size() != 1 || !"Damage".equals(Iterables.getFirst(tag.getAllKeys(), "Default"))) {
                    Bukkit.getScheduler().runTask(VaultLoader.getInstance(),
                            () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban console1 " + e.getPlayer().getName()));
                    e.setCancelled(true);
                }
            } else {
                if (stack.hasTag()) {
                    if (stack.getOrCreateTag().contains("Detected")) return;
                    Bukkit.getScheduler().runTask(VaultLoader.getInstance(),
                            () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban console1 " + e.getPlayer().getName()));
                    e.setCancelled(true);
                }
            }
            CompoundTag tag = stack.getOrCreateTag();
            tag.set("Detected", StringTag.valueOf("ABC"));
            stack.setTag(tag);
            packet.getItemModifier().write(0, stack.getBukkitStack());
        }
    }
}
