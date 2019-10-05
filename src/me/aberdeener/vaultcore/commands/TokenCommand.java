package me.aberdeener.vaultcore.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.aberdeener.vaultcore.VaultCore;

public class TokenCommand implements CommandExecutor {

	static String string = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("string"));
	String variable1 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-1"));
	String variable2 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-2"));

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("token")) {

			// console sender check
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
						VaultCore.getInstance().getConfig().getString("console-error")));
				return true;
			}

			Player player = (Player) sender;

			try {
				player.sendMessage(string + "Your token: " + variable2 + getToken(player.getUniqueId(), player));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	static String getToken(UUID uuid, Player player) throws SQLException {

		java.sql.Statement stmt = VaultCore.getInstance().connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT token FROM players WHERE uuid='" + uuid + "'");

		if (rs.next()) {
			String token = rs.getString("token");
			if (token != null) {
				return token;
			}
		}

		player.sendMessage(string + "Generating your token...");
		int leftLimit = 97;
		int rightLimit = 122;
		int targetStringLength = 8;
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		String new_token = buffer.toString();
		VaultCore.getInstance().connection.createStatement()
				.executeUpdate("UPDATE players SET token='" + new_token + "' WHERE uuid='" + uuid + "'");
		return new_token;
	}

}
