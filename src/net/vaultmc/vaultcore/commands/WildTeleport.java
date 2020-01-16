package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

@RootCommand(literal = "wild", description = "Teleport to a random location.")
@Permission(Permissions.WildTeleport)
@PlayerOnly
public class WildTeleport extends CommandExecutor {
	private String string = Utilities.string;
	private String variable1 = Utilities.variable1;
	private String variable2 = Utilities.variable2;

	public WildTeleport() {
		register("wild", Collections.emptyList());
	}

	@SubCommand("wild")
	public void wild(VLPlayer player) {
		if (player.getWorld().getName().equalsIgnoreCase("Survival")
				|| player.getWorld().getName().equalsIgnoreCase("clans")) {

			Location originalLocation = player.getLocation().clone();
			int x = ThreadLocalRandom.current().nextInt(-100000, 100000);
			int z = ThreadLocalRandom.current().nextInt(-100000, 100000);
			int y = player.getWorld().getHighestBlockYAt(x, z);

			Location newLocation = new Location(player.getWorld(), x, y + 1, z);
			player.teleport(newLocation);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have been teleported "
					+ variable2 + newLocation.distance(originalLocation) + string + " blocks away!"));
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You must be in the " + variable1
					+ "Survival" + string + " or " + variable1 + "Clans" + string + " world to run this command."));
		}
	}
}