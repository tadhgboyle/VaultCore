package net.vaultmc.vaultcore.commands.teleport;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
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
	String string = Utilities.string;
	String variable1 = Utilities.variable1;

	public TPAHereCommand() {
		register("tpahere", Collections.singletonList(Arguments.createArgument("player", Arguments.playerArgument())));
	}

	@SubCommand("tpahere")
	public void tpaHere(VLPlayer player, VLPlayer target) {
		if (target == player) {
			player.sendMessage(ChatColor.RED + "You can't teleport to yourself!");
			return;
		}
		if (!player.getDataConfig().getBoolean("settings.tpa")) {
			player.sendMessage(ChatColor.RED + "That player has disabled TPAs!");
			return;
		}
		requestsHere.put(target.getUniqueId(), player.getUniqueId());
		player.sendMessage(string + "You requested that " + target.getFormattedName() + string + " teleports to you.");
		target.sendMessage(player.getFormattedName() + string + " asked you to teleport to them, type " + variable1
				+ "/tpaccept " + string + "to accept it.");
	}
}