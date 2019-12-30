package net.vaultmc.vaultcore.commands.staff.gamemode;

import java.util.Collections;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultutils.utils.commands.experimental.Aliases;
import net.vaultmc.vaultutils.utils.commands.experimental.CommandExecutor;
import net.vaultmc.vaultutils.utils.commands.experimental.Permission;
import net.vaultmc.vaultutils.utils.commands.experimental.PlayerOnly;
import net.vaultmc.vaultutils.utils.commands.experimental.RootCommand;
import net.vaultmc.vaultutils.utils.commands.experimental.SubCommand;

@RootCommand(
        literal = "gms",
        description = "Change your gamemode to survival."
)
@Permission(Permissions.GamemodeCommand)
@PlayerOnly
@Aliases({"survival", "gmsurvival"})
public class GMSurvivalCommand extends CommandExecutor {
	
	private String string = Utilities.string;
	private String variable1 = Utilities.variable1;
	
    public GMSurvivalCommand() {
        register("execute", Collections.emptyList());
    }

    @SubCommand("execute")
    public void execute(CommandSender sender) {
        ((Player) sender).setGameMode(GameMode.SURVIVAL);
        sender.sendMessage(string + "Your game mode is now " + variable1 + "Survival" + string + ".");
    }
}