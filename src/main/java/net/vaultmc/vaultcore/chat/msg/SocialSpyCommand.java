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

package net.vaultmc.vaultcore.chat.msg;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RootCommand(literal = "socialspy", description = "View messages players are sending.")
@Permission(Permissions.SocialSpyCommand)
@PlayerOnly
public class SocialSpyCommand extends CommandExecutor implements Listener {
    public static final Set<UUID> toggled = new HashSet<>();

    public SocialSpyCommand() {
        register("toggle", Collections.emptyList());
        VaultCore.getInstance().registerEvents(this);
    }

    public static void sendSS(String type, VLPlayer sender, VLOfflinePlayer target, String message) {
        for (VLPlayer players : VLPlayer.getOnlinePlayers()) {
            if (sender == players || target == players) continue;
            if (players.hasPermission(Permissions.SocialSpyCommand)) {
                players.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.socialspy.format"), type, sender.getFormattedName(), target.getFormattedName(), message));
            }
        }
    }

    @SubCommand("toggle")
    public void toggle(VLPlayer player) {
        if (toggled.contains(player.getUniqueId())) {
            toggled.remove(player.getUniqueId());
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.socialspy.toggle"), "off"));
        } else {
            toggled.add(player.getUniqueId());
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.socialspy.toggle"), "on"));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        toggled.remove(e.getPlayer().getUniqueId());
    }
}
