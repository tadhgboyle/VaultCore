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

package net.vaultmc.vaultcore.misc.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

@RootCommand(literal = "near", description = "Find out who is near you.")
@Permission(Permissions.NearCommand)
public class NearCommand extends CommandExecutor {
    public NearCommand() {
        this.unregisterExisting();
        register("near", Collections.emptyList());
        register("nearRadius", Collections.singletonList(Arguments.createArgument("near", Arguments.integerArgument())));
    }

    @SubCommand("near")
    public void near(VLPlayer sender) {
        findNear(sender, 10);
    }

    @SubCommand("nearRadius")
    public void nearRadius(VLPlayer sender, int radius) {
        findNear(sender, radius);
    }

    private void findNear(VLPlayer sender, int radius) {
        Collection<String> nearPlayers = new TreeSet<>();
        for (Player players : sender.getLocation().getNearbyPlayers(radius)) {
            if (VLPlayer.getPlayer(players) == sender) continue;
            nearPlayers.add(VLPlayer.getPlayer(players).getFormattedName());
        }
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.near.main"), nearPlayers.size(), radius));
        if (nearPlayers.size() > 0)
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.near.list"), Utilities.listToString(nearPlayers, true)));
    }
}
