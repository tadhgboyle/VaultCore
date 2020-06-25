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

package net.vaultmc.vaultcore.stats;

import lombok.SneakyThrows;
import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;

import java.sql.ResultSet;
import java.util.Collections;

@RootCommand(literal = "seen", description = "See how long a player has been online/offline for.")
@Permission(Permissions.SeenCommand)
public class SeenCommand extends CommandExecutor {
    public SeenCommand() {
        register("seen",
                Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
    }

    @SneakyThrows
    @SubCommand("seen")
    public void seen(VLCommandSender sender, VLOfflinePlayer player) {
        DBConnection database = VaultCore.getDatabase();
        long lastseen = 0;
        String status = null;
        if (player.isOnline()) {
            ResultSet rs = database.executeQueryStatement("SELECT start_time FROM sessions WHERE uuid=? ORDER BY start_time DESC",
                    player.getUniqueId().toString());
            if (rs.next()) {
                lastseen = rs.getLong("start_time");
                status = ChatColor.GREEN + "online ";
            }
        } else {
            ResultSet rs = database.executeQueryStatement("SELECT lastseen FROM players WHERE username=?",
                    player.getName());
            if (!rs.next()) {
                sender.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
                return;
            }
            lastseen = rs.getLong("lastseen");
            status = ChatColor.RED + "offline ";
        }

        long duration = System.currentTimeMillis() - lastseen;

        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.seen"),
                player.getFormattedName(), status, Utilities.millisToTime(duration, false, true)));
    }
}