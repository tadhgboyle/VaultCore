package net.vaultmc.vaultcore.commands;

import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.Arguments;
import net.vaultmc.vaultutils.utils.commands.experimental.CommandExecutor;
import net.vaultmc.vaultutils.utils.commands.experimental.Permission;
import net.vaultmc.vaultutils.utils.commands.experimental.PlayerOnly;
import net.vaultmc.vaultutils.utils.commands.experimental.RootCommand;
import net.vaultmc.vaultutils.utils.commands.experimental.SubCommand;

@RootCommand(literal = "ping", description = "Check the ping of yourself or other players.")
@Permission(Permissions.PingCommand)
public class PingCommand extends CommandExecutor {
	public PingCommand() {
		register("pingSelf", Collections.emptyList());
		register("pingOthers",
				Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
	}

	private String string = Utilities.string;
	private String variable1 = Utilities.variable1;
	private String variable2 = Utilities.variable2;

	public static int getPing(Player player) {
		return ((CraftPlayer) player).getHandle().ping;
	}

	@SubCommand("pingSelf")
	@PlayerOnly
	public void pingSelf(CommandSender sender) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
				string + "" + "Your ping is: " + variable2 + "" + getPing((Player) sender) + string + "ms"));
	}

	@SubCommand("pingOthers")
	@Permission(Permissions.PingCommandOther)
	public void pingOthers(CommandSender sender, Player target) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', variable1 + "" + VaultCoreAPI.getName(target)
				+ string + "" + "'s ping is: " + variable2 + "" + getPing(target) + string + "ms"));
	}
}