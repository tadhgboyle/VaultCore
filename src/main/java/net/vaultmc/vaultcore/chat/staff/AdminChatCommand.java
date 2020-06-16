/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

package net.vaultmc.vaultcore.chat.staff;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;
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
@Aliases("ac")
public class AdminChatCommand extends CommandExecutor implements Listener {
    @Getter
    private static final Set<UUID> toggled = new HashSet<>();

    public AdminChatCommand() {
        register("chat", Collections.singletonList(Arguments.createArgument("message", Arguments.greedyString())));
        register("toggle", Collections.singletonList(Arguments.createLiteral("toggle")));
        VaultCore.getInstance().registerEvents(this);
    }

    @SubCommand("chat")
    public static void chat(VLCommandSender sender, String message) {
        if (sender instanceof VLPlayer && PlayerSettings.getSetting((VLPlayer) sender, "settings.grammarly")) {
            message = Utilities.grammarly(message);
        }
        for (VLPlayer players : VLPlayer.getOnlinePlayers()) {
            if (players.hasPermission(Permissions.AdminChatCommand)) {
                players.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.adminchat.format"),
                        sender.getFormattedName(), ChatColor.translateAlternateColorCodes('&', message)));
            }
        }
    }

    @SubCommand("toggle")
    @PlayerOnly
    public void toggle(VLPlayer sender) {
        if (toggled.contains(sender.getUniqueId())) {
            toggled.remove(sender.getUniqueId());
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.adminchat.toggle").replace("{TOGGLE}", "off"));
        } else {
            if (StaffChatCommand.checkToggled(sender, toggled)) return;
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.adminchat.toggle"), "on"));
        }
    }
}
