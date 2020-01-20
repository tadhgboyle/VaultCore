package net.vaultmc.vaultcore.commands;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

@RootCommand(literal = "wild", description = "Teleport to a random location.")
@Permission(Permissions.WildTeleport)
@PlayerOnly
public class WildTeleport extends CommandExecutor {

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
			player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.wild.teleported"),
					String.valueOf(newLocation.distance(originalLocation))));
		} else {
			player.sendMessage(VaultLoader.getMessage("vaultcore.commands.wild.wrong_world"));
		}
	}
}