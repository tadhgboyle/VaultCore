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

package net.vaultmc.vaultcore.misc.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Arrays;

@RootCommand(literal = "sudo", description = "Force a player to run a command.")
@Permission(Permissions.SudoCommand)
public class SudoCommand extends CommandExecutor {

    public SudoCommand() {
        register("sudo", Arrays.asList(Arguments.createArgument("target", Arguments.playerArgument()), Arguments.createArgument("command", Arguments.greedyString())));
    }

    @SubCommand("sudo")
    public void sudo(VLCommandSender sender, VLPlayer target, String command) {
        if ((sender instanceof VLPlayer) && sender == target)
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.sudo.self_error"));
        if (target.hasPermission(Permissions.SudoCommandExempt))
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.sudo.excempt_error"));
        else {
            target.performCommand(command);
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.sudo.success"), target.getFormattedName(), command));
        }
    }
}
