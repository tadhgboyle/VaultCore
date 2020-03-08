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

package net.vaultmc.vaultcore.punishments.ban;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.punishments.PunishmentUtils;
import net.vaultmc.vaultcore.punishments.PunishmentsDB;
import net.vaultmc.vaultcore.punishments.ReasonSelector;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.commands.wrappers.WrappedSuggestion;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RootCommand(
        literal = "ban",
        description = "Disallows a player from joining the server permanently."
)
@Permission(Permissions.BanCommand)
@Aliases({"tempban", "tempmute", "kick", "mute"})
public class BanCommand extends CommandExecutor {
    public BanCommand() {
        unregisterExisting();
        register("ban", Collections.singletonList(Arguments.createArgument("player", Arguments.offlinePlayerArgument())));
        register("console", Arrays.asList(
                Arguments.createLiteral("console"),
                Arguments.createArgument("player", Arguments.playerArgument())
        ));
        register("console1", Arrays.asList(
                Arguments.createLiteral("console1"),
                Arguments.createArgument("player", Arguments.playerArgument())
        ));
    }

    @SubCommand("console")
    public void console(VLCommandSender sender, VLPlayer victim) {
        if (sender instanceof VLPlayer) return;
        victim.kick(VaultLoader.getMessage("punishments.tempban.disconnect")
                .replace("{ACTOR}", sender.getFormattedName())
                .replace("{REASON}", "VAULTMC ANTI CHEAT (Please do not appeal)")
                .replace("{EXPIRY}", PunishmentUtils.humanReadableTime(604800)));
        PunishmentsDB.registerData("tempbans", new PunishmentsDB.PunishmentData(victim.getUniqueId().toString(),
                true, "VAULTMC ANTI CHEAT (Please do not appeal)",
                PunishmentUtils.currentTime() + 604800, UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670")));

        sender.sendMessage(VaultLoader.getMessage("punishments.tempban.sent").replace("{PLAYER}", victim.getFormattedName()));

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(Permissions.PunishmentNotify)) {
                player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                        VaultLoader.getMessage("punishments.tempban.announcement")
                                .replace("{ACTOR}", sender.getFormattedName())
                                .replace("{REASON}", "VAULTMC ANTI CHEAT (Please do not appeal)")
                                .replace("{PLAYER}", victim.getFormattedName())
                                .replace("{EXPIRY}", PunishmentUtils.humanReadableTime(604800)));
            }
        }
    }

    @SubCommand("console1")
    public void console1(VLCommandSender sender, VLPlayer victim) {
        if (sender instanceof VLPlayer) return;
        PunishmentsDB.registerData("kicks", new PunishmentsDB.PunishmentData(victim.getUniqueId().toString(), false,
                "VAULTMC ANTI CHEAT", -1, UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670")));

        victim.getOnlinePlayer().kick(VaultLoader.getMessage("punishments.kick.disconnect")
                .replace("{ACTOR}", sender.getFormattedName())
                .replace("{REASON}", "VAULTMC ANTI CHEAT"));

        sender.sendMessage(VaultLoader.getMessage("punishments.kick.sent").replace("{PLAYER}", victim.getFormattedName()));

        for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
            if (player.hasPermission("vaultutils.silentnotify")) {
                player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                        VaultLoader.getMessage("punishments.kick.announcement")
                                .replace("{ACTOR}", sender.getFormattedName())
                                .replace("{REASON}", "VAULTMC ANTI CHEAT")
                                .replace("{PLAYER}", victim.getFormattedName()));
            }
        }
    }

    @TabCompleter(
            subCommand = "ban",
            argument = "player"
    )
    public List<WrappedSuggestion> suggestPlayers(VLCommandSender sender, String remaining) {
        return Bukkit.getOnlinePlayers().stream().map(player -> new WrappedSuggestion(player.getName())).collect(Collectors.toList());
    }

    @SubCommand("ban")
    @PlayerOnly
    public void ban(VLPlayer sender, VLOfflinePlayer victim) {
        ReasonSelector.start(sender, victim, (reason, expiry, type) -> {
            if (type == ReasonSelector.Type.BAN) {
                if (expiry == -1L) {
                    if (victim.isOnline()) {
                        victim.getOnlinePlayer().kick(VaultLoader.getMessage("punishments.ban.disconnect")
                                .replace("{ACTOR}", sender.getFormattedName())
                                .replace("{REASON}", reason));
                    }

                    PunishmentsDB.registerData("bans", new PunishmentsDB.PunishmentData(victim.getUniqueId().toString(),
                            true, reason, -1, sender.getUniqueId()));

                    sender.sendMessage(VaultLoader.getMessage("punishments.ban.sent").replace("{PLAYER}", victim.getFormattedName()));

                    for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                        if (player.hasPermission(Permissions.PunishmentNotify)) {
                            player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                                    VaultLoader.getMessage("punishments.ban.announcement")
                                            .replace("{ACTOR}", sender.getFormattedName())
                                            .replace("{REASON}", reason)
                                            .replace("{PLAYER}", victim.getFormattedName()));
                        }
                    }
                } else {
                    if (victim.isOnline()) {
                        victim.getOnlinePlayer().kick(VaultLoader.getMessage("punishments.tempban.disconnect")
                                .replace("{ACTOR}", sender.getFormattedName())
                                .replace("{REASON}", reason)
                                .replace("{EXPIRY}", PunishmentUtils.humanReadableTime(expiry)));
                    }

                    PunishmentsDB.registerData("tempbans", new PunishmentsDB.PunishmentData(victim.getUniqueId().toString(),
                            true, reason, PunishmentUtils.currentTime() + expiry, sender.getUniqueId()));

                    sender.sendMessage(VaultLoader.getMessage("punishments.tempban.sent").replace("{PLAYER}", victim.getFormattedName()));

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission(Permissions.PunishmentNotify)) {
                            player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                                    VaultLoader.getMessage("punishments.tempban.announcement")
                                            .replace("{ACTOR}", sender.getFormattedName())
                                            .replace("{REASON}", reason)
                                            .replace("{PLAYER}", victim.getFormattedName())
                                            .replace("{EXPIRY}", PunishmentUtils.humanReadableTime(expiry)));
                        }
                    }
                }
            } else if (type == ReasonSelector.Type.KICK) {
                if (!victim.isOnline()) {
                    sender.sendMessage(ChatColor.RED + "No player was found");
                    return;
                }

                PunishmentsDB.registerData("kicks", new PunishmentsDB.PunishmentData(victim.getUniqueId().toString(), false, reason, -1,
                        sender.getUniqueId()));

                victim.getOnlinePlayer().kick(VaultLoader.getMessage("punishments.kick.disconnect")
                        .replace("{ACTOR}", sender.getFormattedName())
                        .replace("{REASON}", reason));

                sender.sendMessage(VaultLoader.getMessage("punishments.kick.sent").replace("{PLAYER}", victim.getFormattedName()));

                for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                    if (player.hasPermission("vaultutils.silentnotify")) {
                        player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                                VaultLoader.getMessage("punishments.kick.announcement")
                                        .replace("{ACTOR}", sender.getFormattedName())
                                        .replace("{REASON}", reason)
                                        .replace("{PLAYER}", victim.getFormattedName()));
                    }
                }
            } else if (type == ReasonSelector.Type.MUTE) {
                if (expiry == -1L) {
                    if (victim.isOnline()) {
                        victim.getOnlinePlayer().sendMessage(VaultLoader.getMessage("punishments.mute.message")
                                .replace("{ACTOR}", sender.getFormattedName())
                                .replace("{REASON}", reason));
                    }

                    PunishmentsDB.registerData("mutes", new PunishmentsDB.PunishmentData(victim.getUniqueId().toString(), true, reason, -1,
                            sender.getUniqueId()));

                    sender.sendMessage(VaultLoader.getMessage("punishments.mute.sent").replace("{PLAYER}", victim.getFormattedName()));

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission(Permissions.PunishmentNotify)) {
                            player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                                    VaultLoader.getMessage("punishments.mute.announcement")
                                            .replace("{ACTOR}", sender.getFormattedName())
                                            .replace("{PLAYER}", victim.getFormattedName())
                                            .replace("{REASON}", reason));
                        }
                    }
                } else {
                    if (victim.isOnline()) {
                        victim.getOnlinePlayer().sendMessage(VaultLoader.getMessage("punishments.tempmute.message")
                                .replace("{ACTOR}", sender.getFormattedName())
                                .replace("{REASON}", reason)
                                .replace("{PLAYER}", victim.getFormattedName())
                                .replace("{EXPIRY}", PunishmentUtils.humanReadableTime(expiry)));
                    }

                    PunishmentsDB.registerData("tempmutes", new PunishmentsDB.PunishmentData(victim.getUniqueId().toString(), true, reason,
                            PunishmentUtils.currentTime() + expiry, sender.getUniqueId()));

                    sender.sendMessage(VaultLoader.getMessage("punishments.tempmute.sent").replace("{PLAYER}", victim.getFormattedName()));

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission("vaultutils.silentnotify")) {
                            player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                                    VaultLoader.getMessage("punishments.tempmute.announcement")
                                            .replace("{ACTOR}", sender.getFormattedName())
                                            .replace("{REASON}", reason)
                                            .replace("{PLAYER}", victim.getFormattedName())
                                            .replace("{EXPIRY}", PunishmentUtils.humanReadableTime(expiry)));
                        }
                    }
                }
            }
        });
    }
}
