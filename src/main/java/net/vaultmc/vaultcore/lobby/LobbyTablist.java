package net.vaultmc.vaultcore.lobby;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class LobbyTablist {

    public LobbyTablist() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(VaultCore.getInstance().getBukkitPlugin(), () -> {
            for(VLPlayer p : VLPlayer.getOnlinePlayers()) {
                showTablist(p);
            }
        },80L,80L);
    }

    private void showTablist(VLPlayer p) {
       p.getPlayer().setPlayerListHeader(ChatColor.GREEN + "You are playing on " + ChatColor.YELLOW + "" + ChatColor.BOLD + "VaultMC Network");
       p.getPlayer().setPlayerListFooter(ChatColor.YELLOW + "Website: " + ChatColor.GOLD + "https://vaultmc.net"
               + "\n" + ChatColor.YELLOW + "Discord: " + ChatColor.GOLD + "https://discord.gg/5Vwbfbj");
    }
}
