package net.vaultmc.vaultcore.commands.teleport;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultloader.utils.commands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

@RootCommand(literal = "tpahere", description = "Request for a player to teleport you.")
@Permission(Permissions.TPAHereCommand)
@PlayerOnly
public class TPAHereCommand extends CommandExecutor {
	
	String string = Utilities.string;
	String variable1 = Utilities.variable1;
	
	private static HashMap<UUID, UUID> requestsHere = TPACommand.getRequestsHere();

	public TPAHereCommand() {
		register("tpahere", Collections.singletonList(Arguments.createArgument("player", Arguments.playerArgument())));
	}

	@SubCommand("tpahere")
	public void tpaHere(CommandSender sender, Player target) {

		Player player = (Player) sender;
		if (target == player) {
			player.sendMessage(ChatColor.RED + "You can't teleport to yourself!");
			return;
		}
		if (!VaultCore.getInstance().getPlayerData()
				.getBoolean("players." + target.getUniqueId() + ".settings.tpa")) {
			player.sendMessage(ChatColor.RED + "That player has disabled TPAs!");
			return;
		}
		requestsHere.put(target.getUniqueId(), player.getUniqueId());
		player.sendMessage(string + "You requested that " + VaultCoreAPI.getName(target) + string
				+ " teleports to you.");
		target.sendMessage(VaultCoreAPI.getName(player) + string + " asked you to teleport to them, type "
				+ variable1 + "/tpaccept " + string + "to accept it.");
	}
}