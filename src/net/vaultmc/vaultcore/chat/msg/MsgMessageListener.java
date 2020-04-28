package net.vaultmc.vaultcore.chat.msg;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.messenger.MessageReceivedEvent;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

import java.util.UUID;

public class MsgMessageListener extends ConstructorRegisterListener {
    @EventHandler
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().startsWith("MsgResponse")) {
            String[] parts = e.getMessage().split(VaultCore.SEPARATOR);
            String session = parts[1];
            String response = parts[2];
            if (MsgCommand.getSessions().containsValue(UUID.fromString(session))) {
                if (response.equalsIgnoreCase("failure")) {
                    VLPlayer player = VLPlayer.getPlayer(MsgCommand.getSessionsReversed().get(UUID.fromString(session)));
                    player.sendMessage(VaultLoader.getMessage("vaultcore.commands.msg.failed"));
                }
                MsgCommand.getSessions().remove(MsgCommand.getSessionsReversed().remove(UUID.fromString(session)));
            }
        } else if (e.getMessage().startsWith("SocialSpyMsg")) {
            String[] parts = e.getMessage().split(VaultCore.SEPARATOR);
            String message = parts[1];
            for (UUID uuid : SocialSpyCommand.toggled) {
                VLPlayer socialspy = VLPlayer.getPlayer(uuid);
                if (socialspy == null) continue;
                socialspy.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_PURPLE + "SS" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + message);
            }
        }
    }
}
