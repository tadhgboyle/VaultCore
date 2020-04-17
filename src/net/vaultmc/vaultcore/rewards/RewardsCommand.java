package net.vaultmc.vaultcore.rewards;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Statistic;

import java.util.Collections;

@RootCommand(literal = "rewards", description = "View and redeem loyalty rewards.")
@Permission(Permissions.RewardsCommand)
@PlayerOnly
public class RewardsCommand extends CommandExecutor {

    public RewardsCommand() {
        register("rewardsMain", Collections.emptyList());
    }

    @SubCommand("rewardsMain")
    public void rewardsMain(VLPlayer sender) {
        int streak = sender.getPlayerData().getInt("streak");
        int playtime = sender.getStatistic(Statistic.PLAY_ONE_MINUTE);
        // TODO: Things with this to engage players
    }

    /**
     * Used to check if a player has joined in the last days
     * @param target The online or offline player to check.
     * @param days The number of days to see if they have been online or not since.
     * @return boolean - Whether they have or not
     */
    public static boolean checkLastSeen(VLOfflinePlayer target, int days) {
        // Convert days to milliseconds
        int millis = days * 86400000;
        // See if their lastSeen was within the milliseconds
        return (target.getLastLogin() + millis > System.currentTimeMillis() - millis);
    }
}
