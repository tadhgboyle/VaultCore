package net.vaultmc.vaultcore.commands.staff;

import java.sql.ResultSet;
import java.util.Collections;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;

@RootCommand(literal = "check", description = "Get info about a player.")
@Permission(Permissions.CheckCommand)
public class CheckCommand extends CommandExecutor {
	private String string = Utilities.string;
	private String variable1 = Utilities.variable1;

	public CheckCommand() {
		register("check",
				Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
	}
	
	@SneakyThrows
	@SubCommand("check")
	public void check(CommandSender sender, OfflinePlayer target) {

		DBConnection database = VaultCore.getDatabase();

		ResultSet rs = database.executeQueryStatement(
				"SELECT uuid, username, firstseen, lastseen, rank, ip FROM players WHERE username=?", target.getName());
		if (!rs.next()) {
			sender.sendMessage(ChatColor.RED + "That player has never joined the server.");
			return;
		}
		String uuid = rs.getString("uuid");
		String username = VaultCoreAPI.getName(target);
		long firstseen = rs.getLong("firstseen");
		long lastseen = rs.getLong("lastseen");
		String rank = WordUtils.capitalize(rs.getString("rank"));
		String ip = rs.getString("ip");

		sender.sendMessage(ChatColor.DARK_GREEN + "--== [Check] ==--");

		if (target.isOnline()) {
			sender.sendMessage(string + "Checking: " + username);
		} else {
			sender.sendMessage(
					string + "Checking: " + username + ChatColor.GRAY + " " + ChatColor.ITALIC + "[OFFLINE]");
		}
		sender.sendMessage(string + "UUID: " + variable1 + uuid);
		sender.sendMessage(string + "First Seen (D/M/Y): " + variable1 + Utilities.millisToDate(firstseen));
		sender.sendMessage(string + "Last Seen (D/M/Y): " + variable1 + Utilities.millisToDate(lastseen));
		sender.sendMessage(string + "Last IP: " + variable1 + ip);
		sender.sendMessage(string + "Rank: " + variable1 + rank);
		sender.sendMessage(
				string + "Database: " + variable1 + "https://database.vaultmc.net/?user=" + target.getName());
		Bukkit.getServer().dispatchCommand(sender, "tag list " + target.getName());

	}
}