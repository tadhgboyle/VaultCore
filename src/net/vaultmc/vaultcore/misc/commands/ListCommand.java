package net.vaultmc.vaultcore.misc.commands;

import java.text.Collator;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.Aliases;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

@RootCommand(literal = "list", description = "See who is online.")
@Permission(Permissions.ListCommand)
@Aliases("online")
public class ListCommand extends CommandExecutor {
    private static final Collection<String> admin = new TreeSet<String>(Collator.getInstance());
    private static final Collection<String> moderator = new TreeSet<String>(Collator.getInstance());
    private static final Collection<String> trusted = new TreeSet<String>(Collator.getInstance());
    private static final Collection<String> patreon = new TreeSet<String>(Collator.getInstance());
    private static final Collection<String> member = new TreeSet<String>(Collator.getInstance());
    private static final Collection<String> defaults = new TreeSet<String>(Collator.getInstance());

    public ListCommand() {
        unregisterExisting();
        this.register("list", Collections.emptyList());
    }

    @SubCommand("list")
    public void list(VLCommandSender sender) {
        if (Bukkit.getOnlinePlayers().size() == 0) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.list.no_players_online"));
        } else {
            for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                String vanished = "";
                if (sender instanceof VLPlayer) {
                    if (!((VLPlayer) sender).hasPermission(Permissions.VanishCommand) && player.isVanished()) {
                        continue;
                    } else if (player.isVanished() && ((VLPlayer) sender).hasPermission(Permissions.VanishCommand)) {
                        vanished = VaultLoader.getMessage("vaultcore.commands.list.player_vanished");
                    }
                } else if (player.isVanished()) {
                    vanished = VaultLoader.getMessage("vaultcore.commands.list.player_vanished");
                }
                String rank = player.getGroup();
                switch (rank) {
                    case "admin":
                        admin.add(player.getName() + vanished + ChatColor.YELLOW);
                        break;
                    case "moderator":
                        moderator.add(player.getName() + vanished + ChatColor.YELLOW);
                        break;
                    case "trusted":
                        trusted.add(player.getName() + vanished + ChatColor.YELLOW);
                        break;
                    case "patreon":
                        patreon.add(player.getName() + vanished + ChatColor.YELLOW);
                        break;
                    case "member":
                        member.add(player.getName() + vanished + ChatColor.YELLOW);
                        break;
                    default:
                        defaults.add(player.getName() + vanished + ChatColor.YELLOW);
                        break;
                }
            }
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.list.header"));
            // todo with messages.yml
            if (!admin.isEmpty())
                sender.sendMessage(ChatColor.BLUE + "Admins: " + ChatColor.YELLOW + Utilities.listToString(admin));
            if (!moderator.isEmpty())
                sender.sendMessage(ChatColor.DARK_AQUA + "Moderators: " + ChatColor.YELLOW + Utilities.listToString(moderator));
            if (!trusted.isEmpty())
                sender.sendMessage(ChatColor.AQUA + "Trusted: " + ChatColor.YELLOW + Utilities.listToString(trusted));
            if (!patreon.isEmpty())
                sender.sendMessage(ChatColor.WHITE + "Patreons: " + ChatColor.YELLOW + Utilities.listToString(patreon));
            if (!member.isEmpty())
                sender.sendMessage(ChatColor.GRAY + "Members: " + ChatColor.YELLOW + Utilities.listToString(member));
            if (!defaults.isEmpty())
                sender.sendMessage(ChatColor.DARK_GRAY + "Defaults: " + ChatColor.YELLOW + Utilities.listToString(defaults));

            admin.clear();
            moderator.clear();
            trusted.clear();
            patreon.clear();
            member.clear();
            defaults.clear();
        }
    }
}