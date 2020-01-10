package net.vaultmc.vaultcore.commands.staff;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;

@RootCommand(literal = "tag", description = "See some notes about a player.")
@Permission(Permissions.TagCommand)
@PlayerOnly
public class TagCommand extends CommandExecutor {

	DBConnection database = VaultCore.getDatabase();

	String string = Utilities.string;
	String variable1 = Utilities.variable1;
	String variable2 = Utilities.variable2;

	public TagCommand() {
		unregisterExisting();
		this.register("tagAdd",
				Arrays.asList(Arguments.createLiteral("add"),
						Arguments.createArgument("target", Arguments.offlinePlayerArgument()),
						Arguments.createArgument("content", Arguments.greedyString())));
		register("tagCheck", Arrays.asList(Arguments.createLiteral("check"),
				Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
		register("tagDelete", Arrays.asList(Arguments.createLiteral("delete"),
				Arguments.createArgument("id", Arguments.integerArgument(1))));
	}

	@SubCommand("tagAdd")
	public void tagAdd(CommandSender sender, OfflinePlayer target, String content) throws SQLException {
		Player executor = (Player) sender;
		if (target == executor) {
			sender.sendMessage(ChatColor.RED + "You can't tag yourself!");
			return;
		}

		if (!target.isOnline()) {
			try {
				ResultSet rs = database.executeQueryStatement("SELECT username FROM players WHERE username=?",
						target.getName());
				if (!rs.next()) {
					sender.sendMessage(ChatColor.RED + "That player has never joined before!");
					return;
				} else {
					addTagQuery(sender, target, content);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			addTagQuery(sender, target, content);
		}
	}

	private void addTagQuery(CommandSender sender, OfflinePlayer target, String content) throws SQLException {
		Player author = (Player) sender;
		database.executeUpdateStatement("INSERT INTO tags (player, author, content) VALUES (?, ?, ?)",
				target.getUniqueId().toString(), author.getUniqueId().toString(), content);
		ResultSet id = database.executeQueryStatement("SELECT MAX(id) AS latest_id FROM tags");
		String tagId = null;
		if (id.next()) {
			tagId = id.getString("latest_id");
		}
		sender.sendMessage(string + "Successfully added a tag [#" + variable2 + tagId + string + "] to "
				+ VaultCoreAPI.getName(target) + string + ".");

	}

	@SubCommand("tagCheck")
	public void tagCheck(CommandSender sender, OfflinePlayer target) throws SQLException {
		ResultSet tags = database.executeQueryStatement(
				"SELECT id, player, content FROM tags WHERE (player = ? AND status = '0')",
				target.getUniqueId().toString());
		boolean empty = true;
		boolean first = true;
		while (tags.next()) {
			empty = false;
			if (first) {
				sender.sendMessage(ChatColor.DARK_GREEN + "--== [Tags] ==--");
			}
			sender.sendMessage(string + "[#" + variable2 + tags.getString("id") + string + "] " + variable1
					+ tags.getString("content"));
			first = false;

		}
		if (empty) {
			sender.sendMessage(ChatColor.RED + "There are no tags for that player.");
		}
	}

	@SubCommand("tagDelete")
	@Permission(Permissions.TagCommandDelete)
	public void tagDelete(CommandSender sender, int id) throws SQLException {

		ResultSet tags = database.executeQueryStatement("SELECT id, status FROM tags WHERE id = ?", id);
		if (tags.next()) {
			if (tags.getInt("status") == 1) {
				sender.sendMessage(ChatColor.RED + "That tag has already been deleted.");
			} else {
				int delete = database.executeUpdateStatement("UPDATE tags SET status = '1' WHERE `tags`.`id` = ?", id);
				if (delete > 0) {
					sender.sendMessage(string + "The tag [#" + variable2 + id + string + "] has been deleted.");
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "That tag does not exist.");
		}
	}
}
