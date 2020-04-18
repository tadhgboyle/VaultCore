package net.vaultmc.vaultcore.rewards;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

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
        /*
        TODO: Things with this to engage players
        Ideas: referral rewards
               playtime rewards
               streak rewards
         */
    }
}
