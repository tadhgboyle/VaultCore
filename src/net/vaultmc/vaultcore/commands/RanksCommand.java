package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;

@RootCommand(
        literal = "ranks",
        description = "Learn about the ranks on our server."
)
@Permission(Permissions.RanksCommand)
public class RanksCommand extends CommandExecutor {
    public RanksCommand() {
        register("checkRanks", Collections.emptyList());
    }

    @SubCommand("checkRanks")
    public void checkRanks(CommandSender sender) {
        sender.sendMessage(ChatColor.DARK_GREEN + "--== [Player Ranks] ==--");
        sender.sendMessage(ChatColor.DARK_GRAY + "Default");
        sender.sendMessage(ChatColor.GRAY + "Member");
        sender.sendMessage(ChatColor.WHITE + "Patreon");
        sender.sendMessage(ChatColor.AQUA + "Trusted");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_GREEN + "--== [Staff Ranks] ==--");
        sender.sendMessage(ChatColor.DARK_AQUA + "Moderator");
        sender.sendMessage(ChatColor.BLUE + "Administrator");
    }
}