package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

@RootCommand(
        literal = "playtime",
        description = "Check yourself or other's play time."
)
@Permission(Permissions.PlayTime)
@Aliases("pt")
public class PlayTime extends CommandExecutor {

    private String string = VaultCore.getInstance().getConfig().getString("string");
    private String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");
    private String variable2 = VaultCore.getInstance().getConfig().getString("variable-2");

    public PlayTime() {
        register("playTimeSelf", Collections.emptyList());
        register("playTimeOthers", Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())),
                "vaultcore");
    }

    @SubCommand("playTimeSelf")
    @PlayerOnly
    public void checkPlayTimeSelf(CommandSender sender) {
        printPlayTimeOnline((Player) sender, sender);
    }

    @SubCommand("playTimeOthers")
    @Permission(Permissions.PlayTimeOther)
    public void checkPlayTimeOthers(CommandSender sender, OfflinePlayer target) {
        if (target.isOnline()) {
            printPlayTimeOnline(target.getPlayer(), sender);
            return;
        }
        printPlayTimeOffline(sender, target.getName());
    }

    private void printPlayTimeOnline(Player player, CommandSender sender) {
        long t = (long) (player.getStatistic(Statistic.PLAY_ONE_MINUTE) * 0.05 * 1000);
        long[] time = Utilities.formatDuration(t);
        String playtimeMsg = String.format(
                ChatColor.translateAlternateColorCodes('&',
                        variable1 + "%s" + string + " has played for " + variable2 + "%d" + string + " days, "
                                + variable2 + "%d" + string + " hours and " + variable2 + "%d" + string + " minutes."),
                VaultCoreAPI.getName(player), time[0], time[1], time[2]);
        sender.sendMessage(playtimeMsg);
    }

    private void printPlayTimeOffline(CommandSender player, String target) {
        try {
            java.sql.Statement stmt = VaultCore.getInstance().connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT username, playtime FROM players WHERE username='" + target + "'");
            if (!rs.next()) {
                player.sendMessage(ChatColor.RED + "This player has never joined before!");
                return;
            }

            String username = rs.getString("username");
            long playtime = rs.getLong("playtime");
            long t = (long) (playtime * 0.05 * 1000);
            long[] time = Utilities.formatDuration(t);
            String playtimeMsg = String.format(
                    ChatColor.translateAlternateColorCodes('&',
                            variable1 + "%s" + ChatColor.GRAY + " " + ChatColor.ITALIC + "[OFFLINE]" + string
                                    + " has played for " + variable2 + "%d" + string + " days, " + variable2 + "%d"
                                    + string + " hours and " + variable2 + "%d" + string + " minutes."),
                    username, time[0], time[1], time[2]);
            player.sendMessage(playtimeMsg);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}