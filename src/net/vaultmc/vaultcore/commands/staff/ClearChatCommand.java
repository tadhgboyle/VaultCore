package net.vaultmc.vaultcore.commands.staff;

import java.util.Collections;

import org.bukkit.Bukkit;

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

@RootCommand(literal = "clearchat", description = "Clears all messages.")
@Permission(Permissions.ClearChatCommand)
@Aliases("cc")
public class ClearChatCommand extends CommandExecutor {
	public ClearChatCommand() {
		register("clear", Collections.emptyList());
	}

	@SubCommand("clear")
	public void clearChat(VLCommandSender sender) {

		for (int i = 0; i < 200; i++) {
			for (VLPlayer players : VLPlayer.getOnlinePlayers()) {
				players.sendMessage(" ");
			}
		}
		sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.clear_chat.sender"));
		Bukkit.broadcastMessage(
				Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.clear_chat.players"), sender.getFormattedName()));
	}
}