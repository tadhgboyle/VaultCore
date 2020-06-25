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

package net.vaultmc.vaultcore.pvp;

import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(
        literal = "kits",
        description = "Grab a kit!"
)
@Permission(Permissions.KitGuiCommand)
@PlayerOnly
public class KitCommand extends CommandExecutor {
    public KitCommand() {
        register("kits", Collections.emptyList(), "vaultcore");
    }

    @SubCommand("kits")
    public void kits(VLPlayer p) {
        if (!p.getWorld().getName().equalsIgnoreCase("pvp")) {
            p.sendMessage(ChatColor.RED + "You must be in PvP world to use this command!");
            return;
        }

        KitGuis.openKitGui(p);
    }
}
