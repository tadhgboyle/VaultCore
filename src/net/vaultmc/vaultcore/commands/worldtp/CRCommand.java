package net.vaultmc.vaultcore.commands.worldtp;

import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultutils.utils.commands.experimental.CommandExecutor;
import net.vaultmc.vaultutils.utils.commands.experimental.Permission;
import net.vaultmc.vaultutils.utils.commands.experimental.PlayerOnly;
import net.vaultmc.vaultutils.utils.commands.experimental.RootCommand;
import net.vaultmc.vaultutils.utils.commands.experimental.SubCommand;

@RootCommand(literal = "cr", description = "Teleport to the Creative world.")
@Permission(Permissions.WorldTPCommandCreative)
@PlayerOnly
public class CRCommand extends CommandExecutor {

	String string = Utilities.string;
	String variable1 = Utilities.variable1;

	public CRCommand() {
		register("cr", Collections.emptyList());
	}

	@SubCommand("cr")
	public void cr(CommandSender sender) {

		Player player = (Player) sender;

		Location cr = VaultCore.getInstance().getPlayerData().getLocation("players." + player.getUniqueId() + ".cr");
		if (cr == null) {
			player.sendMessage(string + "You have never joined this world before... Bringing you to spawn.");
			player.teleport(Bukkit.getWorld("creative").getSpawnLocation());
		} else {
			player.teleport(cr);
			player.sendMessage(string + "Teleported you to the " + variable1 + "Creative" + string + " world.");
		}
	}
}