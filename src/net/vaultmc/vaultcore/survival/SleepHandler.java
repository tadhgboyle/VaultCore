/*
package net.vaultmc.vaultcore.survival;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;

public class SleepHandler implements Listener {

	int sleeping = 0;
	int survival = 0;

	@EventHandler
	public void onPlayerSleep(PlayerBedEnterEvent e) {

		if (e.getPlayer().getWorld().getName().equals("Survival")) {

			World world = e.getPlayer().getWorld();

			if (!world.isDayTime() && e.getPlayer().getGameMode() == GameMode.SURVIVAL) {

				sleeping++;

				for (Player survivalPlayers : world.getPlayers()) {

					if (survivalPlayers.getGameMode() == GameMode.SURVIVAL) {
						survival++;
						Bukkit.broadcastMessage(survival + " debuggin'");
					}
				}
				survival--;
				int required = survival / 2;
				if (sleeping > required) {
					Bukkit.broadcastMessage(sleeping + " is more than " + required);
					world.setTime(23450);
					world.setStorm(false);
					world.setThundering(false);
					for (Player survivalPlayers : world.getPlayers()) {
						survivalPlayers.sendMessage(VaultLoader.getMessage("vaultcore.survival.time_set_day"));
					}
					sleeping = 0;
					survival = 0;
				} else {
					Bukkit.broadcastMessage(sleeping + " is not more than " + required);
					for (Player survivalPlayers : world.getPlayers()) {
						survivalPlayers.sendActionBar(
								Utilities.formatMessage(VaultLoader.getMessage("vaultcore.survival.players_needed"),
										((survival / 2) - sleeping)));
					}
				}
			}
		}
	}
}
*/