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

import lombok.SneakyThrows;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.concurrent.ThreadLocalRandom;

public class OreGen extends ConstructorRegisterListener {
    @EventHandler
    @SneakyThrows
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getPlayer().getWorld().getName().toLowerCase().contains("survival") || e.getPlayer().getWorld().getName().contains("clans")) {
            if (e.getFrom().getChunk() != e.getTo().getChunk()) {
                VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
                if (player.isNew()) {
                    int rand = ThreadLocalRandom.current().nextInt(0, 100);
                    if (rand <= 5) {
                        outerLoop:
                        for (int x = -5; x <= 5; x++) {
                            for (int y = -5; y <= 5; y++) {
                                for (int z = -5; z <= 5; z++) {
                                    if (e.getPlayer().getLocation().getBlock().getRelative(x, y, z).getType() == Material.DIAMOND_ORE) {
                                        // This area is already generated.
                                        break outerLoop;
                                    }
                                    if (e.getPlayer().getLocation().getBlock().getRelative(x, y, z).getType() == Material.STONE) {
                                        if (ThreadLocalRandom.current().nextInt(0, 100) <= 1) {
                                            e.getPlayer().getLocation().getBlock().getRelative(x, y, z).setType(Material.DIAMOND_ORE);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
