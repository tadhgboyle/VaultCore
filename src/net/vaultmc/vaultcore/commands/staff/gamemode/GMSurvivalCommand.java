package net.vaultmc.vaultcore.commands.staff.gamemode;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(
        literal = "gms",
        description = "Change your gamemode to survival."
)
@Permission(Permissions.GamemodeCommand)
@PlayerOnly
@Aliases({"survival", "gmsurvival"})
public class GMSurvivalCommand extends CommandExecutor {
    public GMSurvivalCommand() {
        register("execute", Collections.emptyList(), "vaultcore");
    }

    @SubCommand("execute")
    public void execute(CommandSender sender) {
        ((Player) sender).setGameMode(GameMode.SURVIVAL);
        sender.sendMessage(ChatColor.YELLOW + "Your game mode is now " + ChatColor.GOLD + "survival" + ChatColor.YELLOW + ".");
    }
}