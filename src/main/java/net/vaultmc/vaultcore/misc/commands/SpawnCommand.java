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

package net.vaultmc.vaultcore.misc.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(
        literal = "spawn",
        description = "Teleports you to the world spawn."
)
@Permission(Permissions.SpawnCommand)
@PlayerOnly
public class SpawnCommand extends CommandExecutor {
    public SpawnCommand() {
        register("spawn", Collections.emptyList());
    }

    @SubCommand("spawn")
    public void spawn(VLPlayer sender) {
        if (sender.getWorld().getName().equalsIgnoreCase("skyblock")) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.spawn.disabled"));
            return;
        }
        String world = sender.getWorld().getName();
        if (world.equalsIgnoreCase("creative") || world.equalsIgnoreCase("Lobby"))
            sender.teleport(sender.getWorld().getSpawnLocation());
        else
            sender.teleportNoMove(sender.getWorld().getSpawnLocation());
    }
}
