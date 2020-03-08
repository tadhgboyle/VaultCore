package net.vaultmc.vaultcore.creative;

import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityUpperBound extends ConstructorRegisterListener {
    private static final Map<UUID, Integer> entities = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase("Creative") && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getItem() != null &&
                (e.getItem().getType().toString().endsWith("_SPAWN_EGG") || e.getItem().getType() == Material.ARMOR_STAND ||
                        e.getItem().getType() == Material.ITEM_FRAME || e.getItem().getType().toString().endsWith("MINECART") ||
                        e.getItem().getType().toString().endsWith("BOAT") || (e.getItem().getType() != Material.LAVA_BUCKET &&
                        e.getItem().getType() != Material.WATER_BUCKET && e.getItem().getType().toString().endsWith("BUCKET")))) {
            int count = entities.getOrDefault(e.getPlayer().getUniqueId(), 0);
            if (count > 10) {
                e.getPlayer().sendMessage(VaultLoader.getMessage("creative.entity-max"));
                e.setCancelled(true);
            }
            count++;
            entities.put(e.getPlayer().getUniqueId(), count);
        }
    }
}
