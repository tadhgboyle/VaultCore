package net.vaultmc.vaultcore.settings;

import net.vaultmc.vaultloader.utils.configuration.SQLPlayerData;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

public class PlayerSettings {

    public static boolean getSetting(VLPlayer player, String setting) {
        SQLPlayerData data = player.getPlayerData();
        boolean value = data.getBoolean(setting);
        if (value == true) return true;
        else return false;
    }
}
