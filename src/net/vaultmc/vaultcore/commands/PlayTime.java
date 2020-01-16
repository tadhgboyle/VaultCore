package net.vaultmc.vaultcore.commands;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;

import java.sql.ResultSet;
import java.util.Collections;

@RootCommand(literal = "playtime", description = "Check yourself or other's play time.")
@Permission(Permissions.PlayTime)
@Aliases("pt")
public class PlayTime extends CommandExecutor {

	private String string = Utilities.string;
	private String variable1 = Utilities.variable1;
	private String variable2 = Utilities.variable2;

	public PlayTime() {
		register("playTimeSelf", Collections.emptyList());
		register("playTimeOthers",
				Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
	}

    @SubCommand("playTimeSelf")
    @PlayerOnly
    public void checkPlayTimeSelf(VLPlayer sender) {
        printPlayTimeOnline(sender, sender);
    }

    @SubCommand("playTimeOthers")
    @Permission(Permissions.PlayTimeOther)
    public void checkPlayTimeOthers(VLCommandSender sender, VLOfflinePlayer target) {
        if (target.isOnline()) {
            printPlayTimeOnline(target.getOnlinePlayer(), sender);
            return;
        }
        printPlayTimeOffline(sender, target);
    }

    private void printPlayTimeOnline(VLPlayer player, VLCommandSender sender) {
        long t = (long) (player.getStatistic(Statistic.PLAY_ONE_MINUTE) * 0.05 * 1000);
        long[] time = Utilities.millisToTime(t);
        String playtimeMsg = String.format(
                ChatColor.translateAlternateColorCodes('&',
                        variable1 + "%s" + string + " has played for " + variable2 + "%d" + string + " days, "
                                + variable2 + "%d" + string + " hours and " + variable2 + "%d" + string + " minutes."),
                player.getFormattedName(), time[0], time[1], time[2]);
        sender.sendMessage(playtimeMsg);
    }

    @SneakyThrows
    private void printPlayTimeOffline(VLCommandSender player, VLOfflinePlayer target) {
        DBConnection database = VaultCore.getDatabase();

        ResultSet rs = database.executeQueryStatement("SELECT username, playtime FROM players WHERE username=?",
                target.getName());
        if (!rs.next()) {
            player.sendMessage(ChatColor.RED + "This player has never joined before!");
            return;
        }

        long playtime = rs.getLong("playtime");
		long t = (long) (playtime * 0.05 * 1000);
        long[] time = Utilities.millisToTime(t);
        String playtimeMsg = String.format(
                ChatColor.translateAlternateColorCodes('&',
                        variable1 + "%s" + ChatColor.GRAY + " " + ChatColor.ITALIC + "[OFFLINE]" + string
                                + " has played for " + variable2 + "%d" + string + " days, " + variable2 + "%d" + string
                                + " hours and " + variable2 + "%d" + string + "  minutes."),
                target.getFormattedName(), time[0], time[1], time[2]);
		player.sendMessage(playtimeMsg);
	}
}