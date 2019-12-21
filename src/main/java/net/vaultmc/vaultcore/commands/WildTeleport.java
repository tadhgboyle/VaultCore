package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultutils.utils.commands.experimental.CommandExecutor;
import net.vaultmc.vaultutils.utils.commands.experimental.Permission;
import net.vaultmc.vaultutils.utils.commands.experimental.RootCommand;
import net.vaultmc.vaultutils.utils.commands.experimental.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

@RootCommand(
        literal = "wild",
        description = "Teleport to a random location."
)
@Permission(Permissions.WildTeleport)
public class WildTeleport extends CommandExecutor {
    private String string = VaultCore.getInstance().getConfig().getString("string");
    private String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");
    private String variable2 = VaultCore.getInstance().getConfig().getString("variable-2");

    public WildTeleport() {
        register("wild", Collections.emptyList(), "vaultcore");
    }

    @SubCommand("wild")
    public void wild(CommandSender sender) {
        Player player = (Player) sender;
        if (player.getWorld().getName().equalsIgnoreCase("Survival")
                || player.getWorld().getName().equalsIgnoreCase("clans")) {

            Location originalLocation = player.getLocation().clone();
            int x = ThreadLocalRandom.current().nextInt(-100000, 100000);
            int z = ThreadLocalRandom.current().nextInt(-100000, 100000);
            int y = player.getWorld().getHighestBlockYAt(x, z);

            Location newLocation = new Location(player.getWorld(), x, y + 1, z);
            player.teleport(newLocation);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', string + "You have been teleported "
                    + variable2 + newLocation.distance(originalLocation) + string + " blocks away!"));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    string + "You must be in the " + variable1 + "Survival" + string + " or " + variable1 + "Clans"
                            + string + " world to run this command."));
        }
    }
}