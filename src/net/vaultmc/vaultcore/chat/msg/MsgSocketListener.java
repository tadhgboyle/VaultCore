package net.vaultmc.vaultcore.chat.msg;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.socket.GeneralSocketListener;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;

public class MsgSocketListener implements Runnable {
    private static final Multimap<UUID, Boolean> responses = HashMultimap.create();
    @Getter
    private static BufferedReader reader;
    @Getter
    private static BufferedWriter writer;

    @SneakyThrows
    public MsgSocketListener() {
        // Don't tell me this is not recommended. I've used the scheduler before.
        new Thread(this, "Messaging Socket Listener").start();
        if (reader == null) reader = new BufferedReader(new InputStreamReader(VaultCore.getSocket().getInputStream()));
        if (writer == null)
            writer = new BufferedWriter(new OutputStreamWriter(VaultCore.getSocket().getOutputStream()));
    }

    @SneakyThrows
    private static void addToResponse(UUID id, VLPlayer from, VLOfflinePlayer to, String message, boolean success) {
        if (success) {
            responses.put(id, true);
            from.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"),
                    from.getFormattedName(), to.getFormattedName(), message));
        } else {
            responses.put(id, false);
            String ping = UUID.randomUUID().toString();
            GeneralSocketListener.getWriter().write("Ping" + VaultCore.SEPARATOR + ping + "\n");
            Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
                if (responses.size() == GeneralSocketListener.getPong().get(ping)) {
                    VaultCore.getInstance().getLogger().info("Received pong " + GeneralSocketListener.getPong().get(ping) + " from global server");
                    if (!responses.containsValue(true)) {
                        from.sendMessage(VaultLoader.getMessage("vaultcore.commands.msg.failed"));
                    }
                    responses.removeAll(id);
                    GeneralSocketListener.getPong().remove(ping);
                }
            }, 5);
        }
    }

    @SneakyThrows
    private static void status(String status, String id, UUID from, UUID to, String message) {
        writer.write("MsgStatus" + VaultCore.SEPARATOR + id + VaultCore.SEPARATOR + from.toString() + VaultCore.SEPARATOR +
                to.toString() + VaultCore.SEPARATOR + message + VaultCore.SEPARATOR + status + "\n");
    }

    @Override
    @SneakyThrows
    public void run() {
        while (true) {
            String response;
            while ((response = reader.readLine()) != null) {
                if (response.startsWith("MsgFromTo")) {
                    VaultCore.getInstance().getLogger().info("Received " + response + " from global server");
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
                        MsgCommand.getReplies().put(from.getUniqueId(), to.getUniqueId());

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
                    VaultCore.getInstance().getLogger().info("Received " + response + " from global server");
                    String[] parts = response.split(VaultCore.SEPARATOR);
                    UUID id = UUID.fromString(parts[1]);
                    if (MsgCommand.getSessions().containsValue(id)) {
                        VLPlayer from = VLPlayer.getPlayer(MsgCommand.getSessionsReversed().get(id));
                        addToResponse(id, from, VLOfflinePlayer.getOfflinePlayer(UUID.fromString(parts[3])),
                                parts[4], parts[5].equals("Success"));
                    }
                }
            }
        }
    }
}
