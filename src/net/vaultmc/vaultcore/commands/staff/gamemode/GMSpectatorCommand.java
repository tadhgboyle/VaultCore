package net.vaultmc.vaultcore.commands.staff.gamemode;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.commands.*;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(
        literal = "gmsp",
        description = "Change your gamemode to spectator."
)
@Permission(Permissions.GamemodeCommand)
@PlayerOnly
@Aliases({"spec", "gmspec", "spectator", "gmspectator"})
public class GMSpectatorCommand extends CommandExecutor {
    private String string = Utilities.string;
    private String variable1 = Utilities.variable1;

    public GMSpectatorCommand() {
        register("execute", Collections.emptyList());
    }

    @SubCommand("execute")
    public void execute(CommandSender sender) {
        ((Player) sender).setGameMode(GameMode.SPECTATOR);
        sender.sendMessage(string + "Your game mode is now " + variable1 + "Spectator" + string + ".");
    }
}
