package net.vaultmc.vaultcore.teleport;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(literal = "tpall", description = "Teleport all online players to you.")
@Permission(Permissions.TeleportCommand)
@PlayerOnly
public class TPAllCommand extends CommandExecutor {

    public TPAllCommand() {
        register("tpall", Collections.emptyList());
    }

    @SubCommand("tpall")
    public void tpall(VLPlayer sender) {
        for (VLPlayer players : VLPlayer.getOnlinePlayers()) {
            if (players == sender) continue;
            players.teleport(sender.getLocation());
            players.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpall"), sender.getFormattedName()));
        }
    }
}
