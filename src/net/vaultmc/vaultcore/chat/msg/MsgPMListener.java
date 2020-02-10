package net.vaultmc.vaultcore.chat.msg;

import lombok.Getter;
import lombok.SneakyThrows;
import net.vaultmc.vaultcore.MessengerUtils;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.*;

public class MsgPMListener extends ConstructorRegisterListener implements PluginMessageListener {
    @Getter
    private static final Map<UUID, UUID> msgSessions = new HashMap<>();
    @Getter
    private static final Map<UUID, Set<Boolean>> responses = new HashMap<>();

    private static void addToResponse(UUID id, VLPlayer from, VLOfflinePlayer to, String message, boolean response) {
        if (from == null) {
            responses.remove(id);
            return;
        }

        if (response) {
            responses.remove(id);
            from.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"),
                    from.getFormattedName(), to.getFormattedName(), message));
            return;
        }
        Set<Boolean> b = responses.getOrDefault(id, new HashSet<>());
        b.add(false);
        if (b.size() >= MessengerUtils.getServersBlocking().size()) {
            responses.remove(id);
            from.sendMessage(VaultLoader.getMessage("vaultcore.commands.msg.failed"));
            return;
        }
        responses.put(id, b);
    }

    @SneakyThrows
    private static void success(String id, String from, String to, String message) {
        ByteArrayOutputStream sendingStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(sendingStream);
        stream.writeUTF("Forward");
        stream.writeUTF("ALL");
        stream.writeUTF("vaultcore:tell");

        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(dataStream);
        os.writeUTF("TellStatus");
        os.writeUTF(id);
        os.writeUTF(from);
        os.writeUTF(to);
        os.writeUTF(message);
        os.writeUTF("Success");
        byte[] result = dataStream.toByteArray();

        stream.writeShort(result.length);
        stream.write(result);

        os.close();
        Bukkit.getWorlds().get(0).sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", sendingStream.toByteArray());
        stream.close();
    }

    @SneakyThrows
    private static void failure(String id, String from, String to, String message) {
        ByteArrayOutputStream sendingStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(sendingStream);
        stream.writeUTF("Forward");
        stream.writeUTF("ALL");
        stream.writeUTF("vaultcore:tell");

        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(dataStream);
        os.writeUTF("TellStatus");
        os.writeUTF(id);
        os.writeUTF(from);
        os.writeUTF(to);
        os.writeUTF(message);
        os.writeUTF("Failure");
        byte[] result = dataStream.toByteArray();

        stream.writeShort(result.length);
        stream.write(result);

        os.close();
        Bukkit.getWorlds().get(0).sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", sendingStream.toByteArray());
        stream.close();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        msgSessions.remove(e.getPlayer().getUniqueId());
    }

    @Override
    @SneakyThrows
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("vaultcore:tell")) {
            DataInputStream stream = new DataInputStream(new ByteArrayInputStream(message));
            String command = stream.readUTF();  // Must be TellFromTo or TellStatus
            String id = stream.readUTF();  // This will be used to identify the message when returning.
            // Must be unique for each message. Use UUID.randomUUID().

            if (command.equalsIgnoreCase("TellFromTo")) {
                // The player might be from another server
                VLOfflinePlayer from = VLOfflinePlayer.getOfflinePlayer(UUID.fromString(stream.readUTF()));
                VLPlayer target = VLPlayer.getPlayer(UUID.fromString(stream.readUTF()));
                String msg = stream.readUTF();

                if (target != null && target.getDataConfig().getBoolean("settings.msg", true)) {
                    //from.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"),
                    //        from.getFormattedName(), target.getFormattedName(), msg));
                    // The sender server will help us do so
                    target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"),
                            from.getFormattedName(), target.getFormattedName(), msg));
                    MsgCommand.getReplies().put(from.getUniqueId(), target.getUniqueId());
                    for (VLPlayer socialspy : SocialSpyCommand.toggled) {
                        if (!socialspy.getFormattedName().equals(from.getFormattedName())
                                && !socialspy.getFormattedName().equals(target.getFormattedName())) {
                            socialspy.sendMessage(VaultLoader.getMessage("vaultcore.commands.socialspy.prefix")
                                    + Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"),
                                    from.getFormattedName(), target.getFormattedName(), msg));
                        }
                    }
                    success(id, from.getUniqueId().toString(), target.getUniqueId().toString(), msg);
                } else {
                    failure(id, from.getUniqueId().toString(), "null", msg);
                }
            } else if (command.equalsIgnoreCase("TellStatus")) {
                UUID uuid = UUID.fromString(id);
                if (msgSessions.containsValue(uuid)) {
                    VLPlayer from = VLPlayer.getPlayer(UUID.fromString(stream.readUTF()));
                    String toUID = stream.readUTF();
                    VLOfflinePlayer to = !"null".equals(toUID) ? VLOfflinePlayer.getOfflinePlayer(UUID.fromString(toUID)) : null;
                    String msg = stream.readUTF();
                    boolean response = stream.readUTF().equals("Success");
                    addToResponse(uuid, from, to, msg, response);
                }
            }
            stream.close();
        }
    }
}
