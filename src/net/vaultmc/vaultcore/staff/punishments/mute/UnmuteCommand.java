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

package net.vaultmc.vaultcore.staff.punishments.mute;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.staff.punishments.PunishmentsDB;
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
@Permission(Permissions.UnmuteCommand)
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
                if (player.hasPermission("vaultutils.silentnotify")) {
                    player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                            VaultLoader.getMessage("punishments.unmute.announcement")
                                    .replace("{ACTOR}", actor.getName())
                                    .replace("{PLAYER}", victim.getFormattedName()));
                }
            }
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(
                        VaultLoader.getMessage("punishments.unmute.announcement")
                                .replace("{ACTOR}", actor.getName())
                                .replace("{PLAYER}", victim.getFormattedName()));
            }
        }
    }
}
