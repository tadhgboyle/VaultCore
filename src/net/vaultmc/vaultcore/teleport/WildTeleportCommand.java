package net.vaultmc.vaultcore.teleport;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

@RootCommand(literal = "wild", description = "Teleport to a random location.")
@Permission(Permissions.WildTeleport)
@PlayerOnly
@Aliases("rtp")
public class WildTeleportCommand extends CommandExecutor {
    public WildTeleportCommand() {
        register("wild", Collections.emptyList());
    }

    @SubCommand("wild")
    public void wild(VLPlayer player) {
        if (player.getWorld().getName().equalsIgnoreCase("Survival")
                || player.getWorld().getName().equalsIgnoreCase("clans")) {

            Location originalLocation = player.getLocation().clone();
            Location newLocation;

            do {
                newLocation = generateLoc(player);
            }
            while (newLocation.getBlock().getRelative(BlockFace.DOWN).getType() == Material.WATER);

            player.teleportNoMove(newLocation);
            player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.wild.teleported"),
                    String.valueOf(Math.round(newLocation.distance(originalLocation)))));
        } else {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.wild.wrong_world"));
        }
    }

    private Location generateLoc(VLPlayer player) {
        int x = ThreadLocalRandom.current().nextInt(-8000, 8000);
        int z = ThreadLocalRandom.current().nextInt(-8000, 8000);
        int y = player.getWorld().getHighestBlockYAt(x, z);
        return new Location(player.getWorld(), x, y + 1, z);
    }
}