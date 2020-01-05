package net.vaultmc.vaultcore.commands.warp;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.commands.*;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(literal = "warp", description = "Teleport to a warp.")
@Permission(Permissions.WarpCommand)
@PlayerOnly
public class WarpCommand extends CommandExecutor {
	private String string = Utilities.string;
	private String variable1 = Utilities.variable1;

	public WarpCommand() {
		register("warp", Collections.singletonList(Arguments.createArgument("warp", Arguments.word())), "vaultcore");
	}

	@SubCommand("warp")
	public void warp(CommandSender sender, String warp) {
		if (VaultCore.getInstance().getConfig().get("warps." + warp) == null) {
			sender.sendMessage(string + "The warp " + variable1 + warp + string + " does not exist!");
		} else {
			Location location = (Location) VaultCore.getInstance().getConfig().get("warps." + warp);
			((Player) sender).teleport(location);
			sender.sendMessage(string + "You have been teleported to " + variable1 + warp + string + "!");
		}
	}
}