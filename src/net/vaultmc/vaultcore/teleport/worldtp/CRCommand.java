package net.vaultmc.vaultcore.teleport.worldtp;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Collections;

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
            player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.worldtp.teleported"),
                    "Creative"));
        }
    }
}