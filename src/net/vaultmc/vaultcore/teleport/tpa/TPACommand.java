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
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

@RootCommand(literal = "tpa", description = "Request to teleport to a player.")
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPACommand extends CommandExecutor implements Listener {

    public static HashMap<VLPlayer, VLPlayer> tpaRequests = new HashMap<>();

    public TPACommand() {
        register("tpa", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    @SubCommand("tpa")
    public void tpa(VLPlayer sender, VLPlayer target) {
        if (verifyRequest(sender, target)) return;
        if (PlayerSettings.getSetting(target, "settings.autotpa")) {
            sender.teleport(target);
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.auto_accept_sender"), target.getFormattedName()));
            target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.auto_accept_target"), sender.getFormattedName()));
            return;
        }
        if (checkPendingReq(sender, target, tpaRequests)) return;
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.request_sent"), target.getFormattedName()));
        target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.request_received"), sender.getFormattedName()));
    }

    public static boolean checkPendingReq(VLPlayer sender, VLPlayer target, HashMap<VLPlayer, VLPlayer> tpaRequests) {
        if (tpaRequests.containsKey(target)) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.pending_error"));
            return true;
        }
        if (tpaRequests.containsValue(sender)) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.overrode_request_sender"), tpaRequests.get(sender).getFormattedName()));
            tpaRequests.get(sender).sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.overrode_request_target"), sender.getFormattedName()));
            // No need to remove, as it will be overwritten automatically
        }
        tpaRequests.put(target, sender);
        return false;
    }

    public static boolean verifyRequest(VLPlayer sender, VLPlayer target) {
        if (sender == target) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.self_error"));
            return true;
        }
        if (IgnoreCommand.isIgnoring(target, sender)) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.ignore.you_are_ignored"));
            return true;
        }
        if (!PlayerSettings.getSetting(target, "settings.tpa")) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.disabled_tpa"));
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        tpaRequests.remove(VLPlayer.getPlayer(e.getPlayer()));
    }
}