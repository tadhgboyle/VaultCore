package net.vaultmc.vaultcore.teleport;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

@RootCommand(literal = "tpaccept", description = "Request to teleport to a player.")
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPAcceptCommand extends CommandExecutor {
    private static HashMap<UUID, UUID> requests = TPACommand.getRequests();
    private static HashMap<UUID, UUID> requestsHere = TPACommand.getRequestsHere();

    public TPAcceptCommand() {
        register("tpaccept", Collections.emptyList());
    }

    @SubCommand("tpaccept")
    public void tpaccept(VLPlayer player) {
        if (requests.containsKey(player.getUniqueId())) {
            VLPlayer target = VLPlayer.getPlayer(Bukkit.getPlayer(requests.get(player.getUniqueId())));
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.response_target"),
                            "accepted", target.getFormattedName()));

            target.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.response_sender"),
                            player.getFormattedName(), "accepted"));
            target.teleport(player.getLocation());
            requests.remove(player.getUniqueId());
        } else if (requestsHere.containsKey(player.getUniqueId())) {

            VLPlayer target = VLPlayer.getPlayer(Bukkit.getPlayer(requestsHere.get(player.getUniqueId())));

            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.response_target"),
                            "accepted", target.getFormattedName()));
            target.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.response_sender"),
                            player.getFormattedName(), "accepted"));
            player.teleport(target.getLocation());
            requestsHere.remove(player.getUniqueId());
        } else {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.no_request_error"));
        }
    }
}