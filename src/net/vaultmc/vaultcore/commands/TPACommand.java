package net.vaultmc.vaultcore.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;

public class TPACommand implements CommandExecutor {

	private static HashMap<UUID, UUID> requests = new HashMap<>();
	private static HashMap<UUID, UUID> requestsHere = new HashMap<>();

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		String string = ChatColor.translateAlternateColorCodes('&',
				VaultCore.getInstance().getConfig().getString("string"));
		String variable1 = ChatColor.translateAlternateColorCodes('&',
				VaultCore.getInstance().getConfig().getString("variable-1"));

		if (!(sender instanceof Player)) {
			sender.sendMessage(Utilities.consoleError());
			return true;
		}

		Player player = (Player) sender;
		if (command.getName().equalsIgnoreCase("tpa")) {

			if (!player.hasPermission(Permissions.TPACommand)) {
				sender.sendMessage(Utilities.noPermission());
				return true;
			}
			if (args.length != 1) {
				player.sendMessage(Utilities.usageMessage(command.getName(), "<player>"));
				return true;
			}

			Player target = Bukkit.getPlayer(args[0]);

			if (target == player) {
				player.sendMessage(ChatColor.RED + "You can't teleport to yourself!");
				return true;
			}
			if (target == null) {
				player.sendMessage(ChatColor.RED + "That player is offline!");
				return true;
			}
			if (VaultCore.getInstance().getPlayerData()
					.getBoolean("players." + target.getUniqueId() + ".settings.tpa") == false) {
				player.sendMessage(ChatColor.RED + "That player has disabled TPAs!");
				return true;
			}
			if (VaultCore.getInstance().getPlayerData()
					.getBoolean("players." + target.getUniqueId() + ".settings.autotpa") == true) {
				player.teleport(target);
				player.sendMessage(string + "Teleported to " + variable1 + target.getName() + string + ".");
				target.sendMessage(variable1 + player.getName() + string + " has teleported to you.");
				return true;
			}

			else {
				requests.put(target.getUniqueId(), player.getUniqueId());
				player.sendMessage(
						string + "You sent a teleport request to " + variable1 + target.getName() + string + ".");
				target.sendMessage(variable1 + player.getName() + string + " sent you a teleport request, type "
						+ variable1 + "/tpaccept " + string + "to accept it.");
				return true;
			}
		}
		if (command.getName().equalsIgnoreCase("tpahere")) {

			if (!player.hasPermission(Permissions.TPAHereCommand)) {
				sender.sendMessage(Utilities.noPermission());
				return true;
			}
			if (args.length != 1) {
				player.sendMessage(Utilities.usageMessage(command.getName(), "<player>"));
				return true;
			}
			Player target = Bukkit.getPlayer(args[0]);

			if (target == player) {
				player.sendMessage(ChatColor.RED + "You can't teleport to yourself!");
				return true;
			}
			if (target == null) {
				player.sendMessage(ChatColor.RED + "That player is offline!");
				return true;
			}
			if (VaultCore.getInstance().getPlayerData()
					.getBoolean("players." + target.getUniqueId() + ".settings.tpa") == false) {
				player.sendMessage(ChatColor.RED + "That player has disabled TPAs!");
				return true;
			}
			if (VaultCore.getInstance().getPlayerData()
					.getBoolean("players." + target.getUniqueId() + ".settings.autotpa") == true) {
				player.teleport(target);
				player.sendMessage(string + "Teleported to " + variable1 + target.getName() + string + ".");
				target.sendMessage(variable1 + player.getName() + string + " has teleported to you.");
				return true;
			} else {
				requestsHere.put(target.getUniqueId(), player.getUniqueId());
				player.sendMessage(
						string + "You requested that " + variable1 + target.getName() + string + " teleports to you.");
				target.sendMessage(variable1 + player.getName() + string + " asked you to teleport to them, type "
						+ variable1 + "/tpaccept " + string + "to accept it.");
				return true;
			}
		}
		if (command.getName().equalsIgnoreCase("tpaccept")) {

			if (!player.hasPermission(Permissions.TPACommand)) {
				sender.sendMessage(Utilities.noPermission());
				return true;
			}
			if (args.length != 0) {
				player.sendMessage(Utilities.usageMessage(command.getName(), ""));
				return true;
			}
			if (requests.containsKey(player.getUniqueId())) {
				player.sendMessage(string + "You have accepted the teleport request.");
				Bukkit.getPlayer(requests.get(player.getUniqueId()))
						.sendMessage(variable1 + player.getName() + string + " accepted your teleport request.");
				Bukkit.getPlayer(requests.get(player.getUniqueId())).teleport(player);
				requests.remove(player.getUniqueId());
				return true;
			}
			if (requestsHere.containsKey(player.getUniqueId())) {

				Player target = Bukkit.getPlayer(requestsHere.get(player.getUniqueId()));

				player.sendMessage(string + "You have accepted the teleport request.");
				target.sendMessage(variable1 + player.getName() + string + " accepted your teleport request.");
				player.teleport(target);
				requestsHere.remove(player.getUniqueId());
				return true;
			} else {
				player.sendMessage(ChatColor.RED + "You don't have a pending request!");
				return true;
			}
		}
		if (command.getName().equalsIgnoreCase("tpdeny")) {

			if (!player.hasPermission(Permissions.TPACommand)) {
				sender.sendMessage(Utilities.noPermission());
				return true;
			}
			if (args.length != 0) {
				player.sendMessage(Utilities.usageMessage(command.getName(), ""));
				return true;
			}
			if (requests.containsKey(player.getUniqueId())) {

				player.sendMessage(string + "You denied the teleport request.");
				Bukkit.getPlayer(requests.get(player.getUniqueId()))
						.sendMessage(variable1 + player.getName() + string + " denied your teleport request.");
				requests.remove(player.getUniqueId());
				return true;
			}
			player.sendMessage(ChatColor.RED + "You don't have a pending request!");
			return true;
		}
		return false;
	}
}