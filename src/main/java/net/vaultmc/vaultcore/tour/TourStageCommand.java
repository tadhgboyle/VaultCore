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

package net.vaultmc.vaultcore.tour;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

@RootCommand(
        literal = "tourstage"
)
@Permission(Permissions.Tour)
@PlayerOnly
public class TourStageCommand extends CommandExecutor {
    public TourStageCommand() {
        register("tourStage", Collections.singletonList(Arguments.createArgument("method",
                Arguments.word())));
    }

    @SubCommand("tourStage")
    public void tourStage(VLPlayer sender, String stage) {
        if (!Tour.getTouringPlayers().contains(sender.getUniqueId())) {
            sender.sendMessage(VaultLoader.getMessage("you-shouldnt-do-this"));
            return;
        }
        try {
            Tour.class.getDeclaredMethod(stage, VLPlayer.class).invoke(null, sender);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            sender.sendMessage(VaultLoader.getMessage("tour.invalid_section"));
        }
    }
}
