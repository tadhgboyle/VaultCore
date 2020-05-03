package net.vaultmc.vaultcore.misc.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(
        literal = "spawn",
        description = "Teleports you to the world spawn."
)
@Permission(Permissions.SpawnCommand)
@PlayerOnly
public class SpawnCommand extends CommandExecutor {
    public SpawnCommand() {
        register("spawn", Collections.emptyList());
    }

    @SubCommand("spawn")
    public void spawn(VLPlayer sender) {
        if (sender.getWorld().getName().equalsIgnoreCase("skyblock")) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.spawn.disabled"));
            return;
        }
        String world = sender.getWorld().getName();
        if (world.equalsIgnoreCase("creative") || world.equalsIgnoreCase("Lobby"))
            sender.teleport(sender.getWorld().getSpawnLocation());
        else
            sender.teleportNoMove(sender.getWorld().getSpawnLocation());
    }
}
