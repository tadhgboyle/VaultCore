package net.vaultmc.vaultcore.teleport.tpa;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;

import java.util.Collections;

@RootCommand(literal = "tpaccept", description = "Request to teleport to a player.")
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPAcceptCommand extends CommandExecutor {
    public TPAcceptCommand() {
        register("tpaccept", Collections.emptyList());
    }

    @SubCommand("tpaccept")
    public void tpaccept(VLPlayer target) {
        // Debugging
        for (VLPlayer p : TPACommand.getTpaRequests().keySet()) {
            Bukkit.getLogger().info("tpa:" + p.getDisplayName());
        }
        for (VLPlayer p : TPAHereCommand.getTpaRequestsHere().keySet()) {
            Bukkit.getLogger().info("tpahere:" + p.getDisplayName());
        }
        if (TPACommand.getTpaRequests().containsKey(target)) {
            VLPlayer requester = TPACommand.getTpaRequests().get(target);
            target.teleport(requester.getLocation());
            TPACommand.getTpaRequests().remove(target);
        } else if (TPAHereCommand.getTpaRequestsHere().containsKey(target)) {
            VLPlayer requester = TPAHereCommand.getTpaRequestsHere().get(target);
            target.teleport(requester.getLocation());
            TPAHereCommand.getTpaRequestsHere().remove(target);
        } else target.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.no_request_error"));
    }
}