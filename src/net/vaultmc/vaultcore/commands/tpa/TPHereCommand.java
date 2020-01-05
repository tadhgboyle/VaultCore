package net.vaultmc.vaultcore.commands.tpa;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultloader.utils.commands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(literal = "tphere", description = "Teleport a player to you.")
@Permission(Permissions.TPHereCommand)
@PlayerOnly
public class TPHereCommand extends CommandExecutor {

	String string = Utilities.string;
	
	public TPHereCommand() {
		register("tphere", Collections.singletonList(Arguments.createArgument("player", Arguments.playerArgument())));
	}

	@SubCommand("tphere")
	public void tpaHere(CommandSender sender, Player target) {

		Player player = (Player) sender;
		if (target == player) {
			player.sendMessage(ChatColor.RED + "You can't teleport to yourself!");
			return;
		}
		target.teleport(player);
		player.sendMessage(string + "You have teleported " + VaultCoreAPI.getName(target) + string + " to you.");

	}
}