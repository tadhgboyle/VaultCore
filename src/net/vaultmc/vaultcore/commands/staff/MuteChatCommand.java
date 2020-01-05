package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(literal = "mutechat", description = "Mutes the chat.")
@Permission(Permissions.MuteChatCommand)
public class MuteChatCommand extends CommandExecutor {
	public static boolean chatMuted = false;

	private String string = Utilities.string;
	private String variable1 = Utilities.variable1;

	public MuteChatCommand() {
		register("mutechat", Collections.emptyList());
	}

	@SubCommand("mutechat")
	public void muteChat(CommandSender sender) {
		if (chatMuted) {
			chatMuted = false;
			Bukkit.broadcastMessage(string + "The chat is no longer muted!");

		} else {
			chatMuted = true;
			Bukkit.broadcastMessage(string + "The chat has been muted by " + variable1
					+ (sender instanceof Player ? VaultCoreAPI.getName((Player) sender)
							: ChatColor.BLUE + "" + ChatColor.BOLD + "CONSOLE"));
		}
	}
}
