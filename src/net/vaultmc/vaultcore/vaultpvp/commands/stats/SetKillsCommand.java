package net.vaultmc.vaultcore.vaultpvp.commands.stats;

import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Arrays;

@RootCommand(
        literal = "setkills",
        description = "Useful useful"
)
@Permission("pvp.admin")
public class SetKillsCommand extends CommandExecutor {
    public SetKillsCommand() {
        this.register("setKills", Arrays.asList(
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("amount", Arguments.doubleArgument())
        ), "vaultpvp");

    }

    @SubCommand("setKills")
    public void setKills(VLCommandSender sender, VLPlayer target, double amount) {
        double kills = target.getDataConfig().getInt("stats.kills");
        target.getDataConfig().set("stats.kills", amount);
        target.saveData();
    }

}
