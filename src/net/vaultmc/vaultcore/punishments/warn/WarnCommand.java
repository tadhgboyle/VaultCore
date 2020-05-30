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

package net.vaultmc.vaultcore.punishments.warn;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.punishments.PunishmentsDB;
import net.vaultmc.vaultcore.punishments.ban.BanCommand;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Arrays;

@RootCommand(
        literal = "warn",
        description = "Warn a user."
)
@Permission(Permissions.WarnCommand)
public class WarnCommand extends CommandExecutor {
    public WarnCommand() {
        register("warnReasonSilent", Arrays.asList(
                Arguments.createArgument("player", Arguments.offlinePlayersArgument()),
                Arguments.createArgument("silent", Arguments.boolArgument()),
                Arguments.createArgument("reason", Arguments.greedyString())
        ));
    }

    @SubCommand("warnReasonSilent")
    public void warnReasonSilent(VLCommandSender sender, VLOfflinePlayer target, boolean silent, String reason) {
        PunishmentsDB.registerData("warnings", new PunishmentsDB.PunishmentData(target.getUniqueId().toString(),
                false, reason, -1, (sender instanceof VLPlayer) ? ((VLPlayer) sender).getUniqueId() : BanCommand.console));

        target.sendOrScheduleMessageByKey("punishments.warn.message", "reason", reason, "actor", sender.getFormattedName());
        sender.sendMessage(VaultLoader.getMessage("punishments.warn.sent").replace("{PLAYER}", target.getFormattedName()));

        if (silent) {
            for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                if (player.hasPermission(Permissions.PunishmentSilentOverride)) {
                    player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                            VaultLoader.getMessage("punishments.kick.announcement")
                                    .replace("{ACTOR}", sender.getFormattedName())
                                    .replace("{REASON}", reason)
                                    .replace("{PLAYER}", target.getFormattedName()));
                }
            }
        } else {
            for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                player.sendMessage(VaultLoader.getMessage("punishments.kick.announcement")
                        .replace("{ACTOR}", sender.getFormattedName())
                        .replace("{REASON}", reason)
                        .replace("{PLAYER}", target.getFormattedName()));
            }
        }
    }
}
