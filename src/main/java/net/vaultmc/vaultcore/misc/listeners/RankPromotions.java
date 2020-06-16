/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

package net.vaultmc.vaultcore.misc.listeners;

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;

public class RankPromotions {

    public static int MEMBER_TIME = 1080000;
    public static int PATREON_TIME = 2520000;

    public static void memberPromotion() {
        for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
            String group = player.getGroup();
            if (!group.equalsIgnoreCase("default")) {
                continue;
            }
            int playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
            // 10 hours in ticks
            if (playtime >= MEMBER_TIME) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        "lp user " + player.getName() + " parent remove default");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent set member");

                for (VLPlayer players : VLPlayer.getOnlinePlayers()) {
                    if (PlayerSettings.getSetting(players, "minimal_chat")) continue;
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
            if (playtime >= PATREON_TIME) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        "lp user " + player.getName() + " parent remove member");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        "lp user " + player.getName() + " parent set patreon");

                for (VLPlayer players : VLPlayer.getOnlinePlayers()) {
                    if (PlayerSettings.getSetting(players, "minimal_chat")) continue;
                    players.sendMessage(
                            Utilities.formatMessage(VaultLoader.getMessage("vaultcore.runnables.rank_promotions"),
                                    player.getFormattedName(), "Patreon"));
                }
            }
        }
    }
}