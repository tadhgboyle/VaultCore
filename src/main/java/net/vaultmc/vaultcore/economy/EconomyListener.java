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

package net.vaultmc.vaultcore.economy;

import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.concurrent.ThreadLocalRandom;

public class EconomyListener extends ConstructorRegisterListener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null && (e.getEntity().getWorld().getName().startsWith("skyblock") || e.getEntity().getWorld().getName().startsWith("survival"))) {
            double money = ThreadLocalRandom.current().nextInt(10, 30);
            VLPlayer player = VLPlayer.getPlayer(e.getEntity().getKiller());
            player.sendMessage(ChatColor.GOLD + "+$" + money);
            player.deposit(player.getWorld(), money);
        }
    }
}
