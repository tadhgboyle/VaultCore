package net.vaultmc.vaultcore.misc.runnables;

import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class PlayerNames {
    private PlayerNames() {
    }

    public static void updatePlayerNames() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            String username = VLPlayer.getPlayer(players).getName();
            String prefix = ChatColor.translateAlternateColorCodes('&', VLPlayer.getPlayer(players).getPrefix());
            players.setDisplayName(prefix + username);
            VLPlayer.getPlayer(players).setPlayerListName(prefix + username);
        }
    }
}
