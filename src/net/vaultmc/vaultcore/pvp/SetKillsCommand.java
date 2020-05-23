package net.vaultmc.vaultcore.pvp;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Arrays;

@RootCommand(
        literal = "setkills",
        description = "Useful useful"
)
@Permission(Permissions.PvPAdmin)
public class SetKillsCommand extends CommandExecutor {
    public SetKillsCommand() {
        register("setKills", Arrays.asList(
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("amount", Arguments.doubleArgument())
        ), "vaultcore");

    }

    @SubCommand("setKills")
    public void setKills(VLCommandSender sender, VLPlayer target, double amount) {
        double kills = target.getDataConfig().getInt("stats.kills");
        target.getDataConfig().set("stats.kills", amount);
        target.saveData();
    }
}
