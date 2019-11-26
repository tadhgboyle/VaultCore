package net.vaultmc.vaultcore.commands.staff.gamemode;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
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
    public GMCreativeCommand() {
        register("execute", Collections.emptyList(), "vaultcore");
    }

    @SubCommand("execute")
    public void execute(CommandSender sender) {
        ((Player) sender).setGameMode(GameMode.CREATIVE);
        sender.sendMessage(ChatColor.YELLOW + "Your game mode is now " + ChatColor.GOLD + "creative" + ChatColor.YELLOW + ".");
    }
}
