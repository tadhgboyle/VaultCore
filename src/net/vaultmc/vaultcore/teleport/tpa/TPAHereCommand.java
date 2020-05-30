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

package net.vaultmc.vaultcore.teleport.tpa;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.HashMap;

@RootCommand(
        literal = "tpahere",
        description = "Request a player to teleport to you."
)
@Permission(Permissions.TPAHereCommand)
@PlayerOnly
public class TPAHereCommand extends CommandExecutor implements Listener {

    @Getter
    public static HashMap<VLPlayer, VLPlayer> tpaRequestsHere = new HashMap<>();

    public TPAHereCommand() {
        register("tpahere", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    @SubCommand("tpahere")
    public void tpahere(VLPlayer sender, VLPlayer target) {
        // Check ignore, disabled tpa etc
        if (TPACommand.verifyRequest(sender, target)) return;
        // Check if either has a pending request
        if (TPACommand.checkMap(sender, target, tpaRequestsHere)) return;
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpahere.request_sent"), target.getFormattedName()));
        target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpahere.request_received"), sender.getFormattedName()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        tpaRequestsHere.remove(VLPlayer.getPlayer(e.getPlayer()));
    }
}