package me.aberdeener.vaultcore.tabcompletion;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TabCompletion implements TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		
		// if we are on the first arg
		if (command.getName().equalsIgnoreCase("help") && args.length == 1) {
			if (sender instanceof Player) {
				return Arrays.asList("player", "staff");
			}
		}
		
		if (command.getName().equalsIgnoreCase("gamemode") && args.length == 1) {
			if (sender instanceof Player) {
				return Arrays.asList("creative", "survival", "spectator");
			}
		}

		return null;
	}
}