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
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;

import java.util.Collections;

@RootCommand(
        literal = "vcreload",
        description = "Reloads VaultCore's configuration and data"
)
@Permission(Permissions.ReloadCommand)
public class ReloadCommand extends CommandExecutor {
    public ReloadCommand() {
        register("reload", Collections.emptyList());
    }

    @SubCommand("reload")
    public void reload(VLCommandSender sender) {
        VaultCore.getInstance().reloadConfig();
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.reload"));
    }
}
