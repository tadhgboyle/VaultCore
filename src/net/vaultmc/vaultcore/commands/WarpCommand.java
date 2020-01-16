package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Arrays;
import java.util.Collections;

@RootCommand(literal = "warp", description = "Teleport to a warp.")
@Permission(Permissions.WarpCommand)
@PlayerOnly
public class WarpCommand extends CommandExecutor {
    private String string = Utilities.string;
    private String variable1 = Utilities.variable1;

    public WarpCommand() {
        register("warp", Collections.singletonList(Arguments.createArgument("warp", Arguments.word())));
        register("setWarp",
                Arrays.asList(Arguments.createLiteral("set"), Arguments.createArgument("name", Arguments.word())));
        register("delWarp",
                Arrays.asList(Arguments.createLiteral("delete"), Arguments.createArgument("name", Arguments.word())));
    }

    @SubCommand("warp")
    public void warp(VLPlayer sender, String warp) {
        if (VaultCore.getInstance().getLocationFile().get("warps." + warp) == null) {
            sender.sendMessage(string + "The warp " + variable1 + warp + string + " does not exist!");
        } else {
            sender.teleport(VaultCore.getInstance().getLocationFile().getLocation("warps." + warp));
            sender.sendMessage(string + "You have been teleported to " + variable1 + warp + string + "!");
        }
    }

    @SubCommand("setWarp")
    @Permission(Permissions.WarpCommandSet)
    public void setWarp(VLPlayer sender, String warp) {
        if (VaultCore.getInstance().getLocationFile().get("warps." + warp) == null) {
            VaultCore.getInstance().getLocationFile().set("warps." + warp, sender.getLocation());
            VaultCore.getInstance().saveLocations();
            sender.sendMessage(string + "Warp " + variable1 + warp + string + " has been set!");
        } else {
            sender.sendMessage(string + "The warp " + variable1 + warp + string + " already exists. Try to delete it first.");
        }
    }

    @SubCommand("delWarp")
    @Permission(Permissions.WarpCommandDelete)
    public void delWarp(VLCommandSender sender, String warp) {
        if (VaultCore.getInstance().getLocationFile().get("warps." + warp) == null) {
            sender.sendMessage(string + "The warp " + variable1 + warp + string + " does not exist!");
            return;
        }

        VaultCore.getInstance().getLocationFile().set("warps." + warp, null);
        VaultCore.getInstance().saveLocations();
        sender.sendMessage(string + "Warp " + variable1 + warp + string + " has been deleted!");
    }
}