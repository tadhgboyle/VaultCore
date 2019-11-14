package net.vaultmc.vaultcore.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.VaultCore;

public class PlayTime implements CommandExecutor {

	String string = VaultCore.getInstance().getConfig().getString("string");
	String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");
	String variable2 = VaultCore.getInstance().getConfig().getString("variable-2");

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("playtime")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}

			Player player = (Player) sender;

			if (args.length == 0) {
				if (!sender.hasPermission("vc.playtime")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							VaultCore.getInstance().getConfig().getString("no-permission")));
					return true;
				}
				printPlayTimeOnline(player, (Player) sender);
				return true;

			}

			if (args.length == 1) {

				if (!player.hasPermission("vc.playtime.other")) {
					player.sendMessage(
							ChatColor.DARK_RED + "Uh oh! You don't have permission to check their playtime!");
					return true;
				}

				String target = args[0];
				if (Bukkit.getPlayer(target) != null) {
					printPlayTimeOnline(Bukkit.getPlayer(target), player);
					return true;
				}
				printPlayTimeOffline(player, target);
				return true;
			}

			else {
				player.sendMessage(ChatColor.DARK_GREEN + "Correct usage: " + ChatColor.RED + "/playtime [player]");
				return true;
			}
		}

		return false;
	}

	private void printPlayTimeOnline(Player player, Player sender) {

		long t = (long) (player.getStatistic(Statistic.PLAY_ONE_MINUTE) * 0.05 * 1000);
		long[] time = formatDuration(t);
		String playtimeMsg = String.format(
				ChatColor.translateAlternateColorCodes('&',
						variable1 + "%s" + string + " has played for " + variable2 + "%d" + string + " days, "
								+ variable2 + "%d" + string + " hours and " + variable2 + "%d" + string + " minutes."),
				player.getName(), time[0], time[1], time[2], time[3]);
		sender.sendMessage(playtimeMsg);
	}

	private void printPlayTimeOffline(Player player, String target) {

		try {
			java.sql.Statement stmt = VaultCore.getInstance().connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT username, playtime FROM players WHERE username='" + target + "'");
			while (rs.next()) {

				String username = rs.getString("username");
				long playtime = rs.getLong("playtime");
				long t = (long) (playtime * 0.05 * 1000);
				long[] time = formatDuration(t);
				String playtimeMsg = String.format(
						ChatColor.translateAlternateColorCodes('&',
								variable1 + "%s" + ChatColor.GRAY + " " + ChatColor.ITALIC + "[OFFLINE]" + string
										+ " has played for " + variable2 + "%d" + string + " days, " + variable2 + "%d"
										+ string + " hours and " + variable2 + "%d" + string + " minutes."),
						username, time[0], time[1], time[2], time[3]);
				player.sendMessage(playtimeMsg);
				return;
			}
			player.sendMessage(ChatColor.RED + "That player has never joined the server.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private long[] formatDuration(long millis) {
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
		return new long[] { days, hours, minutes, seconds };
	}
}