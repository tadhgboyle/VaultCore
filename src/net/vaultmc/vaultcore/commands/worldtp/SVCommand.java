package net.vaultmc.vaultcore.commands.worldtp;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Collections;

@RootCommand(literal = "sv", description = "Teleport to the Survival world.")
@Permission(Permissions.WorldTPCommandSurvival)
@PlayerOnly
public class SVCommand extends CommandExecutor {
	String string = Utilities.string;
	String variable1 = Utilities.variable1;

	public SVCommand() {
		register("sv", Collections.emptyList());
	}

    @SubCommand("sv")
    public void sv(VLPlayer player) {
        Location sv = VaultCore.getInstance().getLocationFile().getLocation("players." + player.getUniqueId() + ".sv");
        if (sv == null) {
            player.sendMessage(string + "You have never joined this world before... Bringing you to spawn.");
            player.teleport(Bukkit.getWorld("Survival").getSpawnLocation());
        } else {
            player.teleport(sv);
            player.sendMessage(string + "Teleported you to the " + variable1 + "Survival" + string + " world.");
        }
    }
}