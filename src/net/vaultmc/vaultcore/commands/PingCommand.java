package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultloader.utils.commands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(literal = "ping", description = "Check the ping of yourself or other players.")
@Permission(Permissions.PingCommand)
public class PingCommand extends CommandExecutor {
	private String string = Utilities.string;
	private String variable1 = Utilities.variable1;
	private String variable2 = Utilities.variable2;

	public PingCommand() {
		register("pingSelf", Collections.emptyList());
		register("pingOthers",
				Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
	}

	@SubCommand("pingSelf")
	@PlayerOnly
	public void pingSelf(CommandSender sender) {
		Player player = (Player) sender;
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
				string + "" + "Your ping is: " + variable2 + "" + player.spigot().getPing() + string + "ms"));
	}

	@SubCommand("pingOthers")
	@Permission(Permissions.PingCommandOther)
	public void pingOthers(CommandSender sender, Player target) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', variable1 + "" + VaultCoreAPI.getName(target)
				+ string + "" + "'s ping is: " + variable2 + "" + target.spigot().getPing() + string + "ms"));
	}
}