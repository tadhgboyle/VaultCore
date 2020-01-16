package net.vaultmc.vaultcore.commands.teleport;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.util.Collections;

@RootCommand(literal = "tphere", description = "Teleport a player to you.")
@Permission(Permissions.TeleportCommandHere)
@PlayerOnly
public class TPHereCommand extends CommandExecutor {
    String string = Utilities.string;

    public TPHereCommand() {
        register("tphere", Collections.singletonList(Arguments.createArgument("player", Arguments.playerArgument())));
    }

    @SubCommand("tphere")
    public void tpaHere(VLPlayer player, VLPlayer target) {
        if (target == player) {
            player.sendMessage(ChatColor.RED + "You can't teleport to yourself!");
            return;
        }
        target.teleport(player);
        player.sendMessage(string + "You have teleported " + target.getFormattedName() + string + " to you.");

    }
}