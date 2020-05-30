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
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.World;

import java.util.Arrays;
import java.util.Collections;

@RootCommand(
        literal = "time",
        description = "Sets the time in your world (or a specified world)"
)
@Permission(Permissions.TimeCommand)
public class TimeCommand extends CommandExecutor {
    public TimeCommand() {
        unregisterExisting();

        register("getNoWorld", Collections.singletonList(Arguments.createLiteral("get")));
        register("getWithWorld", Arrays.asList(
                Arguments.createLiteral("get"),
                Arguments.createArgument("world", Arguments.worldArgument())
        ));
        register("setNoWorld", Arrays.asList(
                Arguments.createLiteral("set"),
                Arguments.createArgument("time", Arguments.integerArgument(0))
        ));
        register("setWithWorld", Arrays.asList(
                Arguments.createLiteral("set"),
                Arguments.createArgument("time", Arguments.integerArgument(0)),
                Arguments.createArgument("world", Arguments.worldArgument())
        ));
    }

    @SubCommand("getNoWorld")
    @PlayerOnly
    public void getTimeNoWorld(VLPlayer sender) {
        World world = sender.getWorld();
        sender.sendMessage(VaultLoader.getMessage("time.get").replace("{WORLD}", world.getName()).replace("{TIME}", world.getTime() +
                " (" + world.getFullTime() + ")"));
    }

    @SubCommand("getWithWorld")
    public void getTimeWithWorld(VLCommandSender sender, World world) {
        sender.sendMessage(VaultLoader.getMessage("time.get").replace("{WORLD}", world.getName()).replace("{TIME}", world.getTime() +
                " (" + world.getFullTime() + ")"));
    }

    @SubCommand("setNoWorld")
    @PlayerOnly
    public void setTimeNoWorld(VLPlayer sender, int time) {
        World world = sender.getWorld();
        world.setTime(time);
        sender.sendMessage(VaultLoader.getMessage("time.set").replace("{WORLD}", world.getName()).replace("{TIME}", String.valueOf(time)));
    }

    @SubCommand("setWithWorld")
    public void setTimeWithWorld(VLCommandSender sender, int time, World world) {
        world.setTime(time);
        sender.sendMessage(VaultLoader.getMessage("time.set").replace("{WORLD}", world.getName()).replace("{TIME}", String.valueOf(time)));
    }
}
