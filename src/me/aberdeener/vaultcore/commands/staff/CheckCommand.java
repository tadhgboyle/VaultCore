package me.aberdeener.vaultcore.commands.staff;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;

public class CheckCommand implements CommandExecutor {
	
	public static boolean online = false;
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("check")) {

			if (!sender.hasPermission("vc.check")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
						
			// console sender check
			if ((sender instanceof Player)) {

				if (args.length == 1) {
					
					@SuppressWarnings("deprecation")
					OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

					if (target.isOnline()) {
						online = true;
					}

					// see if they exist in data.yml
					if (VaultCore.getInstance().getPlayerData().contains("players." + target.getUniqueId())) {

						// first seen calender
						GregorianCalendar firstCal = new GregorianCalendar();
						firstCal.setTimeInMillis(VaultCore.getInstance().getPlayerData()
								.getLong("players." + target.getUniqueId() + ".firstSeen"));

						int fdate = firstCal.get(Calendar.DAY_OF_MONTH);
						int fmonth = firstCal.get(Calendar.MONTH) + 1;
						int fyear = firstCal.get(Calendar.YEAR);

						// last seen calender
						GregorianCalendar lastCal = new GregorianCalendar();
						lastCal.setTimeInMillis(target.getLastPlayed());

						int ldate = lastCal.get(Calendar.DAY_OF_MONTH);
						int lmonth = lastCal.get(Calendar.MONTH) + 1;
						int lyear = lastCal.get(Calendar.YEAR);

						sender.sendMessage(ChatColor.DARK_GREEN + "--== [Check] ==--");
						sender.sendMessage("");

						if (online) {
							sender.sendMessage(ChatColor.YELLOW + "Checking: " + ChatColor.GOLD + VaultCore.getInstance()
									.getPlayerData().getString("players." + target.getUniqueId() + ".username"));
						}

						else {
							sender.sendMessage(ChatColor.YELLOW + "Checking: " + ChatColor.GOLD
									+ VaultCore.getInstance().getPlayerData()
											.getString("players." + target.getUniqueId() + ".username")
									+ ChatColor.GRAY + " " + ChatColor.ITALIC + "[OFFLINE]");
						}

						sender.sendMessage(ChatColor.YELLOW + "UUID: " + ChatColor.DARK_GREEN + target.getUniqueId());

						sender.sendMessage(ChatColor.YELLOW + "First Seen (D/M/Y): " + ChatColor.DARK_GREEN + fdate
								+ "/" + ChatColor.DARK_GREEN + fmonth + "/" + ChatColor.DARK_GREEN + fyear);

						sender.sendMessage(ChatColor.YELLOW + "Last Seen (D/M/Y): " + ChatColor.DARK_GREEN + ldate + "/"
								+ ChatColor.DARK_GREEN + lmonth + "/" + ChatColor.DARK_GREEN + lyear);

						sender.sendMessage(ChatColor.YELLOW + "Last IP: " + ChatColor.DARK_GREEN + VaultCore.getInstance()
								.getPlayerData().getString("players." + target.getUniqueId() + ".lastIp"));

						sender.sendMessage(ChatColor.YELLOW + "Last Rank: " + ChatColor.DARK_GREEN + VaultCore.getInstance()
								.getPlayerData().getString("players." + target.getUniqueId() + ".lastRank"));

						sender.sendMessage(ChatColor.YELLOW + "Last World: " + ChatColor.DARK_GREEN + VaultCore.getInstance()
								.getPlayerData().getString("players." + target.getUniqueId() + ".lastWorld"));
						return true;
					}

					else {
						sender.sendMessage(ChatColor.RED + "That player has never joined the server!");
						return true;
					}

				}

			}

			else {
				sender.sendMessage(ChatColor.DARK_GREEN + "Correct usage: " + ChatColor.RED + "/check <player>");
				return true;
			}
		}

		return true;
	}
}