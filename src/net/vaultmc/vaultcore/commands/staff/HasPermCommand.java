package net.vaultmc.vaultcore.commands.staff;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultutils.utils.commands.experimental.*;

@RootCommand(literal = "hasperm", description = "Check if a player has a permission.")
@Permission(Permissions.HasPermCommand)
public class HasPermCommand extends CommandExecutor {

	String string = Utilities.string;
	String variable1 = Utilities.variable1;
	String variable2 = Utilities.variable2;

	public HasPermCommand() {
		unregisterExisting();
		this.register("hasperm", Arrays.asList(Arguments.createArgument("permission", Arguments.string()),
				Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
	}

	@SubCommand("hasperm")
	public void hasperm(CommandSender sender, String permission, OfflinePlayer target) {

		if (VaultCore.getPermissions().playerHas(Bukkit.getWorlds().get(0).getName(), target, permission)) {
			sender.sendMessage(variable1 + target.getName() + ChatColor.GREEN + " has " + string + "the permission "
					+ variable2 + permission + string + ".");
		} else {
			sender.sendMessage(variable1 + target.getName() + ChatColor.RED + " doesn't have " + string
					+ "the permission " + variable2 + permission + string + ".");
		}
		sender.sendMessage(ChatColor.RED + "That player has never joined!");
	}
}
