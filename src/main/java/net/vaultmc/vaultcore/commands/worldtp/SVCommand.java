package net.vaultmc.vaultcore.commands.worldtp;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(
        literal = "sv",
        description = "Teleport to the Survival world."
)
@Permission(Permissions.WorldTPCommandSurvival)
@PlayerOnly
public class SVCommand extends CommandExecutor {
    public SVCommand() {
        register("sv", Collections.emptyList(), "vaultcore");
    }

    @SubCommand("sv")
    public void sv(CommandSender sender) {
        String string = ChatColor.translateAlternateColorCodes('&',
                VaultCore.getInstance().getConfig().getString("string"));
        String variable1 = ChatColor.translateAlternateColorCodes('&',
                VaultCore.getInstance().getConfig().getString("variable-1"));
        Player player = (Player) sender;

        Location sv = VaultCore.getInstance().getPlayerData()
                .getLocation("players." + player.getUniqueId() + ".sv");
        if (sv == null) {
            player.sendMessage(string + "You have never joined this world before... Bringing you to spawn.");
            player.teleport(Bukkit.getWorld("Survival").getSpawnLocation());
        } else {
            player.teleport(sv);
            player.sendMessage(string + "Teleported you to the " + variable1 + "Survival" + string + " world.");
        }
    }
}