package net.vaultmc.vaultcore.chat.staff;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.messenger.MessageReceivedEvent;
import net.vaultmc.vaultloader.utils.messenger.SQLMessenger;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RootCommand(
        literal = "adminchat",
        description = "Use Admin Chat."
)
@Permission(Permissions.AdminChatCommand)
public class AdminChatCommand extends CommandExecutor implements Listener {
    @Getter
    private static final Set<UUID> toggled = new HashSet<>();

    public AdminChatCommand() {
        register("chat", Collections.singletonList(Arguments.createArgument("message", Arguments.greedyString())));
        register("toggle", Collections.singletonList(Arguments.createLiteral("toggle")));
        VaultCore.getInstance().registerEvents(this);
    }

    @SubCommand("toggle")
    @PlayerOnly
    public void toggle(VLPlayer sender) {
        if (toggled.contains(sender.getUniqueId())) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.adminchat.toggle").replace("{TOGGLE}", "off"));
            SQLMessenger.sendGlobalMessage("521ACSetAlwaysOn" + VaultCore.SEPARATOR + sender.getUniqueId() + VaultCore.SEPARATOR + "false");
        } else {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.adminchat.toggle").replace("{TOGGLE}", "on"));
            SQLMessenger.sendGlobalMessage("521ACSetAlwaysOn" + VaultCore.SEPARATOR + sender.getUniqueId() + VaultCore.SEPARATOR + "true");
        }
    }

    @SubCommand("chat")
    public void chat(VLCommandSender sender, String message) {
        SQLMessenger.sendGlobalMessage("513ACChat" + VaultCore.SEPARATOR + sender.getFormattedName() + VaultCore.SEPARATOR + message);
    }

    @EventHandler
    public void onMessageReceived(MessageReceivedEvent e) {
        String[] parts = e.getMessage().split(VaultCore.SEPARATOR);
        if (e.getMessage().startsWith("521ACSetAlwaysOn")) {
            if (parts[2].equals("false")) toggled.remove(UUID.fromString(parts[1]));
            else toggled.add(UUID.fromString(parts[1]));
        } else if (e.getMessage().startsWith("513ACChat")) {
            Bukkit.broadcast(VaultLoader.getMessage("vaultcore.commands.adminchat.format")
                    .replace("{SENDER}", parts[1])
                    .replace("{MESSAGE}", parts[2]), Permissions.AdminChatCommand);
        }
    }
}
