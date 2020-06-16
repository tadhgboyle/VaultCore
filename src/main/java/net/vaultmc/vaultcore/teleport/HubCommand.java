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

package net.vaultmc.vaultcore.teleport;

import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;

import java.util.Collections;

@RootCommand(
        literal = "hub",
        description = "Go back to the hub."
)
@Aliases({"lobby"})
@PlayerOnly
public class HubCommand extends CommandExecutor {
    public HubCommand() {
        register("hub", Collections.emptyList());
    }

    @SubCommand("hub")
    public void hub(VLPlayer sender) {
        sender.teleportNoMove(Bukkit.getWorld("Lobby").getSpawnLocation());
    }
}
