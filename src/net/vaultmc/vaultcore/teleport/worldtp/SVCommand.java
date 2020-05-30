/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

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
    private static final Location svLoc = new Location(Bukkit.getWorld("Survival"), -1122.5, 63, -673.5, 0F, 0F);

    public SVCommand() {
        register("sv", Collections.emptyList());
    }

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