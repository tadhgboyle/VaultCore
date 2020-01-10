package net.vaultmc.vaultcore.commands.staff;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;

@RootCommand(literal = "tag", description = "See some notes about a player.")
@Permission(Permissions.TagCommand)
public class TagCommand extends CommandExecutor {

	DBConnection database = VaultCore.getDatabase();

	String string = Utilities.string;
	String variable1 = Utilities.variable1;
	String variable2 = Utilities.variable2;

	public TagCommand() {
		unregisterExisting();
		this.register("tagAdd", Arrays.asList(Arguments.createArgument("target", Arguments.offlinePlayerArgument()),
				Arguments.createArgument("content", Arguments.greedyString())));
		register("tagCheck",
				Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
		register("tagDelete", Collections.singletonList(Arguments.createArgument("id", Arguments.integerArgument(1))));
	}

	@SubCommand("tagAdd")
	public void tagAdd(CommandSender sender, OfflinePlayer target, String... content) throws SQLException {
		if (target == sender) {
			sender.sendMessage(ChatColor.RED + "You can't tag yourself!");
		}

		String author = null;
		if (sender instanceof ConsoleCommandSender) {
			author = "CONSOLE";
		} else {
			Player executor = (Player) sender;
			author = executor.getUniqueId().toString();
		}

		database.executeUpdateStatement("INSERT INTO tags (player, author, content) VALUES (?, ?, ?)",
				target.getUniqueId(), author, content);
		ResultSet id = database.executeQueryStatement("SELECT MAX(id) AS latest_id FROM tags");
		String tagId = null;
		if (id.next()) {
			tagId = id.getString("latest_id");
		}
		sender.sendMessage(string + "Successfully added a tag [# " + variable2 + tagId + string + "] to " + variable1
				+ target.getName() + string + ".");
	}

	@SubCommand("tagCheck")
	public void tagCheck(CommandSender sender, OfflinePlayer target) throws SQLException {
		ResultSet tags = database.executeQueryStatement("SELECT id, author, content, status FROM tags WHERE player = ?",
				target.getUniqueId());
		if (tags.next()) {
			sender.sendMessage(ChatColor.DARK_GREEN + "--== [Tags] ==--");
			while (tags.next()) {
				String author = Bukkit.getOfflinePlayer(UUID.fromString(tags.getString("author"))).getName();
				sender.sendMessage(string + "[# " + variable2 + tags.getString("id") + string + "] "
						+ tags.getString("content") + ", " + variable1 + author + string + ".");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "There are no tags for that player!");
		}
	}

	@SubCommand("tagDelete")
	@Permission(Permissions.TagCommandDelete)
	public void tagDelete(CommandSender sender, String id) {
		int delete = database.executeUpdateStatement("UPDATE tags SET status = '1' WHERE id = ?", id);
		if (delete > 0) {
			sender.sendMessage(string + "The tag [#" + variable1 + id + string + "] has been deleted.");
		} else {
			sender.sendMessage(ChatColor.RED
					+ "A fatal error has occured while deleting this tag. Please contact an administrator.");
		}
	}
}
