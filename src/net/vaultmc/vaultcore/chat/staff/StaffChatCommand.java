package net.vaultmc.vaultcore.chat.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RootCommand(literal = "staffchat", description = "Use staff chat.")
@Permission(Permissions.StaffChatCommand)
@Aliases("sc")
public class StaffChatCommand extends CommandExecutor {
    public static final Set<UUID> toggled = new HashSet<>();

    public StaffChatCommand() {
        register("chat", Collections.singletonList(Arguments.createArgument("message", Arguments.greedyString())));
        register("toggle", Collections.singletonList(Arguments.createLiteral("toggle")));
    }

    @SubCommand("chat")
    public static void chat(VLCommandSender sender, String message) {
        for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
            if (player.hasPermission(Permissions.StaffChatCommand)) {
                player.sendMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.prefix")
                        + Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.format"),
                        sender.getFormattedName(), message));
            }
        }
    }

    @SubCommand("toggle")
    @PlayerOnly
    public void toggle(VLPlayer player) {
        if (toggled.contains(player.getUniqueId())) {
            toggled.remove(player.getUniqueId());
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.toggle"), "off"));
        } else {
            toggled.add(player.getUniqueId());
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.toggle"), "on"));
        }
    }
}