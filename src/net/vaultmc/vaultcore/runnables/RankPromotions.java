package net.vaultmc.vaultcore.runnables;

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

public class RankPromotions {
	static String string = Utilities.string;
	static String variable1 = Utilities.variable1;

	public static void memberPromotion() {
		for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
			String group = player.getGroup();
			if (!group.equalsIgnoreCase("default")) {
				continue;
			}
			int playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
			// 10 hours in ticks
			if (playtime > 720000) {

				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent set member");
				for (Player players : Bukkit.getOnlinePlayers()) {
					players.sendMessage(player.getFormattedName() + string + " has been promoted to " + variable1
							+ "Member" + string + "!");
				}
			}
		}
	}

	public static void patreonPromotion() {
		for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
			String group = player.getGroup();
			if (!group.equalsIgnoreCase("member")) {
				continue;
			}
			int playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
			// 35 hours in ticks
			if (playtime > 2520000) {

				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
						"lp user " + player.getName() + " parent set patreon");
				for (Player players : Bukkit.getOnlinePlayers()) {
					players.sendMessage(player.getFormattedName() + string + " has been promoted to " + variable1
							+ "Patreon" + string + "!");
				}
			}
		}
	}
}