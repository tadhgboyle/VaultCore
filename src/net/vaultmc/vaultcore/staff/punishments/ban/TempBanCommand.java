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

package net.vaultmc.vaultcore.staff.punishments.ban;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.staff.punishments.PunishmentUtils;
import net.vaultmc.vaultcore.staff.punishments.PunishmentsDB;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.commands.arguments.custom.OfflinePlayerArgument;
import net.vaultmc.vaultloader.utils.commands.wrappers.WrappedSuggestion;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RootCommand(
        literal = "tempban",
        description = "Disallows a player from joining the server temporarily."
)
@Permission(Permissions.TempBanCommand)
public class TempBanCommand extends CommandExecutor {
    public TempBanCommand() {
        register("banNoReason", Arrays.asList(
                Arguments.createArgument("player", OfflinePlayerArgument.player()),
                Arguments.createArgument("time", Arguments.integerArgument(1)),
                Arguments.createArgument("unit", Arguments.word())));
        register("banSilent", Arrays.asList(
                Arguments.createArgument("player", OfflinePlayerArgument.player()),
                Arguments.createArgument("time", Arguments.integerArgument(1)),
                Arguments.createArgument("unit", Arguments.word()),
                Arguments.createArgument("silent", Arguments.boolArgument())));
        register("banSilentReason", Arrays.asList(
                Arguments.createArgument("player", OfflinePlayerArgument.player()),
                Arguments.createArgument("time", Arguments.integerArgument(1)),
                Arguments.createArgument("unit", Arguments.word()),
                Arguments.createArgument("silent", Arguments.boolArgument()),
                Arguments.createArgument("reason", Arguments.greedyString())
        ));
    }

    @TabCompleter(
            subCommand = "banNoReason|banSilent|banSilentReason",
            argument = "unit"
    )
    public List<WrappedSuggestion> suggestTimeUnits(VLCommandSender sender, String remaining) {
        return VaultCore.getInstance().getConfig().getConfigurationSection("time-units").getKeys(false)
                .stream().map(WrappedSuggestion::new).collect(Collectors.toList());
    }

    @SubCommand("banNoReason")
    public void banNoReason(VLCommandSender sender, VLOfflinePlayer victim, int time, String unit) {
        String reason = VaultLoader.getMessage("punishments.default-reason");
        if (!VaultCore.getInstance().getConfig().contains("time-units." + unit)) {
            sender.sendMessage(VaultLoader.getMessage("punishments.time-unit-error"));
            return;
        }
        long expiry = time * VaultCore.getInstance().getConfig().getLong("time-units." + unit);
        if (expiry == 0) {
            sender.sendMessage(ChatColor.RED + "Invalid punishment duration!");
            return;
        }

        tempbanPlayer(sender, victim, reason, false, expiry);
    }

    @SubCommand("banSilent")
    public void banSilent(VLCommandSender sender, VLOfflinePlayer victim, int time, String unit, boolean silent) {
        String reason = VaultLoader.getMessage("punishments.default-reason");
        if (!VaultCore.getInstance().getConfig().contains("time-units." + unit)) {
            sender.sendMessage(VaultLoader.getMessage("punishments.time-unit-error"));
            return;
        }
        long expiry = time * VaultCore.getInstance().getConfig().getLong("time-units." + unit);
        if (expiry == 0) {
            sender.sendMessage(ChatColor.RED + "Invalid punishment duration!");
            return;
        }

        tempbanPlayer(sender, victim, reason, silent, expiry);
    }

    @SubCommand("banSilentReason")
    public void banSilentReason(VLCommandSender sender, VLOfflinePlayer victim, int time, String unit, boolean silent, String reason) {
        if (!VaultCore.getInstance().getConfig().contains("time-units." + unit)) {
            sender.sendMessage(VaultLoader.getMessage("punishments.time-unit-error"));
            return;
        }
        long expiry = time * VaultCore.getInstance().getConfig().getLong("time-units." + unit);
        if (expiry == 0) {
            sender.sendMessage(ChatColor.RED + "Invalid punishment duration!");
            return;
        }

        tempbanPlayer(sender, victim, reason, silent, expiry);
    }

    private void tempbanPlayer(VLCommandSender actor, VLOfflinePlayer victim, String reason, boolean silent, long expiry) {
        // FileConfiguration data = Main.getInstance().getData();
        // expiry *= 1000;

        if (Bukkit.getPlayer(victim.getUniqueId()) != null) {
            Bukkit.getPlayer(victim.getUniqueId()).kickPlayer(VaultLoader.getMessage("punishments.tempban.disconnect")
                    .replace("{ACTOR}", actor.getName())
                    .replace("{REASON}", reason)
                    .replace("{EXPIRY}", PunishmentUtils.humanReadableTime(expiry)));
        }

        PunishmentsDB.registerData("tempbans", new PunishmentsDB.PunishmentData(victim.getUniqueId().toString(),
                true, reason, PunishmentUtils.currentTime() + expiry, actor.getName()));

        /*
        data.set("vaultutils." + victim.getUniqueId().toString() + ".tempban.status", true);
        data.set("vaultutils." + victim.getUniqueId().toString() + ".tempban.reason", reason);
        data.set("vaultutils." + victim.getUniqueId().toString() + ".tempban.expire", PunishmentUtils.currentTime() + expiry);
        data.set("vaultutils." + victim.getUniqueId().toString() + ".tempban.actor", actor.getName());  // Intended
        Main.getInstance().saveConfig();
         */
        actor.sendMessage(VaultLoader.getMessage("punishments.tempban.sent").replace("{PLAYER}", victim.getFormattedName()));

        if (silent) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("vaultutils.silentnotify")) {
                    player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                            VaultLoader.getMessage("punishments.tempban.announcement")
                                    .replace("{ACTOR}", actor.getName())
                                    .replace("{REASON}", reason)
                                    .replace("{PLAYER}", victim.getFormattedName())
                                    .replace("{EXPIRY}", PunishmentUtils.humanReadableTime(expiry)));
                }
            }
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(
                        VaultLoader.getMessage("punishments.tempban.announcement")
                                .replace("{ACTOR}", actor.getName())
                                .replace("{REASON}", reason)
                                .replace("{PLAYER}", victim.getFormattedName())
                                .replace("{EXPIRY}", PunishmentUtils.humanReadableTime(expiry)));
            }
        }
    }
}
