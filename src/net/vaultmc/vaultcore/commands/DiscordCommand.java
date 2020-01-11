package net.vaultmc.vaultcore.commands;

import java.util.Collections;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;

@RootCommand(literal = "discord", description = "Get a link to our Discord Guild.")
@Permission(Permissions.DiscordCommand)
@PlayerOnly
public class DiscordCommand extends CommandExecutor {

	private String string = Utilities.string;
	private String variable1 = Utilities.variable1;
	private String variable2 = Utilities.variable2;

	public DiscordCommand() {
		this.register("discord", Collections.emptyList());
	}

	@SneakyThrows
	@SubCommand("discord")
	public void execute(CommandSender sender) {
		Player player = (Player) sender;

		String token = TokenCommand.getToken(player.getUniqueId(), player);

		player.sendMessage(string + "Your token: " + variable2 + token);

		player.sendMessage(
				string + "Click here to join our Discord guild: " + variable1 + "https://discord.vaultmc.net");
	}
}
