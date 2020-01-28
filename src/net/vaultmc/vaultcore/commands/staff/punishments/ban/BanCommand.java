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

package net.vaultmc.vaultcore.commands.staff.punishments.ban;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.commands.staff.punishments.PunishmentsDB;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.commands.wrappers.WrappedSuggestion;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RootCommand(
        literal = "ban",
        description = "Disallows a player from joining the server permanently."
)
@Permission(Permissions.BanCommand)
public class BanCommand extends CommandExecutor {
    public BanCommand() {
        unregisterExisting();
        register("banNoReason", Collections.singletonList(Arguments.createArgument("player", Arguments.offlinePlayerArgument())));
        register("banSilent", Arrays.asList(
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("silent", Arguments.boolArgument())
        ));
        register("banSilentReason", Arrays.asList(
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("silent", Arguments.boolArgument()),
                Arguments.createArgument("reason", Arguments.greedyString())
        ));
    }

    @TabCompleter(
            subCommand = "banNoReason|banSilent|banSilentReason",
            argument = "player"
    )
    public List<WrappedSuggestion> suggestPlayers(VLCommandSender sender, String remaining) {
        return Bukkit.getOnlinePlayers().stream().map(player -> new WrappedSuggestion(player.getName())).collect(Collectors.toList());
    }

    private void banPlayer(VLCommandSender actor, VLOfflinePlayer victim, String reason, boolean silent) {
        if (Bukkit.getPlayer(victim.getUniqueId()) != null) {
            Bukkit.getPlayer(victim.getUniqueId()).kickPlayer(VaultLoader.getMessage("punishments.ban.disconnect")
                    .replace("{ACTOR}", actor.getFormattedName())
                    .replace("{REASON}", reason));
        }

        PunishmentsDB.registerData("bans", new PunishmentsDB.PunishmentData(victim.getUniqueId().toString(),
                true, reason, -1, actor.getFormattedName()));

        actor.sendMessage(VaultLoader.getMessage("punishments.ban.sent").replace("{PLAYER}", victim.getFormattedName()));

        if (silent) {
            for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                if (player.hasPermission(Permissions.PunishmentNotify)) {
                    player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                            VaultLoader.getMessage("punishments.ban.announcement")
                                    .replace("{ACTOR}", actor.getFormattedName())
                                    .replace("{REASON}", reason)
                                    .replace("{PLAYER}", victim.getFormattedName()));
                }
            }
        } else {
            for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                player.sendMessage(
                        VaultLoader.getMessage("punishments.ban.announcement")
                                .replace("{ACTOR}", actor.getFormattedName())
                                .replace("{REASON}", reason)
                                .replace("{PLAYER}", victim.getFormattedName()));
            }
        }
    }

    @SubCommand("banNoReason")
    public void banNoReason(VLCommandSender sender, VLOfflinePlayer victim) {
        String reason = VaultLoader.getMessage("punishments.default-reason");
        banPlayer(sender, victim, reason, false);
    }

    @SubCommand("banSilent")
    public void banSilent(VLCommandSender sender, VLOfflinePlayer victim, boolean silent) {
        String reason = VaultLoader.getMessage("punishments.default-reason");
        banPlayer(sender, victim, reason, silent);
    }

    @SubCommand("banSilentReason")
    public void banSilentReason(VLCommandSender sender, VLOfflinePlayer victim, boolean silent, String reason) {
        banPlayer(sender, victim, reason, silent);
    }
}
