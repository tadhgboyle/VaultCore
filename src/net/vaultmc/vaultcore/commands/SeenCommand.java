package net.vaultmc.vaultcore.commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;

public class SeenCommand implements CommandExecutor {

	String string = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("string"));
	String variable1 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-1"));
	String variable2 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-2"));

	public boolean onCommand(CommandSender sender, Command commmand, String commandLabel, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(Utilities.consoleError());
			return true;
		}

		Player player = (Player) sender;

		if (commandLabel.equalsIgnoreCase("seen")) {

			if (!player.hasPermission(Permissions.SeenCommand)) {
				player.sendMessage(Utilities.noPermission());
				return true;
			}
			if (args.length != 1) {
				player.sendMessage(Utilities.usageMessage(commandLabel, "<player>"));
				return true;
			}

			String target = args[0];
			try {
				java.sql.Statement stmt = VaultCore.getInstance().connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT lastseen FROM players WHERE username='" + target + "'");
				while (rs.next()) {
					long lastseen = rs.getLong("lastseen");
					long currenttime = System.currentTimeMillis();
					long duration = currenttime - lastseen;

					long[] time = Utilities.formatDuration(duration);

					String status;

					if (Bukkit.getPlayer(target) != null) {
						status = ChatColor.GREEN + " online ";
					} else {
						status = ChatColor.RED + " offline ";
					}

					String message = String.format(variable1 + target + string + " has been" + status + string + "for "
							+ variable2 + "%d" + string + " days, " + variable2 + "%d" + string + " hours and "
							+ variable2 + "%d" + string + "  minutes.", time[0], time[1], time[2]);
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