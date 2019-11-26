package net.vaultmc.vaultcore.commands.warp;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(
        literal = "setwarp",
        description = "Set a warp."
)
@Permission(Permissions.WarpCommandSet)
@PlayerOnly
public class SetWarpCommand extends CommandExecutor {
    private String string = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("string"));
    private String variable1 = ChatColor.translateAlternateColorCodes('&',
            VaultCore.getInstance().getConfig().getString("variable-1"));

    public SetWarpCommand() {
        register("setwarp", Collections.singletonList(Arguments.createArgument("warp", StringArgumentType.word())),
                "vaultcore");
    }

    @SubCommand("setwarp")
    public void setWarp(CommandSender sender, String warp) {
        VaultCore.getInstance().getConfig().set("warps." + warp, ((Player) sender).getLocation());
        VaultCore.getInstance().saveConfig();
        sender.sendMessage(string + "Warp " + variable1 + warp + string + " has been set!");
    }
}