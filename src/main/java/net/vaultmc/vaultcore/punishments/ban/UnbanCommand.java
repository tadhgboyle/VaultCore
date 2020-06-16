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
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

@RootCommand(
        literal = "unban",
        description = "Re-allows a banned player to join the server."
)
@Aliases({"pardon"})
@Permission(Permissions.BanCommand)
public class UnbanCommand extends CommandExecutor {
    public UnbanCommand() {
        register("unban", Collections.singletonList(Arguments.createArgument("player", Arguments.offlinePlayerArgument())));
        register("unbanSilent", Arrays.asList(
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("silent", Arguments.boolArgument())
        ));
    }

    @SubCommand("unban")
    public void unban(VLCommandSender sender, VLOfflinePlayer victim) {
        unbanPlayer(sender, victim, false);
    }

    @SubCommand("unbanSilent")
    public void unbanSilent(VLCommandSender sender, VLOfflinePlayer victim, boolean silent) {
        unbanPlayer(sender, victim, silent);
    }

    private void unbanPlayer(VLCommandSender actor, VLOfflinePlayer victim, boolean silent) {
        PunishmentsDB.unregisterData("bans", victim.getUniqueId().toString());
        PunishmentsDB.unregisterData("tempbans", victim.getUniqueId().toString());
        PunishmentsDB.unregisterData("ipbans", IpBanCommand.getPlayerIp(victim));  // I felt something might go wrong here, but
        PunishmentsDB.unregisterData("iptempbans", IpBanCommand.getPlayerIp(victim));  // I don't know what it is.

        actor.sendMessage(VaultLoader.getMessage("punishments.unban.sent").replace("{PLAYER}", victim.getFormattedName()));

        if (silent) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission(Permissions.PunishmentNotify)) {
                    player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                            VaultLoader.getMessage("punishments.unban.announcement")
                                    .replace("{ACTOR}", actor.getFormattedName())
                                    .replace("{PLAYER}", victim.getFormattedName()));
                }
            }
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(
                        VaultLoader.getMessage("punishments.unban.announcement")
                                .replace("{ACTOR}", actor.getFormattedName())
                                .replace("{PLAYER}", victim.getFormattedName()));
            }
        }
    }
}
