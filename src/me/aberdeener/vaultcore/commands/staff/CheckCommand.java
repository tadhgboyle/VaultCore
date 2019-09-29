package me.aberdeener.vaultcore.commands.staff;

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

import me.aberdeener.vaultcore.VaultCore;

public class CheckCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("check")) {

			if (!sender.hasPermission("vc.check")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}

			// console sender check
			if (sender instanceof Player) {

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

							// first seen calender
							GregorianCalendar firstCal = new GregorianCalendar();
							firstCal.setTimeInMillis(firstseen);

							int fdate = firstCal.get(Calendar.DAY_OF_MONTH);
							int fmonth = firstCal.get(Calendar.MONTH) + 1;
							int fyear = firstCal.get(Calendar.YEAR);

							// last seen calender
							GregorianCalendar lastCal = new GregorianCalendar();
							lastCal.setTimeInMillis(lastseen);

							int ldate = lastCal.get(Calendar.DAY_OF_MONTH);
							int lmonth = lastCal.get(Calendar.MONTH) + 1;
							int lyear = lastCal.get(Calendar.YEAR);

							sender.sendMessage(ChatColor.DARK_GREEN + "--== [Check] ==--");
							sender.sendMessage("");

							if (Bukkit.getOnlinePlayers().toString().contains(args[0])) {
								sender.sendMessage(ChatColor.YELLOW + "Checking: " + ChatColor.GOLD + username);
							}
							
							else {
								sender.sendMessage(ChatColor.YELLOW + "Checking: " + ChatColor.GOLD + username
										+ ChatColor.GRAY + " " + ChatColor.ITALIC + "[OFFLINE]");
							}

							sender.sendMessage(ChatColor.YELLOW + "UUID: " + ChatColor.DARK_GREEN + uuid);

							sender.sendMessage(ChatColor.YELLOW + "First Seen (D/M/Y): " + ChatColor.DARK_GREEN + fdate
									+ "/" + ChatColor.DARK_GREEN + fmonth + "/" + ChatColor.DARK_GREEN + fyear);

							sender.sendMessage(ChatColor.YELLOW + "Last Seen (D/M/Y): " + ChatColor.DARK_GREEN + ldate
									+ "/" + ChatColor.DARK_GREEN + lmonth + "/" + ChatColor.DARK_GREEN + lyear);

							sender.sendMessage(ChatColor.YELLOW + "Last IP: " + ChatColor.DARK_GREEN + ip);

							sender.sendMessage(ChatColor.YELLOW + "Rank: " + ChatColor.DARK_GREEN + rank);

							sender.sendMessage(ChatColor.YELLOW + "Database: " + ChatColor.DARK_GREEN
									+ "database.vaultmc.net/user.php?user=" + username);

							return true;
						}
						sender.sendMessage(ChatColor.RED + "That player has never joined the server.");
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}

				else {
					sender.sendMessage(ChatColor.DARK_GREEN + "Correct usage: " + ChatColor.RED + "/check <player>");
					return true;
				}
			}
		}

		return true;
	}
}