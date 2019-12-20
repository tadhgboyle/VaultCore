package net.vaultmc.vaultcore.commands.warp;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(
        literal = "warp",
        description = "Teleport to a warp."
)
@Permission(Permissions.WarpCommand)
@PlayerOnly
public class WarpCommand extends CommandExecutor {
    private String string = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("string"));
    private String variable1 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-1"));

    public WarpCommand() {
        register("warp", Collections.singletonList(Arguments.createArgument("warp", Arguments.word())),
                "vaultcore");
    }

    @SubCommand("warp")
    public void warp(CommandSender sender, String warp) {
        if (VaultCore.getInstance().getConfig().get("warps." + warp) == null) {
            sender.sendMessage(string + "The warp " + variable1 + warp + string + " does not exist!");
        } else {
            Location location = (Location) VaultCore.getInstance().getConfig().get("warps." + warp);
            ((Player) sender).teleport(location);
            sender.sendMessage(string + "You have been teleported to " + variable1 + warp + string + "!");
        }
    }
}