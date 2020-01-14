package net.vaultmc.vaultcore.commands;

import java.sql.ResultSet;
import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;

@RootCommand(literal = "stats", description = "Check statistics of yourself or other players.")
@Permission(Permissions.StatsCommand)
public class StatsCommand extends CommandExecutor {

	private String string = Utilities.string;
	private String variable2 = Utilities.variable2;

	public StatsCommand() {
		register("statsSelf", Collections.emptyList());
		register("statsOthers",
				Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
	}

	@SubCommand("statsSelf")
	@PlayerOnly
	public void statsSelf(CommandSender sender) {
		Player player = (Player) sender;
		viewStats(player, player);
	}

	@SubCommand("statsOthers")
	@Permission(Permissions.StatsCommandOther)
	public void statsOthers(CommandSender sender, Player target) {
		viewStats(sender, target);
	}

	@SneakyThrows
	private void viewStats(CommandSender sender, OfflinePlayer target) {
		DBConnection database = VaultCore.getDatabase();

		ResultSet check = database.executeQueryStatement("SELECT username FROM players WHERE username=?",
				target.getName());
		if (!check.next()) {
			sender.sendMessage(ChatColor.RED + "This player has never joined before!");
			return;
		}
		ResultSet stats = database.executeQueryStatement(
				"SELECT COUNT(session_id) AS sessions, AVG(duration) AS duration FROM sessions WHERE username=?",
				target.getName());
		if (!stats.next()) {
			sender.sendMessage(ChatColor.RED + "An error has occured, please contact an Administrator.");
			return;
		}
		long [] duration = Utilities.millisToTime(stats.getInt("duration"));
		sender.sendMessage(ChatColor.DARK_GREEN + "--== [Stats] ==--");
		sender.sendMessage(VaultCoreAPI.getName(target) + string + " has joined " + variable2
				+ stats.getString("sessions") + string + " times.");
		sender.sendMessage(VaultCoreAPI.getName(target) + string + "'s average session duration is " + variable2
				+ duration + string + ".");
	}
}