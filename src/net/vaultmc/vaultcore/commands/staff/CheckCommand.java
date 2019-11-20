package net.vaultmc.vaultcore.commands.staff;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;

public class CheckCommand implements CommandExecutor {

	String string = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("string"));
	String variable1 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-1"));
	String variable2 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-2"));

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("check")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(Utilities.consoleError());
				return true;
			}

			Player player = (Player) sender;

			if (!player.hasPermission(Permissions.CheckCommand)) {
				player.sendMessage(Utilities.noPermission());
				return true;
			}

			if (args.length == 1) {

				String target = args[0];
				try {
					java.sql.Statement stmt = VaultCore.getInstance().connection.createStatement();
					ResultSet rs = stmt.executeQuery(
							"SELECT uuid, username, firstseen, lastseen, rank, ip FROM players WHERE username='"
									+ target + "'");
					while (rs.next()) {

						String uuid = rs.getString("uuid");
						String username = rs.getString("username");
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

						player.sendMessage(ChatColor.DARK_GREEN + "--== [Check] ==--");
						player.sendMessage("");

						if (Bukkit.getPlayer(args[0]) != null) {
							player.sendMessage(string + "Checking: " + variable1 + username);
						} else {
							player.sendMessage(string + "Checking: " + variable1 + username + ChatColor.GRAY + " "
									+ ChatColor.ITALIC + "[OFFLINE]");
						}
						player.sendMessage(string + "UUID: " + variable2 + uuid);
						player.sendMessage(string + "First Seen (D/M/Y): " + variable2 + fdate + "/" + variable2
								+ fmonth + "/" + variable2 + fyear);
						player.sendMessage(string + "Last Seen (D/M/Y): " + variable2 + ldate + "/" + variable2 + lmonth
								+ "/" + variable2 + lyear);
						player.sendMessage(string + "Last IP: " + variable2 + ip);
						player.sendMessage(string + "Rank: " + variable2 + rank);
						player.sendMessage(
								string + "Database: " + variable2 + "https://database.vaultmc.net/?user=" + username);
						return true;
					}
					player.sendMessage(ChatColor.RED + "That player has never joined the server.");
					return true;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				player.sendMessage(Utilities.usageMessage(command.getName(), "<player"));
				return true;
			}
		}
		return true;
	}
}