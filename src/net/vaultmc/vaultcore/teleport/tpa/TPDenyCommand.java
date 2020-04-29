package net.vaultmc.vaultcore.teleport.tpa;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
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
    public void tpDeny(VLPlayer sender) {

        if (!(TPACommand.getTpaRequests().containsKey(sender) && TPAHereCommand.getTpaRequestsHere().containsKey(sender))) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.no_request_error"));
        } else {
            TPACommand.getTpaRequests().remove(sender);
            TPAHereCommand.getTpaRequestsHere().remove(sender);
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.declined"));
        }
    }
}