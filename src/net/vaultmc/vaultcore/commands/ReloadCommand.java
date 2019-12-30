package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultutils.utils.commands.experimental.CommandExecutor;
import net.vaultmc.vaultutils.utils.commands.experimental.Permission;
import net.vaultmc.vaultutils.utils.commands.experimental.RootCommand;
import net.vaultmc.vaultutils.utils.commands.experimental.SubCommand;
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
