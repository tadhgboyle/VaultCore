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

package net.vaultmc.vaultcore.punishments.ban;

import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.punishments.PunishmentsDB;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.commands.wrappers.WrappedSuggestion;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RootCommand(
        literal = "iptempban",
        description = "Disallows a player from joining the server temporarily. (By IP)"
)
@Permission(Permissions.BanCommand)
public class IpTempBanCommand extends CommandExecutor {
    public IpTempBanCommand() {
        register("banNoReason", Arrays.asList(
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("time", Arguments.integerArgument(1)),
                Arguments.createArgument("unit", Arguments.word())));
        register("banSilent", Arrays.asList(
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("time", Arguments.integerArgument(1)),
                Arguments.createArgument("unit", Arguments.word()),
                Arguments.createArgument("silent", Arguments.boolArgument())));
        register("banSilentReason", Arrays.asList(
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
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
                    .replace("{ACTOR}", actor.getFormattedName())
                    .replace("{REASON}", reason)
                    .replace("{EXPIRY}", Utilities.humanReadableTime(expiry)));
        }

        PunishmentsDB.registerData("iptempbans", new PunishmentsDB.PunishmentData(IpBanCommand.getPlayerIp(victim),
                true, reason, Utilities.currentTime() + expiry, (actor instanceof VLPlayer) ? ((VLPlayer) actor).getUniqueId() : BanCommand.console));

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
                if (VLPlayer.getPlayer(player).hasPermission(Permissions.PunishmentNotify)) {
                    player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                            VaultLoader.getMessage("punishments.tempban.announcement")
                                    .replace("{ACTOR}", actor.getFormattedName())
                                    .replace("{REASON}", reason)
                                    .replace("{PLAYER}", victim.getFormattedName())
                                    .replace("{EXPIRY}", Utilities.humanReadableTime(expiry)));
                }
            }
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(
                        VaultLoader.getMessage("punishments.tempban.announcement")
                                .replace("{ACTOR}", actor.getFormattedName())
                                .replace("{REASON}", reason)
                                .replace("{PLAYER}", victim.getFormattedName())
                                .replace("{EXPIRY}", Utilities.humanReadableTime(expiry)));
            }
        }
    }
}
