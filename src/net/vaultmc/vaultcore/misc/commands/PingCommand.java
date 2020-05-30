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
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(literal = "ping", description = "Check the ping of yourself or other players.")
@Permission(Permissions.PingCommand)
@Aliases("latency")
public class PingCommand extends CommandExecutor {
    public PingCommand() {
        register("pingSelf", Collections.emptyList());
        register("pingOthers",
                Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    @SubCommand("pingSelf")
    @PlayerOnly
    public void pingSelf(VLPlayer player) {
        player.sendMessage(
                Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.ping.self"), player.getPing() + ""));
    }

    @SubCommand("pingOthers")
    @Permission(Permissions.PingCommandOther)
    public void pingOthers(VLCommandSender sender, VLPlayer target) {
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.ping.other"),
                target.getFormattedName(), target.getPing() + ""));
    }
}