package net.vaultmc.vaultcore.misc.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RootCommand(literal = "list", description = "See who is online.")
@Permission(Permissions.ListCommand)
@Aliases("online")
public class ListCommand extends CommandExecutor {
    private static final List<String> admin = new ArrayList<>();
    private static final List<String> moderator = new ArrayList<>();
    private static final List<String> trusted = new ArrayList<>();
    private static final List<String> patreon = new ArrayList<>();
    private static final List<String> member = new ArrayList<>();
    private static final List<String> defaults = new ArrayList<>();

    public ListCommand() {
        unregisterExisting();
        this.register("list", Collections.emptyList());
    }

    @SubCommand("list")
    public void list(VLCommandSender sender) {
        if (Bukkit.getOnlinePlayers().toArray().length == 0) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.list.no_players_online"));
        } else {
            for (Player players : Bukkit.getOnlinePlayers()) {
                VLPlayer player = VLPlayer.getPlayer(players);
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
                        admin.add(player.getName() + vanished);
                        break;
                    case "moderator":
                        moderator.add(player.getName() + vanished);
                        break;
                    case "trusted":
                        trusted.add(player.getName() + vanished);
                        break;
                    case "patreon":
                        patreon.add(player.getName() + vanished);
                        break;
                    case "member":
                        member.add(player.getName() + vanished);
                        break;
                    default:
                        defaults.add(player.getName() + vanished);
                        break;
                }
            }
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.list.header"));
            // todo with messages.yml
            if (!admin.isEmpty())
                sender.sendMessage(ChatColor.BLUE + "Admins: " + ChatColor.YELLOW + listToString(admin));
            if (!moderator.isEmpty())
                sender.sendMessage(ChatColor.DARK_AQUA + "Moderators: " + ChatColor.YELLOW + listToString(moderator));
            if (!trusted.isEmpty())
                sender.sendMessage(ChatColor.AQUA + "Trusted: " + ChatColor.YELLOW + listToString(trusted));
            if (!patreon.isEmpty())
                sender.sendMessage(ChatColor.WHITE + "Patreons: " + ChatColor.YELLOW + listToString(patreon));
            if (!member.isEmpty())
                sender.sendMessage(ChatColor.GRAY + "Members: " + ChatColor.YELLOW + listToString(member));
            if (!defaults.isEmpty())
                sender.sendMessage(ChatColor.DARK_GRAY + "Defaults: " + ChatColor.YELLOW + listToString(defaults));

            admin.clear();
            moderator.clear();
            trusted.clear();
            patreon.clear();
            member.clear();
            defaults.clear();
        }
    }

    private String listToString(List<String> from) {

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : from) {
            if (first) {
                sb.append(s);
                first = false;
                continue;
            }
            sb.append(", ");
            sb.append(s);
        }
        return sb.toString();
    }
}