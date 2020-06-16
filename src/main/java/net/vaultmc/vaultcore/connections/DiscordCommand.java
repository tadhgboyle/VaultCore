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

package net.vaultmc.vaultcore.connections;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.util.Collections;

@RootCommand(literal = "discord", description = "Get a link to our Discord Guild.")
@Permission(Permissions.DiscordCommand)
@PlayerOnly
public class DiscordCommand extends CommandExecutor {

    public DiscordCommand() {
        register("discord", Collections.emptyList());
    }

    @SubCommand("discord")
    public void execute(VLPlayer player) {
        String token = TokenCommand.getToken(player.getUniqueId(), player);
        player.sendMessage(VaultLoader.getMessage("vaultcore.commands.discord.guild"));
        player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.token.your_token"),
                token));

        try {
            player.sendMessage(ChatColor.YELLOW + "Status:" + ((player.getDiscord() == 0) ? ChatColor.RED + "Unlinked" : ChatColor.GREEN + "Linked"));
        } catch (Exception e) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.discord.error_checking_linked"));
        }
    }
}
