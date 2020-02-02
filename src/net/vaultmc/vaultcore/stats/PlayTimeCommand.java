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
import org.bukkit.Statistic;

import java.sql.ResultSet;
import java.util.Collections;

@RootCommand(literal = "playtime", description = "Check yourself or other's play time.")
@Permission(Permissions.PlayTime)
@Aliases("pt")
public class PlayTimeCommand extends CommandExecutor {
    public PlayTimeCommand() {
        register("playTimeSelf", Collections.emptyList());
        register("playTimeOthers",
                Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
    }

    @SubCommand("playTimeSelf")
    @PlayerOnly
    public void checkPlayTimeSelf(VLPlayer sender) {
        printPlayTimeOnline(sender, sender);
    }

    @SubCommand("playTimeOthers")
    @Permission(Permissions.PlayTimeOther)
    public void checkPlayTimeOthers(VLCommandSender sender, VLOfflinePlayer target) {
        if (target.isOnline()) {
            printPlayTimeOnline(target.getOnlinePlayer(), sender);
            return;
        }
        printPlayTimeOffline(sender, target);
    }

    private void printPlayTimeOnline(VLPlayer player, VLCommandSender sender) {
        long t = (long) (player.getStatistic(Statistic.PLAY_ONE_MINUTE) * 0.05 * 1000);
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.playtime.online_player"),
                player.getFormattedName(), Utilities.millisToTime(t)));
    }

    @SneakyThrows
    private void printPlayTimeOffline(VLCommandSender player, VLOfflinePlayer target) {
        DBConnection database = VaultCore.getDatabase();

        ResultSet rs = database.executeQueryStatement("SELECT username, playtime FROM players WHERE username=?",
                target.getName());
        if (!rs.next()) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
            return;
        }

        long playtime = rs.getLong("playtime");
        long t = (long) (playtime * 0.05 * 1000);
        player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.playtime.offline_player"),
                target.getFormattedName(), Utilities.millisToTime(t)));
    }
}