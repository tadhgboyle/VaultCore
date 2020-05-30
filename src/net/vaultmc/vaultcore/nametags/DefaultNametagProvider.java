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
            case "trusted":
                priority = "c";
                break;
            case "patreon":
                priority = "d";
                break;
            case "member":
                priority = "e";
                break;
            case "default":
                priority = "f";
                break;
        }

        return priority;
    }

    @Override
    public Nametag provideNametag(VLPlayer player, World world) {
        return new Nametag(player.getPrefix(world), player.getSuffix(world), getSortPriority(player));
    }
}
