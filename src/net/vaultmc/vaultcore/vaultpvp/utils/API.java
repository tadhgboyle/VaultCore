package net.vaultmc.vaultcore.vaultpvp.utils;

import net.vaultmc.vaultloader.utils.player.VLPlayer;

public class API {

    public static double getCoins(VLPlayer p) {
        return p.getDataConfig().getDouble("player-coins");
    }

    public static void setCoins(VLPlayer p, double amount) {
        p.getDataConfig().set("player-coins", amount);
        p.saveData();
    }
}
