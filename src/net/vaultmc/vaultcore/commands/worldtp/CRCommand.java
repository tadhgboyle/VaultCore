package net.vaultmc.vaultcore.commands.worldtp;

import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
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
	String string = Utilities.string;
	String variable1 = Utilities.variable1;

	public CRCommand() {
		register("cr", Collections.emptyList());
	}

    @SubCommand("cr")
    public void cr(VLPlayer player) {
        Location cr = player.getDataConfig().getLocation("locations.cr");
        if (cr == null) {
            player.sendMessage(string + "You have never joined this world before... Bringing you to spawn.");
            player.teleport(Bukkit.getWorld("creative").getSpawnLocation());
        } else {
            player.teleport(cr);
            player.sendMessage(string + "Teleported you to the " + variable1 + "Creative" + string + " world.");
        }
    }
}