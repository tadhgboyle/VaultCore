package net.vaultmc.vaultcore.commands;

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
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

@RootCommand(literal = "ping", description = "Check the ping of yourself or other players.")
@Permission(Permissions.PingCommand)
public class PingCommand extends CommandExecutor {

	public PingCommand() {
		register("pingSelf", Collections.emptyList());
		register("pingOthers",
				Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
	}

	@SubCommand("pingSelf")
	@PlayerOnly
	public void pingSelf(VLPlayer player) {
		player.sendMessage(
				Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.ping.self"), player.getPing() + ""));
	}

	@SubCommand("pingOthers")
	@Permission(Permissions.PingCommandOther)
	public void pingOthers(VLCommandSender sender, VLPlayer target) {
		sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.ping.self"),
				target.getFormattedName(), target.getPing() + ""));
	}
}