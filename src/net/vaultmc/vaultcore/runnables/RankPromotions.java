package net.vaultmc.vaultcore.runnables;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.VaultCore;

public class RankPromotions {

	static String string = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("string"));
	static String variable1 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-1"));

	public static void memberPromotion() {

		for (Player player : Bukkit.getOnlinePlayers()) {

			String group = VaultCore.getChat().getPrimaryGroup(player);

			if (!group.equalsIgnoreCase("default")) {
				return;
			}
			else {
				int playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;

				//10 hours
				if (playtime > 36000) {

					VaultCore.getInstance().getServer().dispatchCommand(
							VaultCore.getInstance().getServer().getConsoleSender(),
							"lp user " + player.getName() + " parent set member");
					for (Player players : Bukkit.getOnlinePlayers()) {
						players.sendMessage(variable1 + player.getName() + string + " has been promoted to " + variable1
								+ "Member" + string + "!");
					}
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

			else {
				int playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;

				//35 hours
				if (playtime > 126000) {

					VaultCore.getInstance().getServer().dispatchCommand(
							VaultCore.getInstance().getServer().getConsoleSender(),
							"lp user " + player.getName() + " parent set patreon");
					for (Player players : Bukkit.getOnlinePlayers()) {
						players.sendMessage(variable1 + player.getName() + string + " has been promoted to " + variable1
								+ "Patreon" + string + "!");
					}
				}
			}
		}
	}
}