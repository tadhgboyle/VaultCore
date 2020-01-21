package net.vaultmc.vaultcore.commands.teleport;

import java.util.Arrays;
import java.util.Collections;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.Aliases;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

@RootCommand(literal = "teleport", description = "Teleport to a player.")
@Permission(Permissions.TeleportCommand)
@Aliases("tp")
public class TPCommand extends CommandExecutor {

	public TPCommand() {
		unregisterExisting();
		register("teleportToPlayer",
				Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
		register("teleportPlayerToPlayer",
				Arrays.asList(Arguments.createArgument("target1", Arguments.playerArgument()),
						Arguments.createArgument("to", Arguments.playerArgument())));
	}

	@SubCommand("teleportToPlayer")
	@PlayerOnly
	public void teleportToPlayer(VLPlayer sender, VLPlayer target) {
		if (sender.getName() == target.getName()) {
			sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.self_error"));
			return;
		}
		sender.teleport(target.getLocation());
		sender.sendMessage(Utilities.formatMessage(
				VaultLoader.getMessage("vaultcore.commands.teleport.sender_to_player"), target.getFormattedName()));
	}

	@SubCommand("teleportPlayerToPlayer")
	@PlayerOnly
	public void teleportPlayerToPlayer(VLPlayer sender, VLPlayer target, VLPlayer to) {
		if (target == sender || to == sender) {
			sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.target_error"));
			return;
		}
		target.teleport(to.getLocation());
		sender.sendMessage(
				Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.teleport.player_to_player_sender"),
						target.getFormattedName(), to.getFormattedName()));
		target.sendMessage(
				Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.teleport.player_to_player_target"),
						sender.getFormattedName(), to.getFormattedName()));
		to.sendMessage(
				Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.teleport.player_to_player_receiver"),
						sender.getFormattedName(), target.getFormattedName()));
	}
}
