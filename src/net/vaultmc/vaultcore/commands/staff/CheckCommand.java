package net.vaultmc.vaultcore.commands.staff;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;

import java.sql.ResultSet;
import java.util.Collections;

@RootCommand(literal = "check", description = "Get info about a player.")
@Permission(Permissions.CheckCommand)
public class CheckCommand extends CommandExecutor {
    private String string = Utilities.string;
    private String variable1 = Utilities.variable1;

    public CheckCommand() {
        register("check",
                Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
    }

    @SneakyThrows
    @SubCommand("check")
    public void check(VLCommandSender sender, VLOfflinePlayer target) {
        DBConnection database = VaultCore.getDatabase();
        ResultSet rs = database.executeQueryStatement(
                "SELECT uuid, username, firstseen, lastseen, rank, ip FROM players WHERE username=?", target.getName());
        if (!rs.next()) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
            return;
        }
        String uuid = rs.getString("uuid");
        String username = target.getFormattedName();
        long firstseen = rs.getLong("firstseen");
        long lastseen = rs.getLong("lastseen");
        String rank = WordUtils.capitalize(rs.getString("rank"));
        String ip = rs.getString("ip");

        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.check.header"));

        if (target.isOnline()) {
            sender.sendMessage(string + "Checking: " + username);
        } else {
            sender.sendMessage(
                    string + "Checking: " + username + ChatColor.GRAY + " " + ChatColor.ITALIC + "[OFFLINE]");
        }
        // TODO VaultLoader#getMessage()
        sender.sendMessage(string + "UUID: " + variable1 + uuid);
        sender.sendMessage(string + "First Seen (D/M/Y): " + variable1 + Utilities.millisToDate(firstseen));
        sender.sendMessage(string + "Last Seen (D/M/Y): " + variable1 + Utilities.millisToDate(lastseen));
        sender.sendMessage(string + "Last IP: " + variable1 + ip);
        sender.sendMessage(string + "Rank: " + variable1 + rank);
        sender.sendMessage(
                string + "Database: " + variable1 + "https://database.vaultmc.net/?user=" + target.getName());
        sender.performCommand("tag list " + target.getName());

    }
}