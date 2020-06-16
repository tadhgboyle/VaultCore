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
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RootCommand(literal = "invsee", description = "Look in a players inventory.")
@Permission(Permissions.InvseeCommand)
@PlayerOnly
@Aliases("openinv")
public class InvseeCommand extends CommandExecutor implements Listener {
    private static final Set<UUID> viewers = new HashSet<>();

    public InvseeCommand() {
        register("invsee", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
        Bukkit.getScheduler().runTaskTimer(VaultLoader.getInstance(), () -> {
            for (UUID uuid : viewers) {
                Bukkit.getPlayer(uuid).updateInventory();
            }
        }, 5, 5);
        VaultCore.getInstance().registerEvents(this);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        viewers.remove(e.getPlayer().getUniqueId());
    }

    @SubCommand("invsee")
    public void invsee(VLPlayer sender, VLPlayer target) {
        if (sender == target) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.invsee.self_error"));
            return;
        }

        if (target.hasPermission(Permissions.InvseeExempt) && !sender.hasPermission(Permissions.InvSeeAdmin)) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.invsee.excempt_error"));
            return;
        }

        sender.openInventory(target.getInventory());
    }
}