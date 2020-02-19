package net.vaultmc.vaultcore.chat.msg;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.messenger.MessageReceivedEvent;
import net.vaultmc.vaultloader.utils.messenger.SQLMessenger;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

import java.util.UUID;

public class MsgMessageListener extends ConstructorRegisterListener {
    private static final Multimap<UUID, Boolean> responses = HashMultimap.create();

    @SneakyThrows
    private static void addToResponse(UUID id, VLPlayer from, VLOfflinePlayer to, String message, boolean success) {
        if (success) {
            responses.put(id, true);
            from.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"),
                    from.getFormattedName(), to.getFormattedName(), message));
        } else {
            responses.put(id, false);
            if (responses.get(id).size() == VaultCore.TOTAL_SERVERS) {
                if (!responses.get(id).contains(true)) {
                    from.sendMessage(VaultLoader.getMessage("vaultcore.commands.msg.failed"));
                }
                responses.removeAll(id);
            }
        }
    }

    @SneakyThrows
    private static void status(String status, String id, UUID from, UUID to, String message) {
        SQLMessenger.sendGlobalMessage("MsgStatus" + VaultCore.SEPARATOR + id + VaultCore.SEPARATOR + from.toString() + VaultCore.SEPARATOR +
                to.toString() + VaultCore.SEPARATOR + message + VaultCore.SEPARATOR + status + "\n");
    }

    @EventHandler
    public void onMessageReceived(MessageReceivedEvent e) {
        String response = e.getMessage().trim();
        if (response.startsWith("MsgFromTo")) {
            String[] parts = response.split(VaultCore.SEPARATOR);

            String id = parts[1];
            VLOfflinePlayer from = VLOfflinePlayer.getOfflinePlayer(UUID.fromString(parts[2]));
            VLPlayer to = VLPlayer.getPlayer(UUID.fromString(parts[3]));
            String message = from.hasPermission(Permissions.ChatColor) ? ChatColor.translateAlternateColorCodes('&', parts[4].trim()) : parts[4].trim();
            if (to == null) {
                status("Failure", id, UUID.fromString(parts[2]), UUID.fromString(parts[3]), message);
                return;
            }

            if (to.getDataConfig().getBoolean("settings.msg", true)) {
                to.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"),
                        from.getFormattedName(), to.getFormattedName(), message));
                MsgCommand.getReplies().put(to.getUniqueId(), from.getUniqueId());

                for (VLPlayer socialspy : SocialSpyCommand.toggled) {
                    if (!socialspy.getFormattedName().equals(from.getFormattedName())
                            && !socialspy.getFormattedName().equals(to.getFormattedName())) {
                        socialspy.sendMessage(VaultLoader.getMessage("vaultcore.commands.socialspy.prefix")
                                + Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"),
                                from.getFormattedName(), to.getFormattedName(), message));
                    }
                }

                status("Success", id, UUID.fromString(parts[2]), UUID.fromString(parts[3]), message);
            } else {
                status("Failure", id, UUID.fromString(parts[2]), UUID.fromString(parts[3]), message);
            }
        } else if (response.startsWith("MsgStatus")) {
            String[] parts = response.split(VaultCore.SEPARATOR);
            UUID id = UUID.fromString(parts[1]);
            if (MsgCommand.getSessions().containsValue(id)) {
                VLPlayer from = VLPlayer.getPlayer(MsgCommand.getSessionsReversed().get(id));
                addToResponse(id, from, VLOfflinePlayer.getOfflinePlayer(UUID.fromString(parts[3])),
                        parts[4], parts[5].trim().equals("Success"));
            }
        }
    }
}
