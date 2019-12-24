package net.vaultmc.vaultcore.commands.warp;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;

@RootCommand(
        literal = "delwarp",
        description = "Delete a warp."
)
@Permission(Permissions.WarpCommandDelete)
@PlayerOnly
public class DelWarpCommand extends CommandExecutor {
    private String string = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("string"));
    private String variable1 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-1"));

    public DelWarpCommand() {
        register("delwarp", Collections.singletonList(Arguments.createArgument("warp", Arguments.word())),
                "vaultcore");
    }

    @SubCommand("delwarp")
    public void delWarp(CommandSender sender, String warp) {
        if (VaultCore.getInstance().getConfig().get("warps." + warp) == null) {
            sender.sendMessage(string + "The warp " + variable1 + warp + string + " does not exist!");
            return;
        }

        VaultCore.getInstance().getConfig().set("warps." + warp, null);
        VaultCore.getInstance().saveConfig();
        sender.sendMessage(string + "Warp " + variable1 + warp + string + " has been deleted!");
    }
}