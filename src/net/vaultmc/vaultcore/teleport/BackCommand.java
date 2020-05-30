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

package net.vaultmc.vaultcore.teleport;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Location;

import java.util.Collections;
import java.util.Stack;

@RootCommand(literal = "back", description = "Teleport to your previous location.")
@Permission(Permissions.BackCommand)
@PlayerOnly
public class BackCommand extends CommandExecutor {
    public BackCommand() {
        register("back", Collections.emptyList());
    }

    @SubCommand("back")
    public void back(VLPlayer player) {
        if (PlayerTPListener.teleports.containsKey(player.getUniqueId())) {
            Stack<Location> stack = PlayerTPListener.teleports.get(player.getUniqueId());
            Location before = stack.pop();
            if (!player.hasPermission(Permissions.CooldownBypass)) player.teleportNoMove(before);
            else player.teleport(before);
            if (stack.empty()) {
                PlayerTPListener.teleports.remove(player.getUniqueId());
            }
        } else {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.back.no_teleport_location"));
        }
    }
}