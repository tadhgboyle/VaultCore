package net.vaultmc.vaultcore.commands.teleport;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultloader.utils.commands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

@RootCommand(literal = "tpa", description = "Request to teleport to a player.")
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPACommand extends CommandExecutor {
	@Getter
	private static HashMap<UUID, UUID> requests = new HashMap<>();
	@Getter
	private static HashMap<UUID, UUID> requestsHere = new HashMap<>();

	String string = Utilities.string;
	String variable1 = Utilities.variable1;

	public TPACommand() {
		register("tpa", Collections.singletonList(Arguments.createArgument("player", Arguments.playerArgument())));
	}

	@SubCommand("tpa")
	public void tpa(CommandSender sender, Player target) {

		Player player = (Player) sender;
		if (target == player) {
			player.sendMessage(ChatColor.RED + "You can't teleport to yourself!");
			return;
		}
		if (!VaultCore.getInstance().getPlayerData().getBoolean("players." + target.getUniqueId() + ".settings.tpa")) {
			player.sendMessage(ChatColor.RED + "That player has disabled TPAs!");
			return;
		} else if (VaultCore.getInstance().getPlayerData()
				.getBoolean("players." + target.getUniqueId() + ".settings.autotpa")) {
			player.teleport(target);
			player.sendMessage(string + "Teleported to " + VaultCoreAPI.getName(target) + string + ".");
			target.sendMessage(VaultCoreAPI.getName(player) + string + " has teleported to you.");
		} else {
			requests.put(target.getUniqueId(), player.getUniqueId());
			player.sendMessage(
					string + "You sent a teleport request to " + VaultCoreAPI.getName(target) + string + ".");
			target.sendMessage(VaultCoreAPI.getName(player) + string + " sent you a teleport request, type " + variable1
					+ "/tpaccept " + string + "to accept it.");
		}
	}
}