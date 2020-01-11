package net.vaultmc.vaultcore.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import net.vaultmc.vaultcore.*;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;

@RootCommand(literal = "seen", description = "See how long a player has been online/offline for.")
@Permission(Permissions.SeenCommand)
public class SeenCommand extends CommandExecutor {

	private String string = Utilities.string;
	private String variable2 = Utilities.variable2;

	public SeenCommand() {
		register("seen",
				Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
	}

	@SubCommand("seen")
	public void seen(CommandSender sender, OfflinePlayer player) {

		DBConnection database = VaultCore.getDatabase();

		try {
			ResultSet rs = database.executeQueryStatement("SELECT lastseen FROM players WHERE username=?",
					player.getName());
			if (!rs.next()) {
				sender.sendMessage(ChatColor.RED + "This player has never joined before!");
				return;
			}
			long lastseen = rs.getLong("lastseen");
			long currenttime = System.currentTimeMillis();
			long duration = currenttime - lastseen;

			long[] time = Utilities.millisToTime(duration);

			String status;

			if (player.isOnline()) {
				status = ChatColor.GREEN + " online ";
			} else {
				status = ChatColor.RED + " offline ";
			}

			String message = String.format(VaultCoreAPI.getName(player) + string + " has been" + status
					+ string + "for " + variable2 + "%d" + string + " days, " + variable2 + "%d" + string
					+ " hours and " + variable2 + "%d" + string + "  minutes.", time[0], time[1], time[2]);
			sender.sendMessage(message);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}