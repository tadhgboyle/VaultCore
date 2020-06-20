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

package net.vaultmc.vaultcore.misc.listeners;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

public class StaffPunch extends ConstructorRegisterListener {
    private static final Vector addition = new Vector(0, 2, 0);

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase("Lobby") && (e.getAction() == Action.LEFT_CLICK_AIR ||
                e.getAction() == Action.LEFT_CLICK_BLOCK) && e.getHand() == EquipmentSlot.HAND) {
            Entity targetEntity = e.getPlayer().getTargetEntity(5);
            VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
            if (targetEntity instanceof Player) {
                VLPlayer target = VLPlayer.getPlayer((Player) targetEntity);
                if (target.hasPermission(Permissions.Punchable) && player.hasPermission(Permissions.CanPunch)) {
                    Vector dir = player.getLocation().getDirection().clone();
                    target.getPlayer().setVelocity(dir.multiply(3).add(addition));
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity().getWorld().getName().equalsIgnoreCase("Lobby")) {
            e.setCancelled(true);
        }
    }
}
