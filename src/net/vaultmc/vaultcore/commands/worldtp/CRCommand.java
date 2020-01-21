package net.vaultmc.vaultcore.commands.worldtp;

import java.util.Collections;

import org.bukkit.Bukkit;
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

@RootCommand(literal = "cr", description = "Teleport to the Creative world.")
@Permission(Permissions.WorldTPCommandCreative)
@PlayerOnly
public class CRCommand extends CommandExecutor {

	public CRCommand() {
		register("cr", Collections.emptyList());
	}

	@SubCommand("cr")
	public void cr(VLPlayer player) {
		Location cr = player.getDataConfig().getLocation("locations.cr");
		if (cr == null) {
			player.sendMessage(VaultLoader.getMessage("vaultcore.commands.worldtp.never_joined_before"));
			player.teleport(Bukkit.getWorld("creative").getSpawnLocation());
		} else {
			player.teleport(cr);
			player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaulcore.commands.worldtp.teleported"),
					"Creative"));
		}
	}
}