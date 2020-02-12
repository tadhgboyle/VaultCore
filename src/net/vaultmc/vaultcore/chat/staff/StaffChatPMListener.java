package net.vaultmc.vaultcore.chat.staff;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.UUID;

public class StaffChatPMListener implements PluginMessageListener {
    @Override
    @SneakyThrows
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        DataInputStream bungee = new DataInputStream(new ByteArrayInputStream(message));
        if (bungee.readUTF().equals("vaultcore:staff_chat")) {
            byte[] buffer = new byte[bungee.readShort()];
            bungee.readFully(buffer);
            bungee.close();

            DataInputStream stream = new DataInputStream(new ByteArrayInputStream(buffer));

            String command = stream.readUTF();  // Must be Send or SetAlwaysOn
            if ("Send".equalsIgnoreCase(command)) {
                String nameFrom = stream.readUTF();
                String msg = stream.readUTF();
                for (VLPlayer x : VLPlayer.getOnlinePlayers()) {
                    if (x.hasPermission(Permissions.StaffChatCommand)) {
                        player.sendMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.prefix")
                                + Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.format"), nameFrom, msg));
                    }
                }
            } else if ("SetAlwaysOn".equalsIgnoreCase(command)) {
                UUID uuid = UUID.fromString(stream.readUTF());
                boolean status = stream.readBoolean();

                if (status) {
                    StaffChatCommand.toggled.add(uuid);
                } else {
                    StaffChatCommand.toggled.remove(uuid);
                }
            }
            stream.close();
        }
    }
}
