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

import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;

import java.util.Collections;

@RootCommand(literal = "ranks", description = "Learn about the ranks on our server.")
@Permission(Permissions.RanksCommand)
public class RanksCommand extends CommandExecutor {
    public RanksCommand() {
        register("checkRanks", Collections.emptyList());
    }

    @SubCommand("checkRanks")
    public void checkRanks(VLCommandSender sender) {
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.ranks.player_header"));
        sender.sendMessage(ChatColor.DARK_GRAY + "Default");
        sender.sendMessage(ChatColor.GRAY + "Member");
        sender.sendMessage(ChatColor.WHITE + "Patreon");
        sender.sendMessage(ChatColor.DARK_PURPLE + "Trusted");
        sender.sendMessage("");
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.ranks.donor_header"));
        sender.sendMessage(ChatColor.AQUA + "Hero");
        sender.sendMessage(ChatColor.GREEN + "Titan");
        sender.sendMessage(ChatColor.GOLD + "God");
        sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Lord");
        sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Overlord");
        sender.sendMessage("");
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.ranks.staff_header"));
        sender.sendMessage(ChatColor.DARK_AQUA + "Helper " + ChatColor.YELLOW + "•");
        sender.sendMessage(ChatColor.DARK_AQUA + "Moderator");
        sender.sendMessage(ChatColor.BLUE + "Administrator");
    }
}