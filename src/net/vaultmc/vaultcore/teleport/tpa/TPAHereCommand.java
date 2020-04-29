package net.vaultmc.vaultcore.teleport.tpa;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.HashMap;

@RootCommand(
        literal = "tpahere",
        description = "Request a player to teleport to you."
)
@Permission(Permissions.TPAHereCommand)
@PlayerOnly
public class TPAHereCommand extends CommandExecutor implements Listener {

    @Getter
    public HashMap<VLPlayer, VLPlayer> tpaRequestsHere = new HashMap<>();

    public TPAHereCommand() {
        register("tpahere", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    @SubCommand("tpahere")
    public void tpahere(VLPlayer sender, VLPlayer target) {
        // Check ignore, disabled tpa etc
        if (TPACommand.verifyRequest(sender, target)) return;
        // Check if either has a pending request
        if (tpaRequestsHere.containsKey(target)) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.pending_error"));
            return;
        }
        if (tpaRequestsHere.containsValue(sender)) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.overrode_request_sender"), tpaRequestsHere.get(sender).getFormattedName()));
            tpaRequestsHere.get(sender).sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.overrode_request_target"), sender.getFormattedName()));
            // No need to remove, as it will be overwritten automatically
        }
        tpaRequestsHere.put(target, sender);
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpahere.request_sent"), target.getFormattedName()));
        target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpahere.request_received"), sender.getFormattedName()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        tpaRequestsHere.remove(VLPlayer.getPlayer(e.getPlayer()));
    }
}