package net.vaultmc.vaultcore.settings;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(literal = "settings", description = "Open the settings menu.")
@Permission(Permissions.SettingsCommand)
@PlayerOnly
public class SettingsCommand extends CommandExecutor {
    public SettingsCommand() {
        this.register("settings", Collections.emptyList());
    }

    @SubCommand("settings")
    public void settings(VLPlayer player) {
        SettingsInventories.init(player);
        player.openInventory(SettingsInventories.settingsMain());
    }
}