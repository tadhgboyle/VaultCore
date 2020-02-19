package net.vaultmc.vaultcore.messenger;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.messenger.MessageReceivedEvent;
import net.vaultmc.vaultloader.utils.messenger.SQLMessenger;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GetServerService extends ConstructorRegisterListener {
    private static final Multimap<UUID, GeneralCallback<String>> callbacks = HashMultimap.create();
    private static final Map<UUID, Integer> count = new HashMap<>();

    public static void getServer(VLOfflinePlayer player, GeneralCallback<String> callback) {
        if (!callbacks.containsKey(player.getUniqueId())) {
            SQLMessenger.sendGlobalMessage("GetPlayerServer" + VaultCore.SEPARATOR + player.getUniqueId());
        }
        callbacks.put(player.getUniqueId(), callback);
    }

    @EventHandler
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().startsWith("GetPlayerServer")) {
            VLPlayer player = VLPlayer.getPlayer(UUID.fromString(e.getMessage().split(VaultCore.SEPARATOR)[1]));
            if (player != null) {
                SQLMessenger.sendGlobalMessage("PlayerServerResponse" + VaultCore.SEPARATOR + player.getUniqueId().toString() +
                        VaultCore.SEPARATOR + VaultCore.getInstance().getConfig().getString("server"));
            } else {
                SQLMessenger.sendGlobalMessage("PlayerServerResponse" + VaultCore.SEPARATOR + e.getMessage().split(VaultCore.SEPARATOR)[1] +
                        VaultCore.SEPARATOR + "failure");
            }
        } else if (e.getMessage().startsWith("PlayerServerResponse")) {
            UUID uuid = UUID.fromString(e.getMessage().split(VaultCore.SEPARATOR)[1]);
            String response = e.getMessage().split(VaultCore.SEPARATOR)[2];
            int i = count.getOrDefault(uuid, 0);
            i++;
            count.put(uuid, i);
            if (!response.equalsIgnoreCase("failure")) {
                for (GeneralCallback<String> callback : callbacks.get(uuid)) {
                    callback.getSuccess().accept(response);
                }
                callbacks.removeAll(uuid);
                return;
            }

            if (i == VaultCore.TOTAL_SERVERS && callbacks.containsKey(uuid)) {
                for (GeneralCallback<String> callback : callbacks.get(uuid)) {
                    callback.getFailure().accept("failure");
                }
                callbacks.removeAll(uuid);
            }
        } else if (e.getMessage().startsWith("312Message")) {
            String[] parts = e.getMessage().split(VaultCore.SEPARATOR);
            if (VLPlayer.getPlayer(UUID.fromString(parts[1])) != null) {
                VLPlayer.getPlayer(UUID.fromString(parts[1])).sendMessage(parts[2]);
            }
        }
    }
}
