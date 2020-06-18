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

package net.vaultmc.vaultcore.misc.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RootCommand(
        literal = "sendlarge",
        description = "Send a large message to all the players."
)
@Permission(Permissions.SendLargeCommand)
public class SendLargeCommand extends CommandExecutor {
    private static final Map<UUID, Long> delay = new HashMap<>();

    public SendLargeCommand() {
        register("send", Collections.singletonList(
                Arguments.createArgument("message", Arguments.greedyString())
        ));
    }

    @SubCommand("send")
    public void send(VLPlayer sender, String message) {
        if (delay.containsKey(sender.getUniqueId()) && System.currentTimeMillis() < delay.get(sender.getUniqueId()) &&
                !sender.hasPermission(Permissions.SendLargeBypass)) {
            sender.sendMessageByKey("vaultcore.commands.send-large.delay", "time",
                    Utilities.humanReadableTime((delay.get(sender.getUniqueId()) - System.currentTimeMillis()) / 1000));
            return;
        }
        for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
            player.getPlayer().sendTitle(sender.getExtraFormattedName(), ChatColor.YELLOW + message, 10, 20, 70);
        }
        delay.put(sender.getUniqueId(), System.currentTimeMillis() + 1800000);
    }
}
