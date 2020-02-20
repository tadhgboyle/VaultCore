package net.vaultmc.vaultcore.teleport.tpa;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.messenger.MessageReceivedEvent;
import net.vaultmc.vaultloader.utils.messenger.SQLMessenger;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RootCommand(literal = "tpa", description = "Request to teleport to a player.")
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPACommand extends CommandExecutor implements Listener {
    @Getter
    private static final Map<UUID, UUID> requests = new HashMap<>();
    @Getter
    private static final Map<UUID, UUID> requestsHere = new HashMap<>();
    private static final Multimap<String, Boolean> tpaRequestStatus = HashMultimap.create();
    @Getter
    private static final Map<String, TPASessionData> sessions = new HashMap<>();

    public TPACommand() {
        register("tpa", Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
        VaultCore.getInstance().registerEvents(this);
    }

    public static Map.Entry<String, TPASessionData> getSessionData(UUID player) {
        for (Map.Entry<String, TPASessionData> session : sessions.entrySet()) {
            if (session.getValue().getFrom() == player || session.getValue().getTo() == player) {
                return session;
            }
        }
        return null;
    }

    @EventHandler
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().startsWith("01TPARequest")) {
            String[] parts = e.getMessage().split(VaultCore.SEPARATOR);
            String id = parts[1];
            VLOfflinePlayer from = VLOfflinePlayer.getOfflinePlayer(UUID.fromString(parts[2]));
            VLPlayer to = VLPlayer.getPlayer(UUID.fromString(parts[3]));
            if (to == null) {
                SQLMessenger.sendGlobalMessage("02TPARequestStatus" + VaultCore.SEPARATOR + id + VaultCore.SEPARATOR + "Failure");
                return;
            }
            if (!to.getPlayerData().getBoolean("settings.tpa")) {
                SQLMessenger.sendGlobalMessage("02TPARequestStatus" + VaultCore.SEPARATOR + id + VaultCore.SEPARATOR + "Failure");
                return;
            }
            SQLMessenger.sendGlobalMessage("02TPARequestStatus" + VaultCore.SEPARATOR + id + VaultCore.SEPARATOR + "Sent");
            sessions.put(id, new TPASessionData(from.getUniqueId(), to.getUniqueId()));
            to.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.tpa.request_received").replace("{SENDER}", from.getFormattedName()));
        } else if (e.getMessage().startsWith("02TPARequestStatus")) {
            String[] parts = e.getMessage().split(VaultCore.SEPARATOR);
            String id = parts[1];
            tpaRequestStatus.put(id, parts[2].equals("Sent"));
            if (tpaRequestStatus.get(id).size() == VaultCore.TOTAL_SERVERS) {
                if (tpaRequestStatus.get(id).contains(true)) {
                    VLPlayer player = VLPlayer.getPlayer(sessions.get(id).getFrom());
                    if (player != null) {
                        player.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.tpa.request_sent").replace("{TARGET}",
                                VLOfflinePlayer.getOfflinePlayer(sessions.get(id).getTo()).getFormattedName()));
                    }
                } else {
                    VLPlayer player = VLPlayer.getPlayer(sessions.get(id).getFrom());
                    if (player != null) {
                        player.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.request_failed"));
                    }
                }
                tpaRequestStatus.removeAll(id);
            }
        }
    }

    @SubCommand("tpa")
    public void tpa(VLPlayer player, VLOfflinePlayer target) {
        if (target.getUniqueId() == player.getUniqueId()) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.self_error"));
            return;
        }
        String id = UUID.randomUUID().toString();
        sessions.put(id, new TPASessionData(player.getUniqueId(), target.getUniqueId()));
        SQLMessenger.sendGlobalMessage("01TPARequest" + VaultCore.SEPARATOR + id + VaultCore.SEPARATOR + player.getUniqueId() +
                VaultCore.SEPARATOR + target.getUniqueId());
    }
}