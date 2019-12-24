package net.vaultmc.vaultcore;

import net.vaultmc.vaultutils.VaultUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class VaultCoreAPI {
    public static String getName(Player player) {
        return VaultUtils.getName(player);
    }

    public static String getName(OfflinePlayer player) {
        if (player.isOnline()) return getName(player.getPlayer());
        return VaultUtils.getName(player);
    }
}
