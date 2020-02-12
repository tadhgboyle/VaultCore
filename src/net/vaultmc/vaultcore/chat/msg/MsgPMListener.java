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

        Bukkit.getLogger().info("Adding to response: " + id + " " + from.getName() + " " + to.getName() + " " + message + " " + response);

        if (response) {
            responses.remove(id);
            from.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"),
                    from.getFormattedName(), to.getFormattedName(), message));
            MsgCommand.getReplies().put(from.getUniqueId(), to.getUniqueId());
            return;
        }
        Set<Boolean> b = responses.getOrDefault(id, new HashSet<>());
        b.add(false);
        MessengerUtils.getServers(servers -> {
            if (b.size() >= servers.size()) {
                responses.remove(id);
                from.sendMessage(VaultLoader.getMessage("vaultcore.commands.msg.failed"));
                return;
            }
            responses.put(id, b);
        });
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

        Bukkit.getLogger().info("Sending success for " + id + " " + from + " " + to + " " + message);

        os.close();
        Bukkit.getWorlds().get(0).sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", sendingStream.toByteArray());
        stream.close();
    }

    @SneakyThrows
    private static void failure(String id, String from, String to, String message) {
        sendTellStatusTo(id, from, to, message, "ALL", "Failure");
        MessengerUtils.getThisServer(server -> sendTellStatusTo(id, from, to, message, server, "Success"));
    }

    @SneakyThrows
    private static void sendTellStatusTo(String id, String from, String to, String message, String server, String status) {
        ByteArrayOutputStream sendingStream = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(sendingStream);
        stream.writeUTF("Forward");
        stream.writeUTF(server);
        stream.writeUTF("vaultcore:tell");

        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(dataStream);
        os.writeUTF("TellStatus");
        os.writeUTF(id);
        os.writeUTF(from);
        os.writeUTF(to);
        os.writeUTF(message);
        os.writeUTF(status);
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
        DataInputStream bungee = new DataInputStream(new ByteArrayInputStream(message));
        String subChannel = bungee.readUTF();
        Bukkit.getLogger().info("subChannel = " + subChannel);
        if (subChannel.equalsIgnoreCase("vaultcore:tell")) {
            byte[] bytes = new byte[bungee.readShort()];
            bungee.readFully(bytes);
            bungee.close();
            Bukkit.getLogger().info("bytes = " + Arrays.toString(bytes));

            DataInputStream stream = new DataInputStream(new ByteArrayInputStream(bytes));
            String command = stream.readUTF();  // Must be TellFromTo or TellStatus
            String id = stream.readUTF();  // This will be used to identify the message when returning.
            // Must be unique for each message. Use UUID.randomUUID().

            if (command.equalsIgnoreCase("TellFromTo")) {
                // The player might be from another server
                VLOfflinePlayer from = VLOfflinePlayer.getOfflinePlayer(UUID.fromString(stream.readUTF()));
                VLPlayer target = VLPlayer.getPlayer(UUID.fromString(stream.readUTF()));
                String msg = stream.readUTF();

                Bukkit.getLogger().info("Received TellFromTo: " + from.getName() + " " + target.getName() + " " + msg);

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
                    Bukkit.getLogger().info("Received TellStatus");
                    addToResponse(uuid, from, to, msg, response);
                }
            }
            stream.close();
        }
    }
}
