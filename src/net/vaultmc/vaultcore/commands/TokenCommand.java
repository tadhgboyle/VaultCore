package net.vaultmc.vaultcore.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultutils.VaultUtils;
import net.vaultmc.vaultutils.utils.commands.experimental.CommandExecutor;
import net.vaultmc.vaultutils.utils.commands.experimental.Permission;
import net.vaultmc.vaultutils.utils.commands.experimental.PlayerOnly;
import net.vaultmc.vaultutils.utils.commands.experimental.RootCommand;
import net.vaultmc.vaultutils.utils.commands.experimental.SubCommand;

@RootCommand(literal = "token", description = "Get your universal token for VaultMC services.")
@Permission(Permissions.TokenCommand)
@PlayerOnly
public class TokenCommand extends CommandExecutor {
	private static String string = Utilities.string;
	private static String variable1 = Utilities.variable1;
	private static String variable2 = Utilities.variable2;

	public TokenCommand() {
		register("getToken", Collections.emptyList());
	}

	static String getToken(UUID uuid, Player player) throws SQLException {

		ResultSet getToken = VaultUtils.getDatabase().executeQueryStatement("SELECT token FROM players WHERE uuid=?",
				uuid);
		if (getToken.next()) {
			String token = getToken.getString("token");
			if (token != null) {
				return token;
			}
		}
		player.sendMessage(string + "Generating your token...");

		String new_token = RandomStringUtils.random(8, true, false);

		ResultSet duplicateCheck = VaultUtils.getDatabase()
				.executeQueryStatement("SELECT username FROM players WHERE token=?", new_token);

		if (!duplicateCheck.next()) {
			VaultUtils.getDatabase().executeUpdateStatement("UPDATE players SET token=? WHERE uuid=?", new_token, uuid);
		} else {
			player.sendMessage(string + "You are one in " + variable1 + "308915776" + string
					+ "! The token we generated was already in our database.");
			player.sendMessage(string + "Please re-run this command.");
			// line 73 checks for this null
			return null;
		}
		return new_token;
	}

	@SubCommand("getToken")
	public void getToken(CommandSender sender) throws SQLException {
		String token = getToken(((Player) sender).getUniqueId(), (Player) sender);
		// if they are 1/308915776 make them run cmd again
		if (token == null) {
			return;
		}
		sender.sendMessage(string + "Your token: " + variable2 + token);
	}
}