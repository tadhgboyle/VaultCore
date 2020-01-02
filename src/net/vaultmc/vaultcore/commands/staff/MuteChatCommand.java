package net.vaultmc.vaultcore.commands.staff;

import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.CommandExecutor;
import net.vaultmc.vaultutils.utils.commands.experimental.Permission;
import net.vaultmc.vaultutils.utils.commands.experimental.RootCommand;
import net.vaultmc.vaultutils.utils.commands.experimental.SubCommand;

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
