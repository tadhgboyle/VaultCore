package net.vaultmc.vaultcore.commands.warp;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.commands.*;
import org.bukkit.command.CommandSender;

import java.util.Collections;

@RootCommand(literal = "delwarp", description = "Delete a warp.")
@Permission(Permissions.WarpCommandDelete)
@PlayerOnly
public class DelWarpCommand extends CommandExecutor {
	private String string = Utilities.string;
	private String variable1 = Utilities.variable1;

	public DelWarpCommand() {
		register("delwarp", Collections.singletonList(Arguments.createArgument("warp", Arguments.word())), "vaultcore");
	}

	@SubCommand("delwarp")
	public void delWarp(CommandSender sender, String warp) {
		if (VaultCore.getInstance().getLocationFile().get("warps." + warp) == null) {
			sender.sendMessage(string + "The warp " + variable1 + warp + string + " does not exist!");
			return;
		}

		VaultCore.getInstance().getLocationFile().set("warps." + warp, null);
		VaultCore.getInstance().saveLocations();
		sender.sendMessage(string + "Warp " + variable1 + warp + string + " has been deleted!");
	}
}