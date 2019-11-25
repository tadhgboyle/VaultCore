package net.vaultmc.vaultcore.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;
import net.vaultmc.vaultutils.utils.commands.experimental.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mojang.brigadier.arguments.StringArgumentType;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;

@RootCommand(literal = "msg", description = "Send a player a message.")
@Permission(Permissions.MsgCommand)
@PlayerOnly
public class MsgCommand extends CommandExecutor {

	private static HashMap<UUID, UUID> replies = new HashMap<>();

	public MsgCommand() {
		this.register("msg", Arrays.asList(
				Commands.literal("msg"), 
				Commands.argument("target", EntityArgument.player(),
				Commands.argument("message", StringArgumentType.greedyString())), 
				"VaultCore"));
		
		this.register("r", Arrays.asList(
				Commands.literal("r"), 
				Commands.argument("message", StringArgumentType.greedyString()), 
				"VaultCore"));
	}

	@SubCommand("msg")
	public void msg(CommandSender sender, ArgumentProvider args) {

		Player player = (Player) sender;

		Player target = Bukkit.getPlayer(args.getString("target"));

		if (target == null) {
			player.sendMessage(ChatColor.RED + "That player is offline!");
		}
		if (target == player) {
			player.sendMessage(ChatColor.RED + "You can't message yourself!");
		}
		if (!VaultCore.getInstance().getPlayerData().getBoolean("players." + target.getUniqueId() + ".settings.msg")) {
			player.sendMessage(ChatColor.RED + "That player has disabled Messages!");
		} else {

			String message = args.getString("message");

			String meTo = (ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " -> " + ChatColor.GOLD
					+ target.getName() + ChatColor.YELLOW + ":");
			String toMe = (ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " -> " + ChatColor.GOLD
					+ target.getName() + ChatColor.YELLOW + ":");

			player.sendMessage(meTo + " " + ChatColor.DARK_GREEN + message);
			target.sendMessage(toMe + " " + ChatColor.DARK_GREEN + message);

			replies.put(target.getUniqueId(), player.getUniqueId());
		}
	}

	@SubCommand("r")
	public void r(CommandSender sender, ArgumentProvider args) {

		Player player = (Player) sender;

		Player target = Bukkit.getPlayer(replies.get(player.getUniqueId()));

		if (target == null) {
			player.sendMessage(ChatColor.RED + "That player is now offline!");
		}
		if (!VaultCore.getInstance().getPlayerData().getBoolean("players." + target.getUniqueId() + ".settings.msg")) {
			player.sendMessage(ChatColor.RED + "That player has disabled Messages!");
		}
		if (replies.containsKey(player.getUniqueId())) {
			String message = args.getString("message");

			String meTo = (ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " -> " + ChatColor.GOLD
					+ target.getName() + ChatColor.YELLOW + ":");
			String toMe = (ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " -> " + ChatColor.GOLD
					+ target.getName() + ChatColor.YELLOW + ":");
			player.sendMessage(meTo + " " + ChatColor.DARK_GREEN + message);
			target.sendMessage(toMe + " " + ChatColor.DARK_GREEN + message);
			replies.put(target.getUniqueId(), player.getUniqueId());
		} else {
			player.sendMessage(ChatColor.RED + "You do not have anyone to reply to!");
		}
	}
}
