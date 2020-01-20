package net.vaultmc.vaultcore.commands;

import java.sql.ResultSet;
import java.util.Collections;

import org.bukkit.Statistic;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.commands.Aliases;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

@RootCommand(literal = "playtime", description = "Check yourself or other's play time.")
@Permission(Permissions.PlayTime)
@Aliases("pt")
public class PlayTime extends CommandExecutor {
	
	public PlayTime() {
		register("playTimeSelf", Collections.emptyList());
		register("playTimeOthers",
				Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
	}

	@SubCommand("playTimeSelf")
	@PlayerOnly
	public void checkPlayTimeSelf(VLPlayer sender) {
		printPlayTimeOnline(sender, sender);
	}

	@SubCommand("playTimeOthers")
	@Permission(Permissions.PlayTimeOther)
	public void checkPlayTimeOthers(VLCommandSender sender, VLOfflinePlayer target) {
		if (target.isOnline()) {
			printPlayTimeOnline(target.getOnlinePlayer(), sender);
			return;
		}
		printPlayTimeOffline(sender, target);
	}

	private void printPlayTimeOnline(VLPlayer player, VLCommandSender sender) {
		long t = (long) (player.getStatistic(Statistic.PLAY_ONE_MINUTE) * 0.05 * 1000);
		long[] time = Utilities.millisToTime(t);
		sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.playtime.online_player"),
				player.getFormattedName(), time[0] + "", time[1] + "", time[2] + ""));
	}

	@SneakyThrows
	private void printPlayTimeOffline(VLCommandSender player, VLOfflinePlayer target) {
		DBConnection database = VaultCore.getDatabase();

		ResultSet rs = database.executeQueryStatement("SELECT username, playtime FROM players WHERE username=?",
				target.getName());
		if (!rs.next()) {
			player.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
			return;
		}

		long playtime = rs.getLong("playtime");
		long t = (long) (playtime * 0.05 * 1000);
		long[] time = Utilities.millisToTime(t);
		player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.playtime.offline_player"),
				target.getFormattedName(), time[0] + "", time[1] + "", time[2] + ""));
	}
}