package net.vaultmc.vaultcore.commands.settings;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(literal = "settings", description = "Open the settings menu.")
@Permission(Permissions.SettingsCommand)
@PlayerOnly
public class SettingsCommand extends CommandExecutor {
    public SettingsCommand() {
        this.register("settings", Collections.emptyList());
    }

    @SubCommand("settings")
    public void settings(CommandSender sender) {
        Player player = (Player) sender;
        SettingsInventories.init(player);
        player.openInventory(SettingsInventories.settingsMain());
    }
}