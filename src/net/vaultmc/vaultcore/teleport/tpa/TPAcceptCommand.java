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
        TPACommand tpaCommand = new TPACommand();
        TPAHereCommand tpaHereCommand = new TPAHereCommand();
        // Debugging
        for (VLPlayer p : tpaCommand.getTpaRequests().keySet()) {
            Bukkit.getLogger().info("tpa:" + p.getDisplayName());
        }
        for (VLPlayer p : tpaHereCommand.getTpaRequestsHere().keySet()) {
            Bukkit.getLogger().info("tpahere:" + p.getDisplayName());
        }
        if (tpaCommand.getTpaRequests().containsKey(target)) {
            VLPlayer requester = tpaCommand.getTpaRequests().get(target);
            target.teleport(requester.getLocation());
            tpaCommand.getTpaRequests().remove(target);
        } else if (tpaHereCommand.getTpaRequestsHere().containsKey(target)) {
            VLPlayer requester = tpaHereCommand.getTpaRequestsHere().get(target);
            target.teleport(requester.getLocation());
            tpaHereCommand.getTpaRequestsHere().remove(target);
        } else target.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.no_request_error"));
    }
}