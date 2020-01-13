package net.vaultmc.vaultcore.commands.staff.gamemode;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.commands.*;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(
        literal = "gmc",
        description = "Change your gamemode to creative."
)
@Permission(Permissions.GamemodeCommand)
@PlayerOnly
@Aliases({"creative", "gmcreative"})
public class GMCreativeCommand extends CommandExecutor {
    private String string = Utilities.string;
    private String variable1 = Utilities.variable1;

    public GMCreativeCommand() {
        register("execute", Collections.emptyList());
    }

    @SubCommand("execute")
    public void execute(CommandSender sender) {
        ((Player) sender).setGameMode(GameMode.CREATIVE);
        sender.sendMessage(string + "Your game mode is now " + variable1 + "Creative" + string + ".");
    }
}
