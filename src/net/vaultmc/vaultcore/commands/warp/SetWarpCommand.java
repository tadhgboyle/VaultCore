package net.vaultmc.vaultcore.commands.warp;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.commands.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(literal = "setwarp", description = "Set a warp.")
@Permission(Permissions.WarpCommandSet)
@PlayerOnly
public class SetWarpCommand extends CommandExecutor {
	private String string = Utilities.string;
	private String variable1 = Utilities.variable1;

	public SetWarpCommand() {
		register("setwarp", Collections.singletonList(Arguments.createArgument("warp", Arguments.word())), "vaultcore");
	}

	@SubCommand("setwarp")
	public void setWarp(CommandSender sender, String warp) {
		VaultCore.getInstance().getLocationFile().set("warps." + warp, ((Player) sender).getLocation());
		VaultCore.getInstance().saveLocations();
		sender.sendMessage(string + "Warp " + variable1 + warp + string + " has been set!");
	}
}