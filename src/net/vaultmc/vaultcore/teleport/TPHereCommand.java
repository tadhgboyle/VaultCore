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

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(literal = "tphere", description = "Teleport a player to you.")
@Permission(Permissions.TeleportCommandHere)
@Aliases("tph")
@PlayerOnly
public class TPHereCommand extends CommandExecutor {
    public TPHereCommand() {
        register("tphere", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    @SubCommand("tphere")
    public void tpHere(VLPlayer sender, VLPlayer target) {
        if (sender == target) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.self_error"));
            return;
        }
        target.teleport(sender.getLocation());
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tphere.sender"), target.getFormattedName()));
        target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tphere.target"), sender.getFormattedName()));
    }
}