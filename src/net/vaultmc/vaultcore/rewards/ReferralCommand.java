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
    public void refferalMain(VLPlayer sender) {

    }

    @SubCommand("referralUse")
    public void refferalUse(VLPlayer sender, String code) {
        if (!hasUsedRefferal(sender)) {
            VLOfflinePlayer target = VLPlayer.getPlayer(code);
            if (target.getFirstPlayed() == 0L) {

                return;
            }
            sender.deposit(Bukkit.getWorld("Lobby"), 100);
            target.deposit(Bukkit.getWorld("Lobby"), 100);
            sender.getPlayerData().set("refferal_used", true);
        } else {

        }
    }

    public boolean hasUsedRefferal(VLPlayer target) {
        return target.getPlayerData().getBoolean("refferal_used");
    }
}
