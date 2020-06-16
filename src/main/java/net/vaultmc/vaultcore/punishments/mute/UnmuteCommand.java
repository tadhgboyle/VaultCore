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

package net.vaultmc.vaultcore.punishments.mute;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.punishments.PunishmentsDB;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

@RootCommand(
        literal = "unmute",
        description = "Re-allows a muted player to chat."
)
@Permission(Permissions.MuteCommand)
public class UnmuteCommand extends CommandExecutor {
    public UnmuteCommand() {
        register("unmute", Collections.singletonList(Arguments.createArgument("player", Arguments.offlinePlayerArgument())));
        register("unmuteSilent", Arrays.asList(
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("silent", Arguments.boolArgument())
        ));
    }

    @SubCommand("unmute")
    public void unmute(VLCommandSender sender, VLOfflinePlayer victim) {
        unmutePlayer(sender, victim, false);
    }

    @SubCommand("unmuteSilent")
    public void unmuteSilent(VLCommandSender sender, VLOfflinePlayer victim, boolean silent) {
        unmutePlayer(sender, victim, silent);
    }

    private void unmutePlayer(VLCommandSender actor, VLOfflinePlayer victim, boolean silent) {
        PunishmentsDB.unregisterData("mutes", victim.getUniqueId().toString());
        PunishmentsDB.unregisterData("tempmutes", victim.getUniqueId().toString());

        actor.sendMessage(VaultLoader.getMessage("punishments.unmute.sent").replace("{PLAYER}", victim.getFormattedName()));

        if (silent) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission(Permissions.PunishmentSilentOverride)) {
                    player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                            VaultLoader.getMessage("punishments.unmute.announcement")
                                    .replace("{ACTOR}", actor.getFormattedName())
                                    .replace("{PLAYER}", victim.getFormattedName()));
                }
            }
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(
                        VaultLoader.getMessage("punishments.unmute.announcement")
                                .replace("{ACTOR}", actor.getFormattedName())
                                .replace("{PLAYER}", victim.getFormattedName()));
            }
        }
    }
}
