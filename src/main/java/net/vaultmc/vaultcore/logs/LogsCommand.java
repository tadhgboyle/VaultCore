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

package net.vaultmc.vaultcore.logs;

import lombok.Setter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;

import java.util.Collections;

//Yeah.. it literally just fixes itself
@RootCommand(literal = "logs", description = "Search the logs for a phrase.")
@Permission(Permissions.LogsCommand)
public class LogsCommand extends CommandExecutor {
    @Setter
    public static Boolean searching = false;

    public LogsCommand() {
        register("logs", Collections.singletonList(Arguments.createArgument("search", Arguments.greedyString())));
    }

    @SubCommand("logs")
    public void log(VLCommandSender sender, String search) {
        if (searching) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.logs.already_searching"));
            return;
        }
        if (search.length() < 3) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.logs.minimum_length"));
            return;
        }
        LogsHandler logsHandler = new LogsHandler(sender, search);
        Thread thread = new Thread(logsHandler);
        thread.start();
    }
}
