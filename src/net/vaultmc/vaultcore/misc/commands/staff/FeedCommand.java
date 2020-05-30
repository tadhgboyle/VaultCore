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

package net.vaultmc.vaultcore.misc.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collection;
import java.util.Collections;

@RootCommand(literal = "feed", description = "Feed a player.")
@Permission(Permissions.FeedCommand)
public class FeedCommand extends CommandExecutor {
    public FeedCommand() {
        register("feedSelf", Collections.emptyList());
        register("feedOthers",
                Collections.singletonList(Arguments.createArgument("target", Arguments.playersArgument())));
    }

    @SubCommand("feedSelf")
    @PlayerOnly
    public void feedSelf(VLPlayer player) {
        player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.feed_heal.self"), "fed"));
        player.feed();
    }

    @SubCommand("feedOthers")
    @Permission(Permissions.FeedCommandOther)
    public void feedOthers(VLCommandSender sender, Collection<VLPlayer> targets) {
        for (VLPlayer target : targets) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.feed_heal.other"), "fed",
                    target.getFormattedName()));
            target.feed();
            target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.feed_heal.receiver"),
                    "fed", sender.getFormattedName()));
        }
    }
}