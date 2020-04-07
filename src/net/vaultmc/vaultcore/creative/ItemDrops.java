package net.vaultmc.vaultcore.creative;

import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class ItemDrops implements Listener {

    @EventHandler
    public void onItemDrop(BlockBreakEvent event) {
        VLPlayer player = VLPlayer.getPlayer(event.getPlayer());
        if (player.getWorld().equals("Creative")) {
            if (!PlayerSettings.getSetting(player, "settings.item_drops")) {
                event.setDropItems(false);
            }
        }
    }
}
