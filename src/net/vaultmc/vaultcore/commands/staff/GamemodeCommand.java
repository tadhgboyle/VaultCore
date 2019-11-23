package net.vaultmc.vaultcore.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;

public class GamemodeCommand implements CommandExecutor {

	String string = (ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("string")));
	String variable1 = (ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-1")));

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(Utilities.consoleError());
			return true;
		}

		if (!sender.hasPermission(Permissions.GamemodeCommand)) {
			sender.sendMessage(Utilities.noPermission());
			return true;
		}

		Player player = (Player) sender;

		if (command.getName().equalsIgnoreCase("gamemode")) {

			if (args.length > 2 || args.length == 0) {
				player.sendMessage(Utilities.usageMessage(command.getName(), "<gamemode> [player]"));
				return true;
			}

			if (args.length == 1) {

				if (args[0].equalsIgnoreCase("creative")) {
					setGameModeSelf(player, GameMode.CREATIVE);
					return true;
				}
				if (args[0].equalsIgnoreCase("survival")) {
					setGameModeSelf(player, GameMode.SURVIVAL);
					return true;
				}
				if (args[0].equalsIgnoreCase("spectator")) {
					setGameModeSelf(player, GameMode.SPECTATOR);
					return true;
				} else {
					player.sendMessage(ChatColor.DARK_RED + "Invalid Gamemode!");
					return true;
				}
			}

			if (args.length == 2) {

				Player target = Bukkit.getPlayer(args[1]);
				if (target == null) {
					player.sendMessage(ChatColor.RED + "That player is offline!");
					return true;
				}
				if (args[0].equalsIgnoreCase("creative")) {
					setGameModeOther(player, target, GameMode.CREATIVE);
					return true;
				}
				if (args[0].equalsIgnoreCase("survival")) {
					setGameModeOther(player, target, GameMode.SURVIVAL);
					return true;
				}
				if (args[0].equalsIgnoreCase("spectator")) {
					setGameModeOther(player, target, GameMode.SPECTATOR);
					return true;
				} else {
					player.sendMessage(ChatColor.RED + "Invalid Gamemode!");
					return true;
				}
			}
		}

		if (command.getName().equalsIgnoreCase("gmc")) {
			setGameModeSelf(player, GameMode.CREATIVE);
			return true;
		}
		if (command.getName().equalsIgnoreCase("gms")) {
			setGameModeSelf(player, GameMode.SURVIVAL);
			return true;
		}
		if (command.getName().equalsIgnoreCase("gmsp")) {
			setGameModeSelf(player, GameMode.SPECTATOR);
			return true;
		}
		return true;
	}

	public void setGameModeSelf(Player player, GameMode gamemode) {

		if (player.hasPermission("vc.gamemode." + gamemode.toString())) {
			player.setGameMode(gamemode);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					string + "Your gamemode has been set to " + variable1
							+ gamemode.toString().toLowerCase().substring(0, 1).toUpperCase()
							+ gamemode.toString().toLowerCase().substring(1) + string + "."));
		} else {
			player.sendMessage(Utilities.noPermission());
		}
	}

	public void setGameModeOther(Player player, Player target, GameMode gamemode) {

		if (player.hasPermission("vc.gamemode." + gamemode.toString())) {
			if (player.hasPermission(Permissions.GamemodeCommandOther)) {
				target.setGameMode(gamemode);
				player.sendMessage(variable1 + "" + target.getName() + string + "'s gamemode has been set to "
						+ variable1 + gamemode.toString().toLowerCase().substring(0, 1).toUpperCase()
						+ gamemode.toString().toLowerCase().substring(1) + string + ".");
				target.sendMessage(variable1 + "" + player.getName() + string + " has set your gamemode to " + variable1
						+ gamemode.toString().toLowerCase().substring(0, 1).toUpperCase()
						+ gamemode.toString().toLowerCase().substring(1) + string + ".");
			}
		} else {
			player.sendMessage(Utilities.managePlayerError("gamemode"));
		}
	}
}