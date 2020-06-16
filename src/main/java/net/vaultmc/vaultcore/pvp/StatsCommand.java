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

package net.vaultmc.vaultcore.pvp;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.sql.ResultSet;
import java.util.Collections;

@RootCommand(
        literal = "pvpstats",
        description = "Gets your statistics!"
)
@Permission(Permissions.StatsCommand)
@PlayerOnly
public class StatsCommand extends CommandExecutor {
    public StatsCommand() {
        register("statsSelf", Collections.emptyList(), "vaultcore");
        register("statsOther",
                Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())), "vaultcore");

    }

    @SneakyThrows
    private static void stats(VLPlayer p, VLOfflinePlayer target) {
        ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT kills, deaths FROM pvp_stats WHERE uuid=?", target.getUniqueId().toString());
        int kills = 0;
        int deaths = 0;
        if (rs.next()) {
            kills = rs.getInt("kills");
            deaths = rs.getInt("deaths");
        }
        rs.close();

        double kd;

        if (deaths == 0) {
            kd = 0;

        } else {
            kd = (double) kills / (double) deaths;
        }

        p.sendMessage(target.getFormattedName() + ChatColor.YELLOW + "'s stats:");
        p.sendMessage(ChatColor.YELLOW + "Kills: " + ChatColor.DARK_GREEN + kills);
        p.sendMessage(ChatColor.YELLOW + "Deaths: " + ChatColor.DARK_GREEN + deaths);
        p.sendMessage(ChatColor.YELLOW + "K/D: " + ChatColor.DARK_GREEN + kd);
    }

    @SneakyThrows
    @SubCommand("statsSelf")
    public void statsSelf(VLPlayer p) {
        stats(p, p);
    }

    @SneakyThrows
    @SubCommand("statsOther")
    @Permission(Permissions.StatsCommandOther)
    public void statsOther(VLPlayer p, VLOfflinePlayer target) {
        stats(p, target);
    }
}
