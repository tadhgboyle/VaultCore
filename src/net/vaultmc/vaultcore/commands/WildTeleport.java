package net.vaultmc.vaultcore.commands;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.VaultCore;

public class WildTeleport implements CommandExecutor {

	String string = VaultCore.getInstance().getConfig().getString("string");
	String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");
	String variable2 = VaultCore.getInstance().getConfig().getString("variable-2");
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("wild")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}
			Player player = (Player) sender;
			if (!player.hasPermission("vc.wildteleport")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			if (player.getWorld().getName().equalsIgnoreCase("Survival")
					|| player.getWorld().getName().equalsIgnoreCase("clans")) {

				Location originalLocation = player.getLocation();
				Random random = new Random();
				Location teleportlocation = null;
				int x = random.nextInt(10000) + 100;
				int y = 150;
				int z = random.nextInt(10000) + 100;
				boolean isOnland = false;
				
				while (isOnland == false) {

					teleportlocation = new Location(player.getWorld(), x, y, z);

					if (teleportlocation.getBlock().getType() != Material.AIR) {
						isOnland = true;
					} else
						y--;
				}

				player.teleport(new Location(player.getWorld(), teleportlocation.getX(), teleportlocation.getY() + 1,
						teleportlocation.getZ()));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have been teleported "
						+ variable2 + (int) teleportlocation.distance(originalLocation) + string + " blocks away!"));
				return true;
			}
			else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You must be in the " + variable1
						+ "Survival" + string + " or " + variable1 + "Clans" + string + " world to run this command."));
			}
			return true;
		}
		return true;
	}
}