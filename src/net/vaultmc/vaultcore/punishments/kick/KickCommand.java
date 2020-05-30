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

package net.vaultmc.vaultcore.punishments.kick;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.punishments.PunishmentsDB;
import net.vaultmc.vaultcore.punishments.ban.BanCommand;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Arrays;
import java.util.Collections;

@RootCommand(
        literal = "kick",
        description = "Removes a player from the server."
)
@Permission(Permissions.KickCommand)
public class KickCommand extends CommandExecutor {
    public KickCommand() {
        unregisterExisting();
        register("kick", Collections.singletonList(Arguments.createArgument("player", Arguments.playerArgument())));
        register("kickSilent", Arrays.asList(
                Arguments.createArgument("player", Arguments.playerArgument()),
                Arguments.createArgument("silent", Arguments.boolArgument())));
        register("kickReasonSilent", Arrays.asList(
                Arguments.createArgument("player", Arguments.playerArgument()),
                Arguments.createArgument("silent", Arguments.boolArgument()),
                Arguments.createArgument("reason", Arguments.greedyString())
        ));
    }

    @SubCommand("kick")
    public void kick(VLCommandSender sender, VLPlayer player) {
        kickVLPlayer(player, sender, VaultLoader.getMessage("punishments.default-reason"), false);
    }

    @SubCommand("kickSilent")
    public void kickSilent(VLCommandSender sender, VLPlayer player, boolean silent) {
        kickVLPlayer(player, sender, VaultLoader.getMessage("punishments.default-reason"), silent);
    }

    @SubCommand("kickReasonSilent")
    public void kickReasonSilent(VLCommandSender sender, VLPlayer player, boolean silent, String reason) {
        kickVLPlayer(player, sender, reason, silent);
    }

    private void kickVLPlayer(VLPlayer kicked, VLCommandSender sender, String reason, boolean silent) {
        PunishmentsDB.registerData("kicks", new PunishmentsDB.PunishmentData(kicked.getUniqueId().toString(), false, reason, -1,
                (sender instanceof VLPlayer) ? ((VLPlayer) sender).getUniqueId() : BanCommand.console));

        kicked.kick(VaultLoader.getMessage("punishments.kick.disconnect")
                .replace("{ACTOR}", sender.getFormattedName())
                .replace("{REASON}", reason));

        sender.sendMessage(VaultLoader.getMessage("punishments.kick.sent").replace("{PLAYER}", kicked.getFormattedName()));

        if (silent) {
            for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                if (player.hasPermission(Permissions.PunishmentSilentOverride)) {
                    player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                            VaultLoader.getMessage("punishments.kick.announcement")
                                    .replace("{ACTOR}", sender.getFormattedName())
                                    .replace("{REASON}", reason)
                                    .replace("{PLAYER}", kicked.getFormattedName()));
                }
            }
        } else {
            for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                player.sendMessage(VaultLoader.getMessage("punishments.kick.announcement")
                        .replace("{ACTOR}", sender.getFormattedName())
                        .replace("{REASON}", reason)
                        .replace("{PLAYER}", kicked.getFormattedName()));
            }
        }
    }
}
