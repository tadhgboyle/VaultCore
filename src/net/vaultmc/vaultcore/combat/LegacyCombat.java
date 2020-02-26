package net.vaultmc.vaultcore.combat;

import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LegacyCombat extends ConstructorRegisterListener implements Runnable {
    private static final Map<Material, Double> axe = new HashMap<>();

    static {
        axe.put(Material.DIAMOND_AXE, 6D);
        axe.put(Material.IRON_AXE, 5D);
        axe.put(Material.GOLDEN_AXE, 3D);
        axe.put(Material.STONE_AXE, 4D);
        axe.put(Material.WOODEN_AXE, 3D);
    }

    public LegacyCombat() {
        Bukkit.getScheduler().runTaskTimer(VaultLoader.getInstance(), this, 200, 200);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerInventory inv = player.getInventory();
            for (int i = 0; i < inv.getContents().length; i++) {
                ItemStack stack = inv.getContents()[i];
                if (stack == null) continue;
                ItemMeta meta = stack.getItemMeta();
                if (stack.getType().toString().endsWith("AXE") && meta.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE) == null) {
                    meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                            UUID.randomUUID(), "Attack+", axe.get(stack.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                }
                stack.setItemMeta(meta);
            }
        }
    }
}
