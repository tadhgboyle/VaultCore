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

package net.vaultmc.vaultcore.survival.home;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@RootCommand(
        literal = "sethome",
        description = "Create a home."
)
@Permission(Permissions.Home)
@PlayerOnly
public class SetHomeCommand extends CommandExecutor {
    public SetHomeCommand() {
        register("setHome", Collections.emptyList());
        register("setHomeHome", Collections.singletonList(Arguments.createArgument("name", Arguments.word())));
    }

    @SubCommand("setHome")
    public void setHome(VLPlayer sender) {
        setHomeHome(sender, "home");
    }

    @SubCommand("setHomeHome")
    public void setHomeHome(VLPlayer sender, String home) {
        if (sender.getWorld().getName().equalsIgnoreCase("Survival") || sender.getWorld().getName().equalsIgnoreCase("clans")) {
            Set<String> homes = sender.getPlayerData().getKeys().stream().filter(n -> n.startsWith("home.")).collect(Collectors.toSet());
            homes.add(home);
            if (sender.hasPermission(Permissions.SetHomeBase + homes.size())) {
                sender.getPlayerData().set("home." + home, Utilities.serializeLocation(sender.getLocation()));
                sender.sendMessage(VaultLoader.getMessage("home.created").replace("{HOME}", home));
            } else {
                sender.sendMessage(VaultLoader.getMessage("home.too-many"));
            }
        } else {
            sender.sendMessage(VaultLoader.getMessage("home.invalid-world"));
        }
    }
}
