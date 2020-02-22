package net.vaultmc.vaultcore.chat.msg;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.messenger.MessageReceivedEvent;
import net.vaultmc.vaultloader.utils.messenger.SQLMessenger;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RootCommand(literal = "socialspy", description = "View messages players are sending server-wide.")
@Permission(Permissions.SocialSpyCommand)
@PlayerOnly
public class SocialSpyCommand extends CommandExecutor implements Listener {
    public static final Set<UUID> toggled = new HashSet<>();

    public SocialSpyCommand() {
        register("toggle", Collections.emptyList());
        VaultCore.getInstance().registerEvents(this);
    }

    @EventHandler
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().startsWith("514SocialSpy")) {
            String[] parts = e.getMessage().split(VaultCore.SEPARATOR);
            boolean b = Boolean.parseBoolean(parts[2]);
            if (b) {
                toggled.add(UUID.fromString(parts[1]));
            } else {
                toggled.remove(UUID.fromString(parts[1]));
            }
        }
    }

    @SubCommand("toggle")
    public void toggle(VLPlayer player) {
        if (toggled.contains(player.getUniqueId())) {
            SQLMessenger.sendGlobalMessage("514SocialSpy" + VaultCore.SEPARATOR + player.getUniqueId().toString() + VaultCore.SEPARATOR + false);
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.socialspy.toggle"), "off"));
        } else {
            SQLMessenger.sendGlobalMessage("514SocialSpy" + VaultCore.SEPARATOR + player.getUniqueId().toString() + VaultCore.SEPARATOR + true);
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.socialspy.toggle"), "on"));
        }
    }
}
