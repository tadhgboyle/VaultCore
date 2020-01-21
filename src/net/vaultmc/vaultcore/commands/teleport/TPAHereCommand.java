package net.vaultmc.vaultcore.commands.teleport;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

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

@RootCommand(literal = "tpahere", description = "Request for a player to teleport you.")
@Permission(Permissions.TPAHereCommand)
@PlayerOnly
public class TPAHereCommand extends CommandExecutor {
	private static HashMap<UUID, UUID> requestsHere = TPACommand.getRequestsHere();

	public TPAHereCommand() {
		register("tpahere", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
	}

	@SubCommand("tpahere")
	public void tpaHere(VLPlayer player, VLPlayer target) {
		if (target == player) {
			player.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.self_error"));
			return;
		}
		if (!player.getDataConfig().getBoolean("settings.tpa")) {
			player.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.target_disabled_tpa"));
			return;
		}
		requestsHere.put(target.getUniqueId(), player.getUniqueId());

		player.sendMessage(Utilities.formatMessage(
				VaultLoader.getMessage("vaultcore.commands.tpa.tpahere.request_sent"), target.getFormattedName()));

		target.sendMessage(Utilities.formatMessage(
				VaultLoader.getMessage("vaultcore.commands.tpa.tpahere.request_received"), player.getFormattedName()));
	}
}