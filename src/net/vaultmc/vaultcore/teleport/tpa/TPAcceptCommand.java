package net.vaultmc.vaultcore.teleport.tpa;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(literal = "tpaccept", description = "Request to teleport to a player.")
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPAcceptCommand extends CommandExecutor {
    public TPAcceptCommand() {
        register("tpaccept", Collections.emptyList());
    }

    @SubCommand("tpaccept")
    public void tpAccept(VLPlayer player) {
        // This is handled by bungee. This is just used as a registration.
    }
}