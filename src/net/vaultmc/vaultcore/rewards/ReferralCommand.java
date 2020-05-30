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
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;

import java.util.Collections;

@RootCommand(literal = "referral", description = "Use or display a referral code.")
@Permission(Permissions.RewardsCommand)
@PlayerOnly
public class ReferralCommand extends CommandExecutor {

    public ReferralCommand() {
        register("referralMain", Collections.emptyList());
        register("referralUse", Collections.singletonList(Arguments.createArgument("code", Arguments.word())));
    }

    @SubCommand("referralMain")
    public void referralMain(VLPlayer sender) {

    }

    @SubCommand("referralUse")
    public void referralUse(VLPlayer sender, String code) {
        if (!hasUsedRefferal(sender)) {
            VLOfflinePlayer target = VLPlayer.getPlayer(code);
            if (target.getFirstPlayed() == 0L) {
                return;
            }
            sender.deposit(Bukkit.getWorld("Lobby"), 100);
            target.deposit(Bukkit.getWorld("Lobby"), 100);
            sender.getPlayerData().set("referral_used", true);
        }
    }

    public boolean hasUsedRefferal(VLPlayer target) {
        return target.getPlayerData().getBoolean("referral_used");
    }
}
