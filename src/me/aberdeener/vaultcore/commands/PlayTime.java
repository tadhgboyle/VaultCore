package me.aberdeener.vaultcore.commands;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;

public class PlayTime implements CommandExecutor {

	private static PlayTime INSTANCE;

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("playtime")) {

			if (args.length == 0) {

				// permission check
				if (!sender.hasPermission("vc.playtime")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							VaultCore.getInstance().getConfig().getString("no-permission")));
					return true;
				}

				// console sender error messages
				if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.RED + "You can't get the playtime of the console...");
					sender.sendMessage(ChatColor.RED + "Try adding a name after the command ;D");
					return true;
				}

				printPlayTime((Player) sender, sender);
				return true;

			} else {
				if (!sender.hasPermission("vc.playtime.other")) {
					sender.sendMessage(
							ChatColor.DARK_RED + "Uh oh! You don't have permission to check their playtime!");
					return true;
				}

				Player player = Bukkit.getPlayer(args[0]);
				if (player == null) {
					sender.sendMessage(ChatColor.RED + "That player is offline!");
					return true;
				}

				printPlayTime(player, sender);
				return true;
			}
		}

		return false;
	}

	private void printPlayTime(Player player, CommandSender sender) {
		
		String string = VaultCore.getInstance().getConfig().getString("string");
		String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");
		String variable2 = VaultCore.getInstance().getConfig().getString("variable-2");
		
		long t = (long) (player.getStatistic(Statistic.PLAY_ONE_MINUTE) * 0.05 * 1000);
		long[] time = formatDuration(t);

		// create messages variables (like help hover)
		String playtime = String.format(
				ChatColor.translateAlternateColorCodes('&',
						variable1 + "%s"
								+ string + " has played for "
								+ variable2 + "%d"
								+ string + " days, "
								+ variable2 + "%d"
								+ string + " hours and "
								+ variable2 + "%d"
								+ string + " minutes."),
				player.getName(), time[0], time[1], time[2], time[3]);

		// send messages
		sender.sendMessage(playtime);
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

	public static PlayTime getInstance() {
		return INSTANCE;
	}

}