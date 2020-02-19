package net.vaultmc.vaultcore.teleport.tpa;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.teleport.TPCommand;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;
import java.util.Map.Entry;

@RootCommand(literal = "tpaccept", description = "Request to teleport to a player.")
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPAcceptCommand extends CommandExecutor {
    public TPAcceptCommand() {
        register("tpaccept", Collections.emptyList());
    }

    @SubCommand("tpaccept")
    public void tpAccept(VLPlayer player) {
        Entry<String, TPASessionData> session = TPACommand.getSessionData(player.getUniqueId());
        if (session != null && session.getValue().getTo() == player.getUniqueId()) {
            TPCommand.teleport(VLOfflinePlayer.getOfflinePlayer(session.getValue().getFrom()), player, status -> {
                TPACommand.getSessions().remove(session.getKey());
                if (status) {
                    player.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.tpa.auto_accept_target").replace("{SENDER}",
                            VLOfflinePlayer.getOfflinePlayer(session.getValue().getFrom()).getFormattedName()));
                    VLPlayer.getPlayer(session.getValue().getFrom()).sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.tpa.auto_accept_sender")
                            .replace("{TARGET}", player.getFormattedName()));
                } else {
                    player.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.failed"));
                }
            });
        } else {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.no_request_error"));
        }
    }
}