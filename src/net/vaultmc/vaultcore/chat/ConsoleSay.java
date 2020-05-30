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

package net.vaultmc.vaultcore.chat;

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.commands.wrappers.WrappedChatComponent;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(literal = "say", description = "Used by the console to send messages.")
@Aliases("chat")
public class ConsoleSay extends CommandExecutor {
    public ConsoleSay() {
        register("say", Collections.singletonList(Arguments.createArgument("message", Arguments.messageArgument())));
    }

    @SubCommand("say")
    public void say(VLCommandSender sender, WrappedChatComponent message) {
        if (sender instanceof VLPlayer) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.console_only"));
            return;
        }
        for (VLPlayer players : VLPlayer.getOnlinePlayers()) {
            players.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.console_say.format"), message.toString()));
        }
    }
}