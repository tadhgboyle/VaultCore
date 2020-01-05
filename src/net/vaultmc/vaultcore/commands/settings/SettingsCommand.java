package net.vaultmc.vaultcore.commands.settings;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(
        literal = "settings",
        description = "Open the settings menu."
)
@Permission(Permissions.SettingsCommand)
public class SettingsCommand extends CommandExecutor {
    public SettingsCommand() {
        register("settings", Collections.emptyList());
    }

    @SubCommand("settings")
    public void settings(CommandSender sender) {
        Player player = (Player) sender;
        SettingsInventories.init(player);
        player.openInventory(SettingsInventories.SettingsMain(player));
    }
}