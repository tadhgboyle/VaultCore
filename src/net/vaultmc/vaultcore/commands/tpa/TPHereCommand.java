package net.vaultmc.vaultcore.commands.tpa;

import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultutils.utils.commands.experimental.Arguments;
import net.vaultmc.vaultutils.utils.commands.experimental.CommandExecutor;
import net.vaultmc.vaultutils.utils.commands.experimental.Permission;
import net.vaultmc.vaultutils.utils.commands.experimental.PlayerOnly;
import net.vaultmc.vaultutils.utils.commands.experimental.RootCommand;
import net.vaultmc.vaultutils.utils.commands.experimental.SubCommand;

@RootCommand(literal = "tphere", description = "Teleport a player to you.")
@Permission(Permissions.TPHereCommand)
@PlayerOnly
public class TPHereCommand extends CommandExecutor {

	public TPHereCommand() {
		register("tphere", Collections.singletonList(Arguments.createArgument("player", Arguments.playerArgument())));
	}

	@SubCommand("tphere")
	public void tpaHere(CommandSender sender, Player target) {
		String string = ChatColor.translateAlternateColorCodes('&',
				VaultCore.getInstance().getConfig().getString("string"));
		String variable1 = ChatColor.translateAlternateColorCodes('&',
				VaultCore.getInstance().getConfig().getString("variable-1"));

		Player player = (Player) sender;
		if (target == player) {
			player.sendMessage(ChatColor.RED + "You can't teleport to yourself!");
			return;
		}
		target.teleport(player);
		player.sendMessage(string + "You have teleported " + variable1 + target.getName() + string + " to you.");
		
	}
}