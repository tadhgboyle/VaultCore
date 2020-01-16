package net.vaultmc.vaultcore.commands;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.ChatColor;

import java.sql.ResultSet;
import java.util.Collections;

@RootCommand(literal = "seen", description = "See how long a player has been online/offline for.")
@Permission(Permissions.SeenCommand)
public class SeenCommand extends CommandExecutor {

	private String string = Utilities.string;
	private String variable2 = Utilities.variable2;

	public SeenCommand() {
		register("seen",
				Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
	}

	@SneakyThrows
	@SubCommand("seen")
	public void seen(VLCommandSender sender, VLOfflinePlayer player) {
		DBConnection database = VaultCore.getDatabase();
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

		String message = String.format(player.getFormattedName() + string + " has been" + status + string + "for "
				+ variable2 + "%d" + string + " days, " + variable2 + "%d" + string + " hours and " + variable2 + "%d"
				+ string + "  minutes.", time[0], time[1], time[2]);
		sender.sendMessage(message);
	}
}