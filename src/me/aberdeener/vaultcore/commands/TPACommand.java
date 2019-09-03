package me.aberdeener.vaultcore.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;

public class TPACommand implements CommandExecutor {

	private static HashMap<UUID, UUID> requests = new HashMap<>();
	private static HashMap<UUID, UUID> requestsHere = new HashMap<>();

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		String string = VaultCore.getInstance().getConfig().getString("string");
		String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");

		// console sender check
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					VaultCore.getInstance().getConfig().getString("console-error")));
			return true;
		}

		// request command
		Player player = (Player) sender;
		if (command.getName().equalsIgnoreCase("tpa")) {

			// permission check
			if (!player.hasPermission("vc.tpa")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}

			if (args.length != 1) {
				player.sendMessage(ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/tpa <player>");
				return true;
			}

			Player target = Bukkit.getPlayer(args[0]);

			// if the player is the sender...
			if (target == player) {
				player.sendMessage(ChatColor.RED + "You can't teleport to yourself!");
				return true;
			}

			// if the player is offline...
			if (target == null) {
				player.sendMessage(ChatColor.RED + "That player is offline!");
				return true;
			}

			// if the player is, in fact online...
			else {
				// insert them into the hashmap via uuid
				requests.put(target.getUniqueId(), player.getUniqueId());
				// send messages to either party
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						string + "You sent a teleport request to " + variable1 + target.getName() + string + "."));
				target.sendMessage(ChatColor.translateAlternateColorCodes('&', variable1 + player.getName() + string
						+ " sent you a teleport request, type " + variable1 + "/tpaccept " + string + "to accept it."));
				return true;
			}
		}

		// request command
		if (command.getName().equalsIgnoreCase("tpahere")) {

			// permission check
			if (!player.hasPermission("vc.tpahere")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}

			if (args.length != 1) {
				player.sendMessage(ChatColor.DARK_GREEN + "Correct Usage: " + ChatColor.RED + "/tpahere <player>");
				return true;
			}

			Player target = Bukkit.getPlayer(args[0]);

			// if the player is the sender...
			if (target == player) {
				player.sendMessage(ChatColor.RED + "You can't teleport to yourself!");
				return true;
			}

			// if the player is offline...
			if (target == null) {
				player.sendMessage(ChatColor.RED + "That player is offline!");
				return true;
			}

			// if the player is, in fact online...
			else {
				// insert them into the hashmap via uuid
				requestsHere.put(target.getUniqueId(), player.getUniqueId());
				// send messages to either party
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						string + "You requested that " + variable1 + target.getName() + string + " teleports to you."));
				target.sendMessage(ChatColor.translateAlternateColorCodes('&',
						variable1 + player.getName() + string + " asked you to teleport to them, type " + variable1
								+ "/tpaccept " + string + "to accept it."));
				return true;
			}
		}

		// tpaccept command
		if (command.getName().equalsIgnoreCase("tpaccept")) {

			// if the hashmap contains the accepter...
			if (requests.containsKey(player.getUniqueId())) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						string + "You have accepted the teleport request."));
				Bukkit.getPlayer(requests.get(player.getUniqueId())).sendMessage(ChatColor.translateAlternateColorCodes(
						'&', variable1 + player.getName() + string + " accepted your teleport request."));

				// get the target and teleport them to the sender
				Bukkit.getPlayer(requests.get(player.getUniqueId())).teleport(player);
				requests.remove(player.getUniqueId());
				return true;
			}

			// if the hashmap contains the accepter...
			if (requestsHere.containsKey(player.getUniqueId())) {
				
				Player target = Bukkit.getPlayer(requestsHere.get(player.getUniqueId()));
				
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						string + "You have accepted the teleport request."));
				
				target.sendMessage(ChatColor.translateAlternateColorCodes('&',
								variable1 + player.getName() + string + " accepted your teleport request."));

				// get the target and teleport them to the sender
				player.teleport(target);
				requestsHere.remove(player.getUniqueId());
				return true;
			}

			else {
				// if they dont have an incoming request
				player.sendMessage(ChatColor.RED + "You don't have a pending request!");
				return true;
			}
		}

		// tpdeny command rip
		if (command.getName().equalsIgnoreCase("tpdeny")) {

			// check players are in the hashmap and tell both parties it was declined
			if (requests.containsKey(player.getUniqueId())) {
				
				player.sendMessage(
						ChatColor.translateAlternateColorCodes('&', string + "You denied the teleport request."));
				
				Bukkit.getPlayer(requests.get(player.getUniqueId())).sendMessage(ChatColor.translateAlternateColorCodes(
						'&', variable1 + player.getName() + string + " denied your teleport request."));
				
				// remove from hashmap
				requests.remove(player.getUniqueId());
				return true;
			}

			// if the players are not in the hashmap, tell them so
			player.sendMessage(ChatColor.RED + "You don't have a pending request!");
			return true;
		}

		return false;
	}
}