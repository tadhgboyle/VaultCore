package net.vaultmc.vaultcore.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.VaultCore;

public class SeenCommand implements CommandExecutor {

	String string = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("string"));
	String variable1 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-1"));
	String variable2 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-2"));

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					VaultCore.getInstance().getConfig().getString("console-error")));
			return true;
		}

		Player player = (Player) sender;

		if (commandLabel.equalsIgnoreCase("seen")) {

			if (!player.hasPermission("vc.seen")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			if (args.length != 1) {
				player.sendMessage(ChatColor.DARK_GREEN + "Correct usage: " + ChatColor.RED + "/seen <player>");
				return true;
			}

			String target = args[0];
			try {
				java.sql.Statement stmt = VaultCore.getInstance().connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT lastseen FROM players WHERE username='" + target + "'");
				while (rs.next()) {
					long lastseen = rs.getLong("lastseen");
					long currenttime = System.currentTimeMillis();
					long offlinefor = currenttime - lastseen;

					long milliseconds = offlinefor;
					long day = TimeUnit.MILLISECONDS.toDays(offlinefor);
					long hours = TimeUnit.MILLISECONDS.toHours(offlinefor)
							- TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(milliseconds));
					long minutes = TimeUnit.MILLISECONDS.toMinutes(offlinefor)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(offlinefor));
					long seconds = TimeUnit.MILLISECONDS.toSeconds(offlinefor)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(offlinefor));
					
					String status;
					if (Bukkit.getPlayer(target) != null) {
						status = ChatColor.GREEN + " online ";
					}
					else {
						status = ChatColor.RED + " offline ";
					}
					
					String message = String.format(
							variable1 + target + string + " has been" + status + string + "for "
									+ variable2 + "%d" + string + " days " + variable2 + "%d" + string + " hours "
									+ variable2 + "%d" + string + " mins " + variable2 + "%d" + string + "  secs.",
							day, hours, minutes, seconds);
					player.sendMessage(message);
					return true;
				}
				player.sendMessage(ChatColor.RED + "That player has never joined the server!");
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return true;
	}
}