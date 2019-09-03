package me.aberdeener.vaultcore.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.aberdeener.vaultcore.VaultCore;

public class PlayerTPListener implements CommandExecutor, Listener {

	private static HashMap<UUID, Location> teleports = new HashMap<>();

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		teleports.put(event.getPlayer().getUniqueId(), event.getFrom());

		// add survival location to data.yml
		if (event.getFrom().getWorld().getName().equals("Survival")
				|| (event.getFrom().getWorld().getName().equals("Survival_T")
						|| (event.getFrom().getWorld().getName().equals("Creative")))) {

			Location from = event.getFrom();

			if (event.getFrom().getWorld().getName().equals("Survival")) {
				VaultCore.getInstance().getPlayerData().set("players." + event.getPlayer().getUniqueId() + ".sv", from);
				VaultCore.getInstance().savePlayerData();
			}

			else if (event.getFrom().getWorld().getName().equals("Survival_T")) {
				VaultCore.getInstance().getPlayerData().set("players." + event.getPlayer().getUniqueId() + ".tsv", from);
				VaultCore.getInstance().savePlayerData();
			}

			else if (event.getFrom().getWorld().getName().equals("Creative")) {
				VaultCore.getInstance().getPlayerData().set("players." + event.getPlayer().getUniqueId() + ".cr", from);
				VaultCore.getInstance().savePlayerData();
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("back")) {

			// console sender check
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}

			Player player = (Player) sender;

			if (!sender.hasPermission("vc.back")) {

				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}

			else if (teleports.containsKey(player.getUniqueId())) {

				String string = ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("string"));

				Location before = teleports.get(player.getUniqueId());

				player.teleport(before);
				player.sendMessage(string + "You have been teleported to your previous location.");
				teleports.remove(player.getUniqueId());
				return true;

			}

			else {
				player.sendMessage(ChatColor.RED + "You have nowhere to teleport to!");
				return true;
			}
		}
		return true;

	}

}
