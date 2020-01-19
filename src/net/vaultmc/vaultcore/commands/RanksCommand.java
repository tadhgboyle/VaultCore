package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import org.bukkit.ChatColor;

import java.util.Collections;

@RootCommand(literal = "ranks", description = "Learn about the ranks on our server.")
@Permission(Permissions.RanksCommand)
public class RanksCommand extends CommandExecutor {
	public RanksCommand() {
		register("checkRanks", Collections.emptyList());
	}

    @SubCommand("checkRanks")
    public void checkRanks(VLCommandSender sender) {
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.ranks.player_header"));
        sender.sendMessage(ChatColor.DARK_GRAY + "Default");
        sender.sendMessage(ChatColor.GRAY + "Member");
        sender.sendMessage(ChatColor.WHITE + "Patreon");
        sender.sendMessage(ChatColor.AQUA + "Trusted");
        sender.sendMessage("");
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.ranks.staff_header"));
        sender.sendMessage(ChatColor.DARK_AQUA + "Moderator");
        sender.sendMessage(ChatColor.BLUE + "Administrator");
    }
}