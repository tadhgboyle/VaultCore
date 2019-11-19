package net.vaultmc.vaultcore;

import org.bukkit.entity.Player;

public class VaultCoreAPI {

	//yang requested this for clans
	public static Boolean PWCCheck(Player player) {
		Boolean pwc = VaultCore.getInstance().getPlayerData()
				.getBoolean("players." + player.getUniqueId() + ".settings.pwc");
		return pwc;
	}
}
