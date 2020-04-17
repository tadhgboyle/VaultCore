package net.vaultmc.vaultcore.settings;

import net.vaultmc.vaultloader.utils.configuration.SQLPlayerData;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

public class PlayerSettings {

    public static boolean getSetting(VLPlayer player, String setting) {
        SQLPlayerData data = player.getPlayerData();
        return data.getBoolean(setting);
    }
}
