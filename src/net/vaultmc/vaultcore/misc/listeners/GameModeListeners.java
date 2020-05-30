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

import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class GameModeListeners extends ConstructorRegisterListener {
    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        String world = e.getPlayer().getWorld().getName();

        if (world.contains("clans") || world.contains("Survival") || world.toLowerCase().contains("skyblock")) {
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
        } else if (world.contains("Lobby")) {
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
        } else if (world.contains("Creative")) {
            e.getPlayer().setGameMode(GameMode.CREATIVE);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity().getWorld().getName().equalsIgnoreCase("Lobby"))
            e.setCancelled(true);
    }
}
