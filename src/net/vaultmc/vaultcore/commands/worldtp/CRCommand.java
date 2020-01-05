package net.vaultmc.vaultcore.commands.worldtp;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

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