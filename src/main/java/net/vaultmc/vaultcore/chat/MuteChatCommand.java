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

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import org.bukkit.Bukkit;

import java.util.Collections;

@RootCommand(literal = "mutechat", description = "Mutes the chat.")
@Permission(Permissions.MuteChatCommand)
public class MuteChatCommand extends CommandExecutor {
    public static boolean chatMuted = false;

    public MuteChatCommand() {
        register("mutechat", Collections.emptyList());
    }

    @SubCommand("mutechat")
    public void muteChat(VLCommandSender sender) {
        if (chatMuted) {
            chatMuted = false;
            sender.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.mute_chat.sender"), "unmuted"));
            Bukkit.broadcastMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.mute_chat.players"), "unmuted",
                            sender.getDisplayName()));

        } else {
            chatMuted = true;
            sender.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.mute_chat.sender"), "muted"));
            Bukkit.broadcastMessage(Utilities.formatMessage(
                    VaultLoader.getMessage("vaultcore.commands.mute_chat.players"), "muted", sender.getDisplayName()));
        }
    }
}
