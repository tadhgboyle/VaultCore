package net.vaultmc.vaultcore.misc.runnables;

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

public class RankPromotions {

    public static int MEMBER_TIME = 720000;
    public static int PATREON_TIME = 2520000;

    public static void memberPromotion() {
        for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
            String group = player.getGroup();
            if (!group.equalsIgnoreCase("default")) {
                continue;
            }
            int playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
            // 10 hours in ticks
            if (playtime > MEMBER_TIME) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent set member");

                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.sendMessage(
                            Utilities.formatMessage(VaultLoader.getMessage("vaultcore.runnables.rank_promotions"),
                                    player.getFormattedName(), "Member"));
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
            if (playtime > PATREON_TIME) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        "lp user " + player.getName() + " parent set patreon");
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.sendMessage(
                            Utilities.formatMessage(VaultLoader.getMessage("vaultcore.runnables.rank_promotions"),
                                    player.getFormattedName(), "Patreon"));
                }
            }
        }
    }
}