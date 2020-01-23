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

package net.vaultmc.vaultcore.ported.punishments.mute;

import net.vaultmc.vaultcore.ported.punishments.PunishmentsDB;
import net.vaultmc.vaultcore.ported.punishments.ban.IpBanCommand;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

@RootCommand(
        literal = "ipmute",
        description = "Disallows a player from chatting, using signs and executing some commands permanently. (By IP)"
)
@Permission("vaultutils.mute")
public class IpMuteCommand extends CommandExecutor {
    public IpMuteCommand() {
        register("mute", Collections.singletonList(
                Arguments.createArgument("player", Arguments.offlinePlayerArgument())
        ));
        register("muteSilent", Arrays.asList(
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("silent", Arguments.boolArgument())
        ));
        register("muteReasonSilent", Arrays.asList(
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("silent", Arguments.boolArgument()),
                Arguments.createArgument("reason", Arguments.greedyString())
        ));
    }

    @SubCommand("mute")
    public void mute(VLCommandSender sender, VLOfflinePlayer victim) {
        mutePlayer(sender, victim, VaultLoader.getMessage("punishments.default-reason"), false);
    }

    @SubCommand("muteSilent")
    public void muteSilent(VLCommandSender sender, VLOfflinePlayer victim, boolean silent) {
        mutePlayer(sender, victim, VaultLoader.getMessage("punishments.default-reason"), silent);
    }

    @SubCommand("muteReasonSilent")
    public void muteReasonSilent(VLCommandSender sender, VLOfflinePlayer victim, boolean silent, String reason) {
        mutePlayer(sender, victim, reason, silent);
    }

    private void mutePlayer(VLCommandSender actor, VLOfflinePlayer victim, String reason, boolean silent) {
        if (Bukkit.getPlayer(victim.getUniqueId()) != null) {
            Bukkit.getPlayer(victim.getUniqueId()).sendMessage(VaultLoader.getMessage("punishments.mute.message")
                    .replace("{ACTOR}", actor.getName())
                    .replace("{REASON}", reason));
        }

        PunishmentsDB.registerData("ipmutes", new PunishmentsDB.PunishmentData(IpBanCommand.getPlayerIp(victim), true, reason, -1, actor.getName()));

        actor.sendMessage(VaultLoader.getMessage("punishments.mute.sent").replace("{PLAYER}", victim.getFormattedName()));

        if (silent) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("vaultutils.silentnotify")) {
                    player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                            VaultLoader.getMessage("punishments.mute.announcement")
                                    .replace("{ACTOR}", actor.getName())
                                    .replace("{PLAYER}", victim.getFormattedName())
                                    .replace("{REASON}", reason));
                }
            }
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(
                        VaultLoader.getMessage("punishments.mute.announcement")
                                .replace("{ACTOR}", actor.getName())
                                .replace("{PLAYER}", victim.getFormattedName())
                                .replace("{REASON}", reason));
            }
        }
    }
}
