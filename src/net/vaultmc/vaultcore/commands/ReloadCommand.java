package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import org.bukkit.ChatColor;

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
    public void reload(VLCommandSender sender) {
        VaultCore.getInstance().reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Configuration reloaded!");
    }
}
