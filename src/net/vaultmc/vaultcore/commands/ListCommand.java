package net.vaultmc.vaultcore.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

@RootCommand(literal = "list", description = "See who is online.")
@Permission(Permissions.ListCommand)
public class ListCommand extends CommandExecutor {

	List<String> admin = new ArrayList<>();
	List<String> moderator = new ArrayList<>();
	List<String> trusted = new ArrayList<>();
	List<String> patreon = new ArrayList<>();
	List<String> member = new ArrayList<>();
	List<String> defaults = new ArrayList<>();

	public ListCommand() {
		this.register("list", Collections.emptyList());
	}

	@SubCommand("list")
	public void list(VLCommandSender sender) {

		if (Bukkit.getOnlinePlayers().toArray().length == 0) {
			sender.sendMessage(ChatColor.RED + "There are no players online.");
		} else {
			for (Player players : Bukkit.getOnlinePlayers()) {
				VLPlayer player = VLPlayer.getPlayer(players);
				String rank = player.getGroup();
				switch (rank) {
				case "admin":
					admin.add(player.getName());
					break;
				case "moderator":
					moderator.add(player.getName());
					break;
				case "trusted":
					trusted.add(player.getName());
					break;
				case "patreon":
					patreon.add(player.getName());
					break;
				case "member":
					member.add(player.getName());
					break;
				default:
					defaults.add(player.getName());
					break;
				}
			}
			sender.sendMessage(ChatColor.DARK_GREEN + "--== [List] ==--");
			if (!admin.isEmpty()) sender.sendMessage(ChatColor.BLUE + "Admins: " + ChatColor.RESET + listToString(admin));
			if (!moderator.isEmpty()) sender.sendMessage(ChatColor.DARK_AQUA + "Moderators: " + ChatColor.RESET+ listToString(moderator));
			if (!trusted.isEmpty()) sender.sendMessage(ChatColor.AQUA + "Trusted: " + ChatColor.RESET+ listToString(trusted));
			if (!patreon.isEmpty()) sender.sendMessage(ChatColor.WHITE + "Patreons: " + ChatColor.RESET+ listToString(patreon));
			if (!member.isEmpty()) sender.sendMessage(ChatColor.GRAY + "Members: " + ChatColor.RESET+ listToString(admin));
			if (!defaults.isEmpty()) sender.sendMessage(ChatColor.DARK_GRAY + "Defaults: " + ChatColor.RESET+ listToString(defaults));
			
		}
	}
	
    private String listToString(List<String> from) {

        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (String x : from) {
            if (first) {
                result.append(x);
                first = false;
                continue;
            }
            result.append(", ");
            result.append(x);
        }
        return result.toString();
    }
}