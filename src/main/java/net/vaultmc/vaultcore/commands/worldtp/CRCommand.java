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
        literal = "cr",
        description = "Teleport to the Creative world."
)
@Permission(Permissions.WorldTPCommandCreative)
@PlayerOnly
public class CRCommand extends CommandExecutor {
    public CRCommand() {
        register("cr", Collections.emptyList());
    }

    @SubCommand("cr")
    public void cr(CommandSender sender) {
        String string = ChatColor.translateAlternateColorCodes('&',
                VaultCore.getInstance().getConfig().getString("string"));
        String variable1 = ChatColor.translateAlternateColorCodes('&',
                VaultCore.getInstance().getConfig().getString("variable-1"));
        Player player = (Player) sender;

        Location cr = VaultCore.getInstance().getPlayerData().getLocation("players." + player.getUniqueId() + ".cr");
        if (cr == null) {
            player.sendMessage(string + "You have never joined this world before... Bringing you to spawn.");
            player.teleport(Bukkit.getWorld("creative").getSpawnLocation());
        } else {
            player.teleport(cr);
            player.sendMessage(string + "Teleported you to the " + variable1 + "Creative" + string + " world.");
        }
    }
}