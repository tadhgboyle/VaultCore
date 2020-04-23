package net.vaultmc.vaultcore.teleport;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Location;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

@RootCommand(literal = "wild", description = "Teleport to a random location.")
@Permission(Permissions.WildTeleport)
@PlayerOnly
public class WildTeleportCommand extends CommandExecutor {
    public WildTeleportCommand() {
        register("wild", Collections.emptyList());
    }

    @SubCommand("wild")
    public void wild(VLPlayer player) {
        if (player.getWorld().getName().equalsIgnoreCase("Survival")
                || player.getWorld().getName().equalsIgnoreCase("clans")) {

            Location originalLocation = player.getLocation().clone();
            int x = ThreadLocalRandom.current().nextInt(-8000, 8000);
            int z = ThreadLocalRandom.current().nextInt(-8000, 8000);
            int y = player.getWorld().getHighestBlockYAt(x, z);

            Location newLocation = new Location(player.getWorld(), x, y + 1, z);
            player.teleportNoMove(newLocation);
            player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.wild.teleported"),
                    String.valueOf(Math.round(newLocation.distance(originalLocation)))));
        } else {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.wild.wrong_world"));
        }
    }
}