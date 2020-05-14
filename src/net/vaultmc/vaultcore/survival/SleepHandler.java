package net.vaultmc.vaultcore.survival;

import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class SleepHandler extends ConstructorRegisterListener {
    private static final World world = Bukkit.getWorld("Survival");
    private static int sleepingPlayers = 0;

    private static void showActionbar() {
        if (sleepingPlayers < Math.round(world.getPlayerCount() / 2D)) {
            for (Player player : world.getPlayers()) {
                player.sendActionBar(VaultLoader.getMessage("vaultcore.survival.players_needed").replace("{PLAYERS}",
                        String.valueOf(Math.round(world.getPlayerCount() / 2D) - sleepingPlayers)));
            }
        }
    }

    @EventHandler
    public void onPlayerWantsToRestAndHaveADream(PlayerBedEnterEvent e) {
        if (e.getPlayer().getWorld() == world && e.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            sleepingPlayers++;
            showActionbar();

            if (sleepingPlayers >= Math.round(world.getPlayerCount() / 2D)) {
                world.setTime(0);
                world.setStorm(false);
                world.setThundering(false);
                sleepingPlayers = 0;
                for (Player player : world.getPlayers()) {
                    player.sendMessage(VaultLoader.getMessage("vaultcore.survival.time_set_day"));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerWakesUpEarlyToPlayMinecraft(PlayerBedLeaveEvent e) {
        if (e.getPlayer().getWorld().getName().equals("Survival")) {
            if (sleepingPlayers > 0) sleepingPlayers--;
            showActionbar();
        }
    }
}