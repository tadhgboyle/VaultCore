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

package net.vaultmc.vaultcore.rewards;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;

import java.util.Collections;

@RootCommand(
        literal = "vote",
        description = "Contribute to VaultMC by voting to us!"
)
@Permission(Permissions.VoteCommand)
public class VoteCommand extends CommandExecutor {
    public VoteCommand() {
        register("vote", Collections.emptyList());
    }

    @SubCommand("vote")
    public void vote(VLCommandSender sender) {

    }
}
