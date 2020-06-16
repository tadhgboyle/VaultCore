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

package net.vaultmc.vaultcore.mods.forge;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(
        literal = "forgemods",
        description = "Get a list of installed Forge mods, reported by the client."
)
@Permission(Permissions.BrandCommand)
public class ForgeModsCommand extends CommandExecutor {
    public ForgeModsCommand() {
        register("mod", Collections.singletonList(
                Arguments.createArgument("player", Arguments.playerArgument())));
    }

    @SubCommand("mod")
    public void mods(VLCommandSender sender, VLPlayer player) {
        if (!ForgeModsListener.getMods().containsKey(player.getUniqueId())) {
            sender.sendMessage(VaultLoader.getMessage("forge-support.no-mods"));
            return;
        }
        sender.sendMessage(VaultLoader.getMessage("forge-support.mods")
                .replace("{PLAYER}", player.getFormattedName())
                .replace("{MODS}", String.join(", ", ForgeModsListener.getMods().get(player.getUniqueId()))));
    }
}
