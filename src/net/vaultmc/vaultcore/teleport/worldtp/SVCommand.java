package net.vaultmc.vaultcore.teleport.worldtp;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Collections;

@RootCommand(literal = "sv", description = "Teleport to the Survival world.")
@Permission(Permissions.WorldTPCommandSurvival)
@PlayerOnly
public class SVCommand extends CommandExecutor {
    public SVCommand() {
        register("sv", Collections.emptyList());
    }

    private static final Location svLoc = new Location(Bukkit.getWorld("Survival"), -1122.5, 63, -673.5, 0F, 0F);

    @SubCommand("sv")
    public void sv(VLPlayer player) {
        Location sv = Utilities.deserializeLocation(player.getPlayerData().getString("locations.sv"));
        if (sv == null) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.worldtp.never_joined_before"));
            player.teleport(svLoc);
        } else {
            player.teleport(sv);
            player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.worldtp.teleported"),
                    "Survival"));
        }
    }
}