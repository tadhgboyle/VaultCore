package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.commands.*;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

@RootCommand(literal = "check", description = "Get info about a player.")
@Permission(Permissions.CheckCommand)
public class CheckCommand extends CommandExecutor {
	private String string = Utilities.string;
	private String variable1 = Utilities.variable1;

	public CheckCommand() {
		register("check",
				Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
	}

	@SubCommand("check")
	public void check(CommandSender sender, OfflinePlayer target) {

		DBConnection database = VaultCore.getDatabase();

		try {
			ResultSet rs = database.executeQueryStatement(
					"SELECT uuid, username, firstseen, lastseen, rank, ip FROM players WHERE username=?",
					target.getName());
			if (!rs.next()) {
				sender.sendMessage(ChatColor.RED + "That player has never joined the server.");
				return;
			}
			String uuid = rs.getString("uuid");
			String username = VaultCoreAPI.getName(target);
			long firstseen = rs.getLong("firstseen");
			long lastseen = rs.getLong("lastseen");
			String rank = rs.getString("rank");
			String ip = rs.getString("ip");

			GregorianCalendar firstCal = new GregorianCalendar();
			firstCal.setTimeInMillis(firstseen);

			int fdate = firstCal.get(Calendar.DAY_OF_MONTH);
			int fmonth = firstCal.get(Calendar.MONTH) + 1;
			int fyear = firstCal.get(Calendar.YEAR);

			GregorianCalendar lastCal = new GregorianCalendar();
			lastCal.setTimeInMillis(lastseen);

			int ldate = lastCal.get(Calendar.DAY_OF_MONTH);
			int lmonth = lastCal.get(Calendar.MONTH) + 1;
			int lyear = lastCal.get(Calendar.YEAR);

			sender.sendMessage(ChatColor.DARK_GREEN + "--== [Check] ==--");
			sender.sendMessage("");

			if (target.isOnline()) {
				sender.sendMessage(string + "Checking: " + username);
			} else {
				sender.sendMessage(
						string + "Checking: " + username + ChatColor.GRAY + " " + ChatColor.ITALIC + "[OFFLINE]");
			}
			sender.sendMessage(string + "UUID: " + variable1 + uuid);
			sender.sendMessage(string + "First Seen (D/M/Y): " + variable1 + fdate + "/" + variable1 + fmonth + "/"
					+ variable1 + fyear);
			sender.sendMessage(string + "Last Seen (D/M/Y): " + variable1 + ldate + "/" + variable1 + lmonth + "/"
					+ variable1 + lyear);
			sender.sendMessage(string + "Last IP: " + variable1 + ip);
			sender.sendMessage(string + "Rank: " + variable1 + rank);
			sender.sendMessage(
					string + "Database: " + variable1 + "https://database.vaultmc.net/?user=" + target.getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}