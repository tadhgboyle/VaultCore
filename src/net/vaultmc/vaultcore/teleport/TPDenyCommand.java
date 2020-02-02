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

@RootCommand(literal = "tpdeny", description = "Deny a teleport request from a player.")
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPDenyCommand extends CommandExecutor {
    private static HashMap<UUID, UUID> requests = TPACommand.getRequests();

    public TPDenyCommand() {
        register("tpdeny", Collections.emptyList());
    }

    @SubCommand("tpdeny")
    public void tpdeny(VLPlayer player) {
        if (requests.containsKey(player.getUniqueId())) {
            VLPlayer target = VLPlayer.getPlayer(Bukkit.getPlayer(requests.get(player.getUniqueId())));
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.response_target"),
                            "declined", target.getFormattedName()));

            target.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.response_sender"),
                            player.getFormattedName(), "declined"));
            requests.remove(player.getUniqueId());
            return;
        }
        player.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.no_request_error"));
    }
}