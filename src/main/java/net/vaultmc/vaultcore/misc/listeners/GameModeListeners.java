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
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class GameModeListeners extends ConstructorRegisterListener {
    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        String world = player.getWorld().getName().toLowerCase();

        if (world.contains("clans") || world.contains("survival") || world.toLowerCase().contains("skyblock")) {
            player.setGameMode(GameMode.SURVIVAL);
        } else if (world.contains("lobby") || world.contains("kingdoms")) {
            player.setGameMode(GameMode.ADVENTURE);
        } else if (world.contains("creative")) {
            player.setGameMode(GameMode.CREATIVE);
        } else if (world.contains("build")) {
            player.setGameMode(GameMode.CREATIVE);
            player.sendMessageByKey("vaultcore.build.notification");
        }
    }
}
