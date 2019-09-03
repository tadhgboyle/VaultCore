package me.aberdeener.vaultcore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.aberdeener.vaultcore.VaultCore;

public class MuteChat implements CommandExecutor, Listener {

	public static boolean mutechat = false;

	@EventHandler
	public void muteChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if ((mutechat) && (!p.hasPermission("vc.mutechat.override"))) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + "The chat is muted!");
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		String string = VaultCore.getInstance().getConfig().getString("string");
		String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");

		if (cmd.getName().equalsIgnoreCase("mutechat")) {

			if (sender instanceof ConsoleCommandSender) {
				if (mutechat) {
					mutechat = false;
					Bukkit.broadcastMessage(
							ChatColor.translateAlternateColorCodes('&', string + "The chat has been unmuted!"));

				} else {
					mutechat = true;
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
							variable1 + "CONSOLE " + string + "has muted the chat!"));
				}
			}

			else if (sender instanceof Player) {

				Player player = (Player) sender;

				if (!player.hasPermission("vc.mutechat")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							VaultCore.getInstance().getConfig().getString("no-permission")));
					return true;
				}

				if (mutechat) {
					mutechat = false;
					Bukkit.broadcastMessage(
							ChatColor.translateAlternateColorCodes('&', string + "The chat has been unmuted!"));

				} else {
					mutechat = true;
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
							string + "The chat has been muted by " + variable1 + sender.getName()));
				}
			}
			return true;
		}
		return true;
	}
}