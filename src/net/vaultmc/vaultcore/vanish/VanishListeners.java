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

package net.vaultmc.vaultcore.vanish;

import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Iterator;

public class VanishListeners extends ConstructorRegisterListener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        if (player.isVanished() && e.getHand() == EquipmentSlot.HAND && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType().toString().endsWith("CHEST")) {
                player.openInventory(((Chest) e.getClickedBlock().getState()).getBlockInventory());
                e.setCancelled(true);
            } else if (e.getClickedBlock().getType() == Material.BARREL) {
                player.openInventory(((Barrel) e.getClickedBlock().getState()).getInventory());
                e.setCancelled(true);
            } else if (e.getClickedBlock().getType().toString().endsWith("SHULKER_BOX")) {
                player.openInventory(((ShulkerBox) e.getClickedBlock().getState()).getInventory());
                e.setCancelled(true);
            } else if (e.getClickedBlock().getType() == Material.ENDER_CHEST) {
                player.openInventory(player.getEnderChest());
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMultiplayerPagePing(ServerListPingEvent e) {
        Iterator<Player> it = e.iterator();
        while (it.hasNext()) {
            Player player = it.next();
            if (VanishCommand.vanished.containsKey(player.getUniqueId())) {
                if (VanishCommand.vanished.get(player.getUniqueId())) it.remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (VanishCommand.vanished.getOrDefault(e.getPlayer().getUniqueId(), false)) {
            e.setQuitMessage(null);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        VanishCommand.update(player);
        if (VanishCommand.vanished.getOrDefault(player.getUniqueId(), false)) {
            VanishCommand.setVanishState(player, true);
        }
    }
}
