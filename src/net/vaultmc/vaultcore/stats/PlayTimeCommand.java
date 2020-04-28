package net.vaultmc.vaultcore.stats;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.misc.runnables.RankPromotions;
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
        register("playTimeTop", Collections.singletonList(Arguments.createLiteral("top")));
    }

    @SubCommand("playTimeSelf")
    @PlayerOnly
    public void checkPlayTimeSelf(VLPlayer sender) {
        printPlayTimeOnline(sender, sender);
    }

    @SubCommand("playTimeOthers")
    @Permission(Permissions.PlayTimeOther)
    public void checkPlayTimeOthers(VLCommandSender sender, VLOfflinePlayer target) {
        if (target.isOnline()) printPlayTimeOnline(target.getOnlinePlayer(), sender);
        else printPlayTimeOffline(sender, target);
    }

    @SubCommand("playTimeTop")
    @SneakyThrows
    public void playTimeTop(VLCommandSender sender) {
        int position = 1;
        DBConnection database = VaultCore.getDatabase();
        ResultSet rs = database.executeQueryStatement("SELECT username, playtime FROM players ORDER BY playtime DESC LIMIT 10");
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.playtime.top_header"));
        while (rs.next() && position <= 10) {
            VLOfflinePlayer player = VLOfflinePlayer.getOfflinePlayer(rs.getString("username"));
            long playtime;
            if (player.isOnline())
                playtime = (long) (VLPlayer.getPlayer(rs.getString("username")).getStatistic(Statistic.PLAY_ONE_MINUTE) * 0.05 * 1000);
            else playtime = (long) (rs.getLong("playtime") * 0.05 * 1000);
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.playtime.top"), position, player.getFormattedName(), Utilities.millisToTime(playtime, false, true)));
            position++;
        }
    }

    private void printPlayTimeOnline(VLPlayer player, VLCommandSender sender) {
        long playtime = (long) (player.getStatistic(Statistic.PLAY_ONE_MINUTE) * 0.05 * 1000);
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.playtime.online_player"),
                player.getFormattedName(), Utilities.millisToTime(playtime, false, true)));
        if (player == sender) {
            if (player.getGroup().equalsIgnoreCase("default")) {
                player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.playtime.rank_wait"), Utilities.millisToTime((playtime - RankPromotions.MEMBER_TIME), false, false), "Member"));
            }
            if (player.getGroup().equalsIgnoreCase("member")) {
                player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.playtime.rank_wait"), Utilities.millisToTime((playtime - RankPromotions.PATREON_TIME), false, false), "Patreon"));
            }
        }
    }

    @SneakyThrows
    private void printPlayTimeOffline(VLCommandSender player, VLOfflinePlayer target) {
        DBConnection database = VaultCore.getDatabase();

        if (target.getFirstPlayed() == 0L) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
            return;
        }

        ResultSet rs = database.executeQueryStatement("SELECT username, playtime FROM players WHERE username=?",
                target.getName());
        if (rs.next()) {
            long playtime = rs.getLong("playtime");
            long t = (long) (playtime * 0.05 * 1000);
            player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.playtime.offline_player"),
                    target.getFormattedName(), Utilities.millisToTime(t, false, true)));
        } else {
            player.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
        }
    }
}