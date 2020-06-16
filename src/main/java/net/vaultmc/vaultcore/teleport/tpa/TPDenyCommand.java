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

package net.vaultmc.vaultcore.teleport.tpa;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(literal = "tpdeny", description = "Deny a teleport request from a player.")
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPDenyCommand extends CommandExecutor {
    public TPDenyCommand() {
        register("tpdeny", Collections.emptyList());
    }

    @SubCommand("tpdeny")
    @SneakyThrows
    public void tpDeny(VLPlayer sender) {
        if (!(TPACommand.getTpaRequests().containsKey(sender) && TPAHereCommand.getTpaRequestsHere().containsKey(sender))) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.no_request_error"));
        } else {
            TPACommand.getTpaRequests().get(sender).sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.response_sender"), sender.getFormattedName(), "declined"));
            TPAHereCommand.getTpaRequestsHere().get(sender).sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.response_sender"), sender.getFormattedName(), "declined"));
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.declined"));
            TPACommand.getTpaRequests().remove(sender);
            TPAHereCommand.getTpaRequestsHere().remove(sender);
        }
    }
}