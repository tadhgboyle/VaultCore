/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

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
