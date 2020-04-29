package net.vaultmc.vaultcore.teleport.tpa;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
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
    public void tpaccept(VLPlayer sender) {
        if (TPACommand.tpaRequests.containsKey(sender)) {
            VLPlayer requester = TPACommand.tpaRequests.get(sender);
            sender.teleport(requester);
            TPACommand.tpaRequests.remove(sender);
        } else if (TPAHereCommand.tpaRequestsHere.containsKey(sender)) {
            VLPlayer target = TPAHereCommand.tpaRequestsHere.get(sender);
            sender.teleport(target);
            TPAHereCommand.tpaRequestsHere.remove(sender);
        } else sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.no_request_error"));
    }
}