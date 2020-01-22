package net.vaultmc.vaultcore.commands.staff;

import java.sql.ResultSet;
import java.util.Collections;

import org.apache.commons.lang.WordUtils;
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

@RootCommand(literal = "check", description = "Get info about a player.")
@Permission(Permissions.CheckCommand)
public class CheckCommand extends CommandExecutor {

	public CheckCommand() {
		register("check",
				Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
	}

	@SneakyThrows
	@SubCommand("check")
	public void check(VLCommandSender sender, VLOfflinePlayer target) {
		DBConnection database = VaultCore.getDatabase();
		ResultSet rs = database.executeQueryStatement(
				"SELECT uuid, username, firstseen, lastseen, rank, ip FROM players WHERE username=?", target.getName());
		if (!rs.next()) {
			sender.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
			return;
		}
		String uuid = rs.getString("uuid");
		String username = target.getFormattedName();
		long firstseen = rs.getLong("firstseen");
		long lastseen = rs.getLong("lastseen");
		String rank = WordUtils.capitalize(rs.getString("rank"));
		String ip = rs.getString("ip");

		sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.check.header"));

		if (target.isOnline()) {
			sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.check.format"),
					"Checking", username));
		} else {
			sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.check.format"),
					"Checking", username + ChatColor.GRAY + " " + ChatColor.ITALIC + "[OFFLINE]"));
		}
		sender.sendMessage(
				Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.check.format"), "UUID", uuid));
		sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.check.format"),
				"First Seen", Utilities.millisToDate(firstseen)));
		sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.check.format"),
				"Last Seen", Utilities.millisToDate(lastseen)));
		sender.sendMessage(
				Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.check.format"), "Last IP", ip));
		sender.sendMessage(
				Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.check.format"), "Rank", rank));
		sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.check.format"), "WebUI",
				"https://database.vaultmc.net/?user=" + target.getName()));
		sender.performCommand("tag list " + target.getName());

	}
}