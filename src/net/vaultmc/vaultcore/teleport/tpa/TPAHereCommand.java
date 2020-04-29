package net.vaultmc.vaultcore.teleport.tpa;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.chat.IgnoreCommand;
import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.messenger.SQLMessenger;
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

    public static HashMap<VLPlayer, VLPlayer> tpaRequestsHere = new HashMap<>();

    public TPAHereCommand() {
        register("tpahere", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    @SubCommand("tpahere")
    public void tpahere(VLPlayer sender, VLPlayer target) {
        // Check ignore, disabled tpa etc
        if (TPACommand.verifyRequest(sender, target)) return;
        // Check if either has a pending request
        if (TPACommand.checkPendingReq(sender, target, tpaRequestsHere)) return;
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpahere.request_sent"), target.getFormattedName()));
        target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpahere.request_received"), sender.getFormattedName()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        tpaRequestsHere.remove(VLPlayer.getPlayer(e.getPlayer()));
    }
}