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
        this.register("statsSelf", Collections.emptyList(), "vaultpvp");
        this.register("statsOther",
                Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())), "vaultpvp");

    }

    @SneakyThrows
    @SubCommand("statsSelf")
    public void statsSelf(VLPlayer p) {
        ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT kills, deaths FROM pvp_stats WHERE uuid=?", p.getUniqueId().toString());
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

        p.sendMessage(p.getFormattedName() + ChatColor.YELLOW + "'s stats:");
        p.sendMessage(ChatColor.YELLOW + "Kills: " + ChatColor.DARK_GREEN + kills);
        p.sendMessage(ChatColor.YELLOW + "Deaths: " + ChatColor.DARK_GREEN + deaths);
        p.sendMessage(ChatColor.YELLOW + "K/D: " + ChatColor.DARK_GREEN + kd);

    }

    @SneakyThrows
    @SubCommand("statsOther")
    @Permission(Permissions.StatsCommandOther)
    public void statsOther(VLPlayer p, VLOfflinePlayer target) {
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
}
