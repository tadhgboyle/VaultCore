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

package net.vaultmc.vaultcore.pvp;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Arrays;

@RootCommand(
        literal = "setkills",
        description = "Useful useful"
)
@Permission(Permissions.PvPAdmin)
public class SetKillsCommand extends CommandExecutor {
    public SetKillsCommand() {
        register("setKills", Arrays.asList(
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("amount", Arguments.doubleArgument())
        ), "vaultcore");

    }

    @SubCommand("setKills")
    public void setKills(VLCommandSender sender, VLPlayer target, double amount) {
        double kills = target.getDataConfig().getInt("stats.kills");
        target.getDataConfig().set("stats.kills", amount);
        target.saveData();
    }
}
