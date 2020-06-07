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

package net.vaultmc.vaultcore.nametags;

import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.World;

public class DefaultNametagProvider implements INametagProvider {
    private static String getSortPriority(VLPlayer p) {
        // Get the sort priority by permissions to a player.
        String group = p.getGroup();
        String priority = null;

        switch (group) {
            case "admin":
                priority = "a";
                break;
            case "moderator":
                priority = "b";
                break;
            case "helper":
                priority = "c";
                break;
            case "trusted":
                priority = "d";
                break;
            case "patreon":
                priority = "e";
                break;
            case "member":
                priority = "f";
                break;
            case "default":
                priority = "g";
                break;
        }

        return priority;
    }

    @Override
    public Nametag provideNametag(VLPlayer player, World world) {
        return new Nametag(player.getPrefix(world), player.getSuffix(world), getSortPriority(player));
    }
}
