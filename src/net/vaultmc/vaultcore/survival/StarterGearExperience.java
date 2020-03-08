package net.vaultmc.vaultcore.survival;

import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class StarterGearExperience extends ConstructorRegisterListener {
    private static final ItemStack[] starterGear = {
            new ItemStack(Material.IRON_SWORD),
            new ItemStack(Material.BOW),
            new ItemStack(Material.STONE_AXE),
            new ItemStack(Material.STONE_PICKAXE),
            new ItemStack(Material.OAK_LOG, 16),
            new ItemStack(Material.IRON_HELMET),
            new ItemStack(Material.IRON_CHESTPLATE),
            new ItemStack(Material.IRON_LEGGINGS),
            new ItemStack(Material.IRON_BOOTS),
            new ItemStack(Arrays.stream(Material.values()).filter(m -> {  // Use hack to get hidden material
                try {
                    Field field = Material.class.getDeclaredField("id");
                    field.setAccessible(true);
                    int id = (int) field.get(m);
                    field.setAccessible(false);
                    return id == 20865;
                } catch (NoSuchFieldException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
                return false;
            }).collect(Collectors.toList()).get(0), 15)
    };

    @EventHandler
    public void svStarterGear(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().contains("survival")) {
            VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
            if (!player.getPlayerData().contains("svstartergear")) {
                player.getInventory().addItem(starterGear);
                player.getPlayerData().set("svstartergear", true);
            }
        }
    }
}
