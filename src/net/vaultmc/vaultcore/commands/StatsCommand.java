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

import java.sql.ResultSet;
import java.util.Collections;

@RootCommand(literal = "stats", description = "Check statistics of yourself or other players.")
@Permission(Permissions.StatsCommand)
public class StatsCommand extends CommandExecutor {

	private String string = Utilities.string;
	private String variable2 = Utilities.variable2;

	public StatsCommand() {
		register("statsSelf", Collections.emptyList());
		register("statsOthers",
				Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
	}

	@SubCommand("statsSelf")
	@PlayerOnly
	public void statsSelf(VLPlayer player) {
		viewStats(player, player);
	}

	@SubCommand("statsOthers")
	@Permission(Permissions.StatsCommandOther)
	public void statsOthers(VLCommandSender sender, VLOfflinePlayer target) {
		viewStats(sender, target);
	}

	@SneakyThrows
	private void viewStats(VLCommandSender sender, VLOfflinePlayer target) {
		DBConnection database = VaultCore.getDatabase();

		ResultSet check = database.executeQueryStatement("SELECT username FROM players WHERE username=?",
				target.getName());
		if (!check.next()) {
			sender.sendMessage(ChatColor.RED + "This player has never joined before!");
			return;
		}
		ResultSet stats = database.executeQueryStatement(
				"SELECT COUNT(session_id) AS sessions, AVG(duration) AS duration FROM sessions WHERE username=?",
				target.getName());
		if (!stats.next()) {
			sender.sendMessage(ChatColor.RED + "An error has occurred, please consult an administrator for help.");
			return;
		}
		long[] time = Utilities.millisToTime(stats.getInt("duration"));
		sender.sendMessage(ChatColor.DARK_GREEN + "--== [Stats] ==--");
		sender.sendMessage(target.getFormattedName() + string + " has joined " + variable2
				+ stats.getString("sessions") + string + " times.");
		String message = String.format(target.getFormattedName() + string + "'s average session length is "
				+ variable2 + "%d" + string + " days, " + variable2 + "%d" + string + " hours and " + variable2 + "%d"
				+ string + "  minutes.", time[0], time[1], time[2]);
		sender.sendMessage(message);
	}
}