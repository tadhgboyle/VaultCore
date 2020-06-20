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

package net.vaultmc.vaultcore.afk;

import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class AFKListener {
    private static final Map<VLPlayer, Location> locations = new HashMap<>();

    public static void afkUpdater() {
        for (VLPlayer players : VLPlayer.getOnlinePlayers()) {
            if (locations.get(players) == null) {
                locations.put(players, players.getLocation());  // Oh my god this is a smart solution
                // wc nb
            } else if (locations.get(players) == players.getLocation()) {
                players.performCommand("/afk");
            }
        }
    }
}
