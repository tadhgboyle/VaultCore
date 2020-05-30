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

package net.vaultmc.vaultcore.creative;

import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class ItemDrops extends ConstructorRegisterListener {
    @EventHandler
    public void onItemDrop(BlockBreakEvent event) {
        VLPlayer player = VLPlayer.getPlayer(event.getPlayer());
        if (player.getWorld().getName().equalsIgnoreCase("creative")) {
            if (!PlayerSettings.getSetting(player, "settings.item_drops")) {
                event.setDropItems(false);
            }
        }
    }
}
