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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.text.Collator;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

@RootCommand(literal = "list", description = "See who is online.")
@Permission(Permissions.ListCommand)
@Aliases({"online", "ls"})
public class ListCommand extends CommandExecutor {
    public ListCommand() {
        unregisterExisting();
        register("list", Collections.emptyList());
    }

    @SubCommand("list")
    public void list(VLCommandSender sender) {
        Collection<String> admin = new TreeSet<>(Collator.getInstance());
        Collection<String> moderator = new TreeSet<>(Collator.getInstance());
        Collection<String> helper = new TreeSet<>(Collator.getInstance());
        Collection<String> trusted = new TreeSet<>(Collator.getInstance());
        Collection<String> patreon = new TreeSet<>(Collator.getInstance());
        Collection<String> member = new TreeSet<>(Collator.getInstance());
        Collection<String> defaults = new TreeSet<>(Collator.getInstance());

        if (Bukkit.getOnlinePlayers().size() == 0) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.list.no_players_online"));
        } else {
            int count = 0;
            for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                count++;
                String vanished = "";
                if (sender instanceof VLPlayer) {
                    // If sender is player with no vanish perms
                    if (!((VLPlayer) sender).hasPermission(Permissions.VanishCommand) && player.isVanished()) {
                        count--;
                        continue;
                        // If sender is player and has vanish perms
                    } else if (player.isVanished() && ((VLPlayer) sender).hasPermission(Permissions.VanishCommand)) {
                        vanished = VaultLoader.getMessage("vaultcore.commands.list.player_vanished");
                    }
                    // Sender is console with perms
                } else if (player.isVanished()) {
                    vanished = VaultLoader.getMessage("vaultcore.commands.list.player_vanished");
                }
                String rank = player.getGroup();
                switch (rank) {
                    case "admin":
                        admin.add(ChatColor.YELLOW + player.getDisplayName() + vanished + ChatColor.YELLOW);
                        break;
                    case "moderator":
                        moderator.add(ChatColor.YELLOW + player.getDisplayName() + vanished + ChatColor.YELLOW);
                        break;
                    case "helper":
                        helper.add(ChatColor.YELLOW + player.getDisplayName() + vanished + ChatColor.YELLOW);
                    case "trusted":
                        trusted.add(ChatColor.YELLOW + player.getDisplayName() + vanished + ChatColor.YELLOW);
                        break;
                    case "patreon":
                        patreon.add(ChatColor.YELLOW + player.getDisplayName() + vanished + ChatColor.YELLOW);
                        break;
                    case "member":
                        member.add(ChatColor.YELLOW + player.getDisplayName() + vanished + ChatColor.YELLOW);
                        break;
                    default:
                        defaults.add(ChatColor.YELLOW + player.getDisplayName() + vanished + ChatColor.YELLOW);
                        break;
                }
            }
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.list.header"));
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.list.count"), count));
            if (!admin.isEmpty())
                sender.sendMessage(ChatColor.BLUE + "Admins: " + ChatColor.YELLOW + Utilities.listToString(admin, true));
            if (!moderator.isEmpty())
                sender.sendMessage(ChatColor.DARK_AQUA + "Moderators: " + ChatColor.YELLOW + Utilities.listToString(moderator, true));
            if (!helper.isEmpty())
                sender.sendMessage(ChatColor.YELLOW + "Helpers: " + ChatColor.YELLOW + Utilities.listToString(helper, true));
            if (!trusted.isEmpty())
                sender.sendMessage(ChatColor.AQUA + "Trusted: " + ChatColor.YELLOW + Utilities.listToString(trusted, true));
            if (!patreon.isEmpty())
                sender.sendMessage(ChatColor.WHITE + "Patreons: " + ChatColor.YELLOW + Utilities.listToString(patreon, true));
            if (!member.isEmpty())
                sender.sendMessage(ChatColor.GRAY + "Members: " + ChatColor.YELLOW + Utilities.listToString(member, true));
            if (!defaults.isEmpty())
                sender.sendMessage(ChatColor.DARK_GRAY + "Defaults: " + ChatColor.YELLOW + Utilities.listToString(defaults, true));
        }
    }
}