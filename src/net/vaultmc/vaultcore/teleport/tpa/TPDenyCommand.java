package net.vaultmc.vaultcore.teleport.tpa;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.messenger.SQLMessenger;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RootCommand(literal = "tpdeny", description = "Deny a teleport request from a player.")
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPDenyCommand extends CommandExecutor {
    private static Map<UUID, UUID> requests = TPACommand.getRequests();

    public TPDenyCommand() {
        register("tpdeny", Collections.emptyList());
    }

    @SubCommand("tpdeny")
    @SneakyThrows
    public void tpDeny(VLPlayer player) {
        Map.Entry<String, TPASessionData> session = TPACommand.getSessionData(player.getUniqueId());
        if (session != null && session.getValue().getTo() == player.getUniqueId()) {
            SQLMessenger.sendGlobalMessage("312Message" + VaultCore.SEPARATOR + session.getValue().getFrom().toString() +
                    VaultCore.SEPARATOR + (VaultLoader.getMessage("vaultcore.commands.tpa.requests.response_sender")
                    .replace("{TARGET}", player.getFormattedName())
                    .replace("{STATUS}", "denied")));
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.response_target")
                    .replace("{SENDER}", VLOfflinePlayer.getOfflinePlayer(session.getValue().getFrom()).getFormattedName())
                    .replace("{STATUS}", "denied"));
            TPACommand.getSessions().remove(session.getKey());
        } else {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.no_request_error"));
        }
    }
}