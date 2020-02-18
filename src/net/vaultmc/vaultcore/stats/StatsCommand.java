package net.vaultmc.vaultcore.stats;

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
import java.util.Collections;

@RootCommand(literal = "stats", description = "Check statistics of yourself or other players.")
@Permission(Permissions.StatsCommand)
public class StatsCommand extends CommandExecutor {
    public StatsCommand() {
        register("statsSelf", Collections.emptyList());
        register("statsOthers",
                Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
    }

    @SubCommand("statsSelf")
    @PlayerOnly
    public void statsSelf(VLPlayer player) {
        viewStats(player, player);
    }

    @SubCommand("statsOthers")
    @Permission(Permissions.StatsCommandOther)
    public void statsOthers(VLCommandSender sender, VLOfflinePlayer target) {
        viewStats(sender, target);
    }

    @SneakyThrows
    private void viewStats(VLCommandSender sender, VLOfflinePlayer target) {
        DBConnection database = VaultCore.getDatabase();

        if (target.getFirstPlayed() == 0L) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
            return;
        }
        ResultSet stats = database.executeQueryStatement(
                "SELECT COUNT(session_id) AS sessions, AVG(duration) AS duration FROM sessions WHERE uuid=?",
                target.getUniqueId().toString());
        if (!stats.next()) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.stats.error"));
            return;
        }
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.stats.header"));
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.stats.player_joined"),
                target.getFormattedName(), stats.getString("sessions")));
        sender.sendMessage(
                Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.stats.player_session_length"),
                        target.getFormattedName(), Utilities.millisToTime(stats.getInt("duration"), true)));
    }
}