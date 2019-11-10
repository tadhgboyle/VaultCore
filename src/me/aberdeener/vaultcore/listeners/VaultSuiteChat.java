package me.aberdeener.vaultcore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.aberdeener.vaultcore.VaultCore;
import me.aberdeener.vaultcore.commands.staff.StaffChat;

public class VaultSuiteChat implements Listener {

	private static String clansChatHook(Player player, String prefix, String name, String text) {
		try {
			String clan = (String) Class.forName("net.vaultmc.gameplay.clans.api.ClansAPI")
					.getDeclaredMethod("getCurrentClan", Player.class).invoke(player);
			return prefix + name + "<" + clan + ">" + ChatColor.DARK_GRAY + " → " + ChatColor.WHITE + text;
		} catch (ReflectiveOperationException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@EventHandler
	public void chatFormat(AsyncPlayerChatEvent event) {

		Player player = event.getPlayer();

		String groupPrefix = VaultCore.getChat().getPlayerPrefix(player);
		String prefix = ChatColor.translateAlternateColorCodes('&', groupPrefix);
		String name = player.getName();
		String text = event.getMessage();
		text = text.replace("%", "%%");

		String SCmessage = (prefix + name + ChatColor.DARK_GRAY + " » " + ChatColor.WHITE + ChatColor.AQUA
				+ ChatColor.translateAlternateColorCodes('&', text));
		String SCprefix = (ChatColor.translateAlternateColorCodes('&',
				VaultCore.getInstance().getConfig().getString("staffchat-prefix")));
		String staffchat = SCprefix + SCmessage;

		if (StaffChat.toggled.containsKey(player.getUniqueId())) {
			Bukkit.getConsoleSender().sendMessage(staffchat);
			for (Player players : Bukkit.getOnlinePlayers()) {
				if (players.hasPermission("vc.sc")) {
					players.sendMessage(staffchat.replaceFirst(",", ""));
					event.setCancelled(true);
				}
			}
		}

		if (event.getMessage().charAt(0) == ',') {
			if (event.getPlayer().hasPermission("vc.sc")) {
				Bukkit.getConsoleSender().sendMessage(staffchat);
				for (Player players : Bukkit.getOnlinePlayers()) {
					if (players.hasPermission("vc.sc")) {
						players.sendMessage(staffchat.replaceFirst(",", ""));
						event.setCancelled(true);
					}
				}
			}
		}

		if (!player.hasPermission("vc.chat.color")) {
			String message = !player.getWorld().getName().equals("clan")
					? (prefix + name + ChatColor.DARK_GRAY + " → " + ChatColor.WHITE + text)
					: clansChatHook(player, prefix, name, text);
			event.setFormat(message);
		}

		else {
			String message = !player.getWorld().getName().equals("clan")
					? (prefix + name + ChatColor.DARK_GRAY + " → " + ChatColor.WHITE
							+ ChatColor.translateAlternateColorCodes('&', text))
					: clansChatHook(player, prefix, name, ChatColor.translateAlternateColorCodes('&', text));
			event.setFormat(message);
		}
	}
}