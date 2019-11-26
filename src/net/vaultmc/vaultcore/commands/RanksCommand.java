package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultutils.utils.commands.experimental.CommandExecutor;
import net.vaultmc.vaultutils.utils.commands.experimental.Permission;
import net.vaultmc.vaultutils.utils.commands.experimental.RootCommand;
import net.vaultmc.vaultutils.utils.commands.experimental.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(
        literal = "ranks",
        description = "Learn about the ranks on our server."
)
@Permission(Permissions.RanksCommand)
public class RanksCommand extends CommandExecutor {
    public RanksCommand() {
        register("checkRanks", Collections.emptyList(), "vaultcore");
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