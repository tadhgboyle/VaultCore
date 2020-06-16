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

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.punishments.PunishmentsDB;
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
import java.util.UUID;
import java.util.stream.Collectors;

@RootCommand(
        literal = "ban",
        description = "Disallows a player from joining the server permanently."
)
@Permission(Permissions.BanCommand)
public class BanCommand extends CommandExecutor {
    public static final UUID console = UUID.fromString("00000000-0000-0000-0000-000000000000");

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
                true, reason, -1, (actor instanceof VLPlayer) ? ((VLPlayer) actor).getUniqueId() : console));

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
