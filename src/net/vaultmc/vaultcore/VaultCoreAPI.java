package net.vaultmc.vaultcore;

import org.bukkit.entity.Player;

public class VaultCoreAPI {

	// yang requested this for clans
	public static Boolean PWCCheck(Player player) {
		Boolean pwc = VaultCore.getInstance().getPlayerData()
				.getBoolean("players." + player.getUniqueId() + ".settings.pwc");
		return pwc;
	}

	public static String rankCheck(Player player) {
		return VaultCore.getChat().getPrimaryGroup(player);
	}

	public static Boolean hasPermission(Player player, String permission) {
		boolean hasPermission;
		if (player.hasPermission(permission)) {
			hasPermission = true;
		} else {
			hasPermission = false;
		}
		return hasPermission;
	}
}
