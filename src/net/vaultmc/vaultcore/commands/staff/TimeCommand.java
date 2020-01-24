/*
 * VaultUtils: VaultMC functionalities provider.
 * Copyright (C) 2020 yangyang200
 *
 * VaultUtils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VaultUtils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VaultCore.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.vaultmc.vaultcore.commands.staff;

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
