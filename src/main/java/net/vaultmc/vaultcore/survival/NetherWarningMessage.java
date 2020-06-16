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

package net.vaultmc.vaultcore.survival;

import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class NetherWarningMessage extends ConstructorRegisterListener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase("survival_nether")) {
            e.getPlayer().sendMessage(VaultLoader.getMessage("vaultcore.survival.nether_warning"));
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase("survival_nether")) {
            e.getPlayer().sendMessage(VaultLoader.getMessage("vaultcore.survival.nether_warning"));
        }
    }
}
