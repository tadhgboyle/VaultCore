package net.vaultmc.vaultcore.teleport.tpa;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.chat.IgnoreCommand;
import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.messenger.MessageReceivedEvent;
import net.vaultmc.vaultloader.utils.messenger.SQLMessenger;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.UUID;

@RootCommand(literal = "tpa", description = "Request to teleport to a player.")
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPACommand extends CommandExecutor implements Listener {
    public TPACommand() {
        register("tpa", Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
        VaultCore.getInstance().registerEvents(this);
    }

    @EventHandler
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().startsWith("TPAStatus")) {
            String[] parts = e.getMessage().split(VaultCore.SEPARATOR);
            VLPlayer from = VLPlayer.getPlayer(UUID.fromString(parts[1]));
            VLPlayer to = VLPlayer.getPlayer(UUID.fromString(parts[2]));
            if (from != null) {
                if (parts[3].equals("Sent")) {
                    if (PlayerSettings.getSetting(to.getOnlinePlayer(), "settings.autotpa") && to.isOnline()) {
                        from.teleport(to.getOnlinePlayer());
                        to.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.response.response_target"), "accepted", from.getFormattedName()));
                        from.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.response.response_sender"), to.getFormattedName(), "accepted"));
                    }
                    from.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.tpa.request_sent").replace("{TARGET}",
                            to.getFormattedName()));
                }
            }
        }
    }

    @SubCommand("tpa")
    public void tpa(VLPlayer player, VLOfflinePlayer target) {
        if (target == player) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.self_error"));
            return;
        }
        if (IgnoreCommand.isIgnoring(target, player)) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.ignore.you_are_ignored"));
            return;
        }
        SQLMessenger.sendGlobalMessage("TPA2jRequest" + VaultCore.SEPARATOR + player.getUniqueId() + VaultCore.SEPARATOR + target.getUniqueId() +
                VaultCore.SEPARATOR + player.getFormattedName() + VaultCore.SEPARATOR + "normal");
    }
}