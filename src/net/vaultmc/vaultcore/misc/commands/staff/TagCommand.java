package net.vaultmc.vaultcore.misc.commands.staff;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.sql.ResultSet;
import java.util.Arrays;

@RootCommand(literal = "tag", description = "See some notes about a player.")
@Permission(Permissions.TagCommand)
@PlayerOnly
public class TagCommand extends CommandExecutor {
    public TagCommand() {
        unregisterExisting();
        register("tagAdd",
                Arrays.asList(Arguments.createLiteral("add"),
                        Arguments.createArgument("target", Arguments.offlinePlayerArgument()),
                        Arguments.createArgument("content", Arguments.greedyString())));
        register("tagList", Arrays.asList(Arguments.createLiteral("list"),
                Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
        register("tagDelete", Arrays.asList(Arguments.createLiteral("delete"),
                Arguments.createArgument("id", Arguments.integerArgument(1))));
    }

    @SneakyThrows
    @SubCommand("tagAdd")
    public void tagAdd(VLPlayer executor, VLOfflinePlayer target, String content) {
        if (target == executor) {
            executor.sendMessage(VaultLoader.getMessage("vaultcore.commands.tag.self_error"));
            return;
        }

        if (!target.isOnline()) {
            if (target.getFirstPlayed() == 0L) {
                executor.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
                return;
            }
        }
        tagAddQuery(executor, target, content);
    }

    @SneakyThrows
    private void tagAddQuery(VLPlayer author, VLOfflinePlayer target, String content) {
        DBConnection database = VaultCore.getDatabase();
        database.executeUpdateStatement("INSERT INTO tags (player, author, content, timestamp) VALUES (?, ?, ?, ?)",
                target.getUniqueId().toString(), author.getUniqueId().toString(), content, System.currentTimeMillis());
        ResultSet id = database.executeQueryStatement("SELECT MAX(id) AS latest_id FROM tags");
        String tagId = null;
        if (id.next()) {
            tagId = id.getString("latest_id");
        }
        author.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tag.add"), tagId,
                target.getFormattedName()));
    }

    @SneakyThrows
    @SubCommand("tagList")
    public void tagList(VLCommandSender sender, VLOfflinePlayer target) {
        DBConnection database = VaultCore.getDatabase();
        ResultSet tags = database.executeQueryStatement(
                "SELECT id, player, content, timestamp FROM tags WHERE (player = ? AND status = '0')",
                target.getUniqueId().toString());
        boolean empty = true;
        boolean first = true;
        while (tags.next()) {
            empty = false;
            if (first) {
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.tag.list_header"));
            }
            sender.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tag.list"), tags.getString("id"),
                            tags.getString("content"), Utilities.millisToDate(tags.getLong("timestamp"))));
            first = false;
        }
        if (empty) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.tag.no_tags_for_player"));
        }
    }

    @SneakyThrows
    @SubCommand("tagDelete")
    @Permission(Permissions.TagCommandDelete)
    public void tagDelete(VLCommandSender sender, int id) {
        DBConnection database = VaultCore.getDatabase();
        ResultSet tags = database.executeQueryStatement("SELECT id, player, status FROM tags WHERE id = ?", id);
        if (tags.next()) {
            if (tags.getInt("status") == 1) {
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.tag.tag_already_deleted"));
            } else {
                int delete = database.executeUpdateStatement("UPDATE tags SET status = '1' WHERE `tags`.`id` = ?", id);
                if (delete > 0) {
                    sender.sendMessage(
                            Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tag.delete"), id + ""));
                }
            }
        } else {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.tag.tag_not_exist"));
        }
    }
}
