package net.vaultmc.vaultcore.vanish;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RootCommand(literal = "vanish", description = "Toggles your vanish state or somebody else's.")
@Permission(Permissions.VanishCommand)
@Aliases("v")
public class VanishCommand extends CommandExecutor {
	public static final Map<UUID, Boolean> vanished = new HashMap<>();

	public VanishCommand() {
		register("vanishSelf", Collections.emptyList());
		register("vanishOthers",
				Collections.singletonList(Arguments.createArgument("player", Arguments.playerArgument())));
	}

	public static void setVanishState(VLPlayer player, boolean vanish) {
		if (vanish) {
			vanished.put(player.getUniqueId(), true);

			for (VLPlayer i : VLPlayer.getOnlinePlayers()) {
				if (i == player)
					continue;
				if (i.hasPermission(Permissions.VanishCommand))
					continue;
				i.getPlayer().hidePlayer(VaultCore.getInstance().getBukkitPlugin(), player.getPlayer());
			}
		} else {
			vanished.remove(player.getUniqueId());

			for (VLPlayer i : VLPlayer.getOnlinePlayers()) {
				if (i == player)
					continue;
				i.getPlayer().showPlayer(VaultCore.getInstance().getBukkitPlugin(), player.getPlayer());
			}
		}
		player.setTemporaryData("vanished", vanish);
	}

	public static void update(VLPlayer player) {
		for (Map.Entry<UUID, Boolean> x : vanished.entrySet()) {
			if (x.getKey().toString().equals(player.getUniqueId().toString()))
				continue;
			if (VLPlayer.getPlayer(x.getKey()) != null && x.getValue()) {
				player.getPlayer().hidePlayer(VaultCore.getInstance().getBukkitPlugin(), Bukkit.getPlayer(x.getKey()));
			}
		}
	}

	@SubCommand("vanishSelf")
	@PlayerOnly
	public void vanishSelf(VLPlayer sender) {
		boolean newValue = !vanished.getOrDefault(sender.getUniqueId(), false);
		String state = newValue ? VaultLoader.getMessage("vanish.invisible") : VaultLoader.getMessage("vanish.visible");
		String message = newValue ? ChatColor.RED + "left" : ChatColor.GREEN + "joined";
		sender.sendMessage(VaultLoader.getMessage("vanish.player-state").replace("{STATE}", state));

		for (Player players : Bukkit.getOnlinePlayers()) {
			if (players.hasPermission(Permissions.VanishCommand)
					&& players.hasPermission(Permissions.StaffChatCommand)) {
				players.sendMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.prefix") + Utilities
						.formatMessage(VaultLoader.getMessage("vanish.staff_message"), sender.getName(), state));
			}
			players.sendMessage(
					Utilities.formatMessage(VaultLoader.getMessage("vaultcore.listeners.joinquit.event_message"),
							sender.getFormattedName(), message));
		}
		setVanishState(sender, newValue);
	}

	@SubCommand("vanishOthers")
	public void vanishOthers(VLCommandSender sender, VLPlayer player) {
		boolean newValue = !vanished.getOrDefault(player.getUniqueId(), false);
		String state = newValue ? VaultLoader.getMessage("vanish.invisible") : VaultLoader.getMessage("vanish.visible");
		player.sendMessage(VaultLoader.getMessage("vanish.player-state").replace("{STATE}", state));
		sender.sendMessage(VaultLoader.getMessage("vanish.others-state").replace("{STATE}", state).replace("{PLAYER}", player.getFormattedName()));
		setVanishState(player, newValue);
	}
}
