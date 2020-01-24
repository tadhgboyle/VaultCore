/*
 * VaultUtils: VaultMC functionalities provider.
 * Copyright (C) 2020 yangyang200
 *
 * VaultUtils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VaultUtils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VaultCore.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.vaultmc.vaultcore.staff.punishments.kick;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.staff.punishments.PunishmentsDB;
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
        PunishmentsDB.registerData("kicks", new PunishmentsDB.PunishmentData(kicked.getUniqueId().toString(), false, reason, -1, sender.getName()));

        kicked.kick(VaultLoader.getMessage("punishments.kick.disconnect")
                .replace("{ACTOR}", sender.getName())
                .replace("{REASON}", reason));

        sender.sendMessage(VaultLoader.getMessage("punishments.kick.sent").replace("{PLAYER}", kicked.getFormattedName()));

        if (silent) {
            for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                if (player.hasPermission("vaultutils.silentnotify")) {
                    player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                            VaultLoader.getMessage("punishments.kick.announcement")
                                    .replace("{ACTOR}", sender.getName())
                                    .replace("{REASON}", reason)
                                    .replace("{PLAYER}", kicked.getFormattedName()));
                }
            }
        } else {
            for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                player.sendMessage(VaultLoader.getMessage("punishments.kick.announcement")
                        .replace("{ACTOR}", sender.getName())
                        .replace("{REASON}", reason)
                        .replace("{PLAYER}", kicked.getFormattedName()));
            }
        }
    }
}
