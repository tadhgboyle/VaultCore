package net.vaultmc.vaultcore.commands.teleport;

import java.util.Collections;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

@RootCommand(literal = "tphere", description = "Teleport a player to you.")
@Permission(Permissions.TeleportCommandHere)
@PlayerOnly
public class TPHereCommand extends CommandExecutor {

	public TPHereCommand() {
		register("tphere", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
	}

	@SubCommand("tphere")
	public void tpaHere(VLPlayer player, VLPlayer target) {
		if (target == player) {
			player.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.self_error"));
			return;
		}
		target.teleport(player.getLocation());
		player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.tphere.sender"),
				target.getFormattedName()));

	}
}