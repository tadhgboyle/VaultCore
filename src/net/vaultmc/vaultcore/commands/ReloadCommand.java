package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;

@RootCommand(
        literal = "vcreload",
        description = "Reloads VaultCore's configuration and data"
)
@Permission(Permissions.ReloadCommand)
public class ReloadCommand extends CommandExecutor {
    public ReloadCommand() {
        register("reload", Collections.emptyList());
    }

    @SubCommand("reload")
    public void reload(CommandSender sender) {
        VaultCore.getInstance().reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Configuration reloaded!");
    }
}
