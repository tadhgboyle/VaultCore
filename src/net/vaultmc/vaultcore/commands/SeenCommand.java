package net.vaultmc.vaultcore.commands;

import java.sql.ResultSet;
import java.util.Collections;

import org.bukkit.ChatColor;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;

@RootCommand(literal = "seen", description = "See how long a player has been online/offline for.")
@Permission(Permissions.SeenCommand)
public class SeenCommand extends CommandExecutor {

	public SeenCommand() {
		register("seen",
				Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
	}

	@SneakyThrows
	@SubCommand("seen")
	public void seen(VLCommandSender sender, VLOfflinePlayer player) {
		DBConnection database = VaultCore.getDatabase();
		long lastseen = 0;
		String status = null;
		if (player.isOnline()) {
			ResultSet rs = database.executeQueryStatement("SELECT start_time FROM sessions WHERE username=? ORDER BY start_time DESC",
					player.getName());
			if (rs.next()) {
				lastseen = rs.getLong("start_time");
				status = ChatColor.GREEN + "online ";
			}

		} else {
			ResultSet rs = database.executeQueryStatement("SELECT lastseen FROM players WHERE username=?",
					player.getName());
			if (!rs.next()) {
				sender.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
				return;
			}
			lastseen = rs.getLong("lastseen");
			status = ChatColor.RED + "offline ";
		}

		long currenttime = System.currentTimeMillis();
		long duration = currenttime - lastseen;

		sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.seen"),
				player.getFormattedName(), status, Utilities.millisToTime(duration)));
	}
}