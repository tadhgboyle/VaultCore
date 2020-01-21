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

@RootCommand(literal = "sv", description = "Teleport to the Survival world.")
@Permission(Permissions.WorldTPCommandSurvival)
@PlayerOnly
public class SVCommand extends CommandExecutor {

	public SVCommand() {
		register("sv", Collections.emptyList());
	}

    @SubCommand("sv")
    public void sv(VLPlayer player) {
        Location sv = player.getDataConfig().getLocation("locations.sv");
        if (sv == null) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.worldtp.never_joined_before"));
            player.teleport(Bukkit.getWorld("Survival").getSpawnLocation());
        } else {
            player.teleport(sv);
			player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaulcore.commands.worldtp.teleported"),
					"Survival"));
        }
    }
}