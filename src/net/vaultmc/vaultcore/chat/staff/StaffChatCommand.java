package net.vaultmc.vaultcore.chat.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.messenger.MessageReceivedEvent;
import net.vaultmc.vaultloader.utils.messenger.SQLMessenger;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RootCommand(literal = "staffchat", description = "Use staff chat.")
@Permission(Permissions.StaffChatCommand)
@Aliases("sc")
public class StaffChatCommand extends CommandExecutor implements Listener {
    public static final Set<UUID> toggled = new HashSet<>();

    public StaffChatCommand() {
        register("chat", Collections.singletonList(Arguments.createArgument("message", Arguments.greedyString())));
        register("toggle", Collections.singletonList(Arguments.createLiteral("toggle")));
        VaultCore.getInstance().registerEvents(this);
    }

    @SubCommand("chat")
    public static void chat(VLCommandSender sender, String message) {
        SQLMessenger.sendGlobalMessage("SCChat" + VaultCore.SEPARATOR + sender.getFormattedName() + VaultCore.SEPARATOR + message);
    }

    @SubCommand("toggle")
    @PlayerOnly
    public void toggle(VLPlayer player) {
        if (toggled.contains(player.getUniqueId())) {
            SQLMessenger.sendGlobalMessage("SCSetAlwaysOn" + VaultCore.SEPARATOR + player.getUniqueId().toString() + VaultCore.SEPARATOR + "false");
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.toggle"), "off"));
        } else {
            SQLMessenger.sendGlobalMessage("SCSetAlwaysOn" + VaultCore.SEPARATOR + player.getUniqueId().toString() + VaultCore.SEPARATOR + "true");
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.toggle"), "on"));
        }
    }

    @EventHandler
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().startsWith("SCChat")) {
            String[] parts = e.getMessage().trim().split(VaultCore.SEPARATOR);
            for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                if (player.hasPermission(Permissions.StaffChatCommand)) {
                    player.sendMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.prefix")
                            + Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.format"),
                            parts[1], parts[2]));
                }
            }
        } else if (e.getMessage().startsWith("SCSetAlwaysOn")) {
            String[] parts = e.getMessage().trim().split(VaultCore.SEPARATOR);
            UUID uuid = UUID.fromString(parts[1]);
            boolean alwaysOn = Boolean.parseBoolean(parts[2]);
            if (alwaysOn) {
                toggled.add(uuid);
            } else {
                toggled.remove(uuid);
            }
        }
    }
}