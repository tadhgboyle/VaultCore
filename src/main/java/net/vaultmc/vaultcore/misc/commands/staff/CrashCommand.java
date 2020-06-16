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

package net.vaultmc.vaultcore.misc.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Particle;

import java.util.Collections;

@RootCommand(
        literal = "crash",
        description = "Useful command."
)
@Permission(Permissions.Crash)
public class CrashCommand extends CommandExecutor {
    public CrashCommand() {
        register("crash", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    @SubCommand("crash")
    public void crash(VLCommandSender sender, VLPlayer target) {
        target.getPlayer().spawnParticle(Particle.SPELL_WITCH, target.getLocation(), 1000000000);
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("useful"), target.getFormattedName()));
    }
}
