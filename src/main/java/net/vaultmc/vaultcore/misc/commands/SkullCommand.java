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
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import java.util.Collections;

@RootCommand(literal = "skull", description = "Get a player's head.")
@Permission(Permissions.SkullCommand)
@Aliases({"head"})
@PlayerOnly
public class SkullCommand extends CommandExecutor {

    public SkullCommand() {
        register("skull", Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
    }

    @SubCommand("skull")
    public void skull(VLPlayer sender, VLOfflinePlayer target) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(target.getUniqueId());
        sender.getInventory().addItem(new ItemStackBuilder(Material.PLAYER_HEAD)
                .skullOwner(player)
                .build());
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.skull"), target.getFormattedName()));
    }
}
