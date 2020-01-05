package net.vaultmc.vaultcore.commands.staff.gamemode;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.*;
import org.bukkit.ChatColor;
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
    public GMSpectatorCommand() {
        register("execute", Collections.emptyList());
    }

    @SubCommand("execute")
    public void execute(CommandSender sender) {
        ((Player) sender).setGameMode(GameMode.SPECTATOR);
        sender.sendMessage(ChatColor.YELLOW + "Your game mode is now " + ChatColor.GOLD + "Spectator" + ChatColor.YELLOW + ".");
    }
}
