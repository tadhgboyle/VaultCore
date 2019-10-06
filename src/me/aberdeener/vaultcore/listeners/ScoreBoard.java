package me.aberdeener.vaultcore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.aberdeener.vaultcore.VaultCore;

public class ScoreBoard {

	static Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	static Team admin = scoreboard.registerNewTeam("00_admin");
	static Team moderator = scoreboard.registerNewTeam("01_moderator");
	static Team trusted = scoreboard.registerNewTeam("02_trusted");
	static Team patreon = scoreboard.registerNewTeam("03_patreon");
	static Team member = scoreboard.registerNewTeam("04_member");
	static Team normal = scoreboard.registerNewTeam("05_default");

	public static void scoreboard(Player player) {
		
		if (VaultCore.getChat().getPrimaryGroup(player).equals("admin")) {
			admin.addEntry(player.getName());
			admin.setColor(ChatColor.BLUE);
			player.setPlayerListName(player.getDisplayName());
		}
		else if (VaultCore.getChat().getPrimaryGroup(player).equals("moderator")) {
			moderator.addEntry(player.getName());
			moderator.setColor(ChatColor.DARK_AQUA);
			player.setPlayerListName(player.getDisplayName());
		}
		else if (VaultCore.getChat().getPrimaryGroup(player).equals("trusted")) {
			trusted.addEntry(player.getName());
			trusted.setColor(ChatColor.AQUA);
			player.setPlayerListName(player.getDisplayName());
		}
		else if (VaultCore.getChat().getPrimaryGroup(player).equals("patreon")) {
			patreon.addEntry(player.getName());
			patreon.setColor(ChatColor.WHITE);
			player.setPlayerListName(player.getDisplayName());
		}
		else if (VaultCore.getChat().getPrimaryGroup(player).equals("member")) {
			member.addEntry(player.getName());
			member.setColor(ChatColor.GRAY);
			player.setPlayerListName(player.getDisplayName());
		}

		else if (VaultCore.getChat().getPrimaryGroup(player).equals("default")) {
			normal.addEntry(player.getName());
			normal.setColor(ChatColor.DARK_GRAY);
			player.setPlayerListName(player.getDisplayName());
		}
		player.setScoreboard(scoreboard);
	}
}