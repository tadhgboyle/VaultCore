package net.vaultmc.vaultcore.teleport.tpa;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(literal = "tpdeny", description = "Deny a teleport request from a player.")
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPDenyCommand extends CommandExecutor {
    public TPDenyCommand() {
        register("tpdeny", Collections.emptyList());
    }

    @SubCommand("tpdeny")
    @SneakyThrows
    public void tpDeny(VLPlayer player) {
        // This is handled by Bungee. This is just used as a registration.
    }
}