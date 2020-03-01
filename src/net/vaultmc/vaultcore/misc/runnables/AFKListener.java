package net.vaultmc.vaultcore.misc.runnables;

import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class AFKListener {

    private static Map<VLPlayer, Location> locations = new HashMap<>();

    public static void afkUpdater() {
        for (VLPlayer players : VLPlayer.getOnlinePlayers()) {
            if (locations.get(players) == null) {
                locations.put(players, players.getLocation());
            } else if (locations.get(players) == players.getLocation()) {
                players.performCommand("/afk");
            }
        }
    }
}
