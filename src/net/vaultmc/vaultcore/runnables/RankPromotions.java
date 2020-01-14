package net.vaultmc.vaultcore.runnables;

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

public class RankPromotions {

	static String string = Utilities.string;
	static String variable1 = Utilities.variable1;

	public static void memberPromotion() {

		for (Player player : Bukkit.getOnlinePlayers()) {

			String group = VaultCore.getChat().getPrimaryGroup(player);

			if (!group.equalsIgnoreCase("default")) {
				return;
			}
			int playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
			// 10 hours in ticks
			if (playtime > 720000) {

				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent set member");
				for (Player players : Bukkit.getOnlinePlayers()) {
					players.sendMessage(VaultCoreAPI.getName(player) + string + " has been promoted to " + variable1
							+ "Member" + string + "!");
				}
			}
		}
	}

	public static void patreonPromotion() {

		for (Player player : Bukkit.getOnlinePlayers()) {

			String group = VaultCore.getChat().getPrimaryGroup(player);

			if (!group.equalsIgnoreCase("member")) {
				return;
			}
			int playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
			// 35 hours in ticks
			if (playtime > 2520000) {

				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
						"lp user " + player.getName() + " parent set patreon");
				for (Player players : Bukkit.getOnlinePlayers()) {
					players.sendMessage(VaultCoreAPI.getName(player) + string + " has been promoted to " + variable1
							+ "Patreon" + string + "!");
				}
			}
		}
	}
}