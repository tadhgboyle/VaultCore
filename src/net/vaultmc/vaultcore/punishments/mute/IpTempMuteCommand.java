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
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.punishments.PunishmentsDB;
import net.vaultmc.vaultcore.punishments.ban.BanCommand;
import net.vaultmc.vaultcore.punishments.ban.IpBanCommand;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.commands.arguments.custom.OfflinePlayerArgument;
import net.vaultmc.vaultloader.utils.commands.wrappers.WrappedSuggestion;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RootCommand(
        literal = "iptempmute",
        description = "Disallows a player from chatting, using signs and executing some commands temporarily. (By IP)"
)
@Permission(Permissions.MuteCommand)
public class IpTempMuteCommand extends CommandExecutor {
    public IpTempMuteCommand() {
        register("mute", Arrays.asList(
                Arguments.createArgument("player", OfflinePlayerArgument.player()),
                Arguments.createArgument("time", Arguments.integerArgument(1)),
                Arguments.createArgument("unit", Arguments.word())));
        register("muteSilent", Arrays.asList(
                Arguments.createArgument("player", OfflinePlayerArgument.player()),
                Arguments.createArgument("time", Arguments.integerArgument(1)),
                Arguments.createArgument("unit", Arguments.word()),
                Arguments.createArgument("silent", Arguments.boolArgument())));
        register("muteSilentReason", Arrays.asList(
                Arguments.createArgument("player", OfflinePlayerArgument.player()),
                Arguments.createArgument("time", Arguments.integerArgument(1)),
                Arguments.createArgument("unit", Arguments.word()),
                Arguments.createArgument("silent", Arguments.boolArgument()),
                Arguments.createArgument("reason", Arguments.greedyString())
        ));
    }

    @TabCompleter(
            subCommand = "mute|muteSilent|muteSilentReason",
            argument = "unit"
    )
    public List<WrappedSuggestion> suggestTimeUnits(VLCommandSender sender, String remaining) {
        return VaultCore.getInstance().getConfig().getConfigurationSection("time-units").getKeys(false)
                .stream().map(WrappedSuggestion::new).collect(Collectors.toList());
    }

    @SubCommand("mute")
    public void mute(VLCommandSender sender, VLOfflinePlayer victim, int time, String unit) {
        if (!VaultCore.getInstance().getConfig().contains("time-units." + unit)) {
            sender.sendMessage(VaultLoader.getMessage("punishments.time-unit-error"));
            return;
        }
        long expiry = time * VaultCore.getInstance().getConfig().getLong("time-units." + unit);
        if (expiry == 0) {
            sender.sendMessage(ChatColor.RED + "Invalid punishment duration!");
            return;
        }
        tempmutePlayer(sender, victim, VaultLoader.getMessage("punishments.default-reason"),
                false, expiry);
    }

    @SubCommand("muteSilent")
    public void muteSilent(VLCommandSender sender, VLOfflinePlayer victim, int time, String unit, boolean silent) {
        if (!VaultCore.getInstance().getConfig().contains("time-units." + unit)) {
            sender.sendMessage(VaultLoader.getMessage("punishments.time-unit-error"));
            return;
        }
        long expiry = time * VaultCore.getInstance().getConfig().getLong("time-units." + unit);
        if (expiry == 0) {
            sender.sendMessage(ChatColor.RED + "Invalid punishment duration!");
            return;
        }
        tempmutePlayer(sender, victim, VaultLoader.getMessage("punishments.default-reason"),
                silent, expiry);
    }

    @SubCommand("muteSilentReason")
    public void muteSilentReason(VLCommandSender sender, VLOfflinePlayer victim, int time, String unit, boolean silent, String reason) {
        if (!VaultCore.getInstance().getConfig().contains("time-units." + unit)) {
            sender.sendMessage(VaultLoader.getMessage("punishments.time-unit-error"));
            return;
        }
        long expiry = time * VaultCore.getInstance().getConfig().getLong("time-units." + unit);
        if (expiry == 0) {
            sender.sendMessage(ChatColor.RED + "Invalid punishment duration!");
            return;
        }
        tempmutePlayer(sender, victim, reason,
                silent, expiry);
    }

    private void tempmutePlayer(VLCommandSender actor, VLOfflinePlayer victim, String reason, boolean silent, long expiry) {
        if (Bukkit.getPlayer(victim.getUniqueId()) != null) {
            Bukkit.getPlayer(victim.getUniqueId()).sendMessage(VaultLoader.getMessage("punishments.tempmute.message")
                    .replace("{ACTOR}", actor.getFormattedName())
                    .replace("{REASON}", reason)
                    .replace("{PLAYER}", victim.getFormattedName())
                    .replace("{EXPIRY}", Utilities.humanReadableTime(expiry)));
        }
        // expiry *= 1000;

        PunishmentsDB.registerData("iptempmutes", new PunishmentsDB.PunishmentData(IpBanCommand.getPlayerIp(victim), true, reason,
                Utilities.currentTime() + expiry, (actor instanceof VLPlayer) ? ((VLPlayer) actor).getUniqueId() : BanCommand.console));

        actor.sendMessage(VaultLoader.getMessage("punishments.tempmute.sent").replace("{PLAYER}", victim.getFormattedName()));

        if (silent) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission(Permissions.PunishmentNotify)) {
                    player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                            VaultLoader.getMessage("punishments.tempmute.announcement")
                                    .replace("{ACTOR}", actor.getFormattedName())
                                    .replace("{REASON}", reason)
                                    .replace("{PLAYER}", victim.getFormattedName())
                                    .replace("{EXPIRY}", Utilities.humanReadableTime(expiry)));
                }
            }
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(
                        VaultLoader.getMessage("punishments.tempmute.announcement")
                                .replace("{ACTOR}", actor.getFormattedName())
                                .replace("{REASON}", reason)
                                .replace("{PLAYER}", victim.getFormattedName())
                                .replace("{EXPIRY}", Utilities.humanReadableTime(expiry)));
            }
        }
    }
}
