package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.listeners.PlayerTPListener;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Location;

import java.util.Collections;

@RootCommand(
        literal = "back",
        description = "Teleport to your previous location."
)
@Permission(Permissions.BackCommand)
@PlayerOnly
public class BackCommand extends CommandExecutor {
    public BackCommand() {
        this.register("back", Collections.emptyList());
    }

    @SubCommand("back")
    public void back(VLPlayer player) {
        if (PlayerTPListener.teleports.containsKey(player.getUniqueId())) {
            Location before = PlayerTPListener.teleports.get(player.getUniqueId());
            player.teleport(before);
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.back.success"));
            PlayerTPListener.teleports.remove(player.getUniqueId());
        } else {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.back.no_teleport_location"));
        }
    }
}