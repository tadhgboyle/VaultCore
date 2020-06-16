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

package net.vaultmc.vaultcore.creative;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Collections;

@RootCommand(literal = "cr", description = "Teleport to the Creative world.")
@Permission(Permissions.CreativeCommand)
@PlayerOnly
public class CreativeCommand extends CommandExecutor {
    public CreativeCommand() {
        register("cr", Collections.emptyList());
    }

    @SubCommand("cr")
    public void cr(VLPlayer player) {
        Location cr = Utilities.deserializeLocation(player.getPlayerData().getString("locations.cr"));
        if (cr == null) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.worldtp.never_joined_before"));
            player.teleport(Bukkit.getWorld("creative").getSpawnLocation());
        } else {
            player.teleport(cr);
            player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.worldtp.teleported"),
                    "Creative"));
        }
    }
}