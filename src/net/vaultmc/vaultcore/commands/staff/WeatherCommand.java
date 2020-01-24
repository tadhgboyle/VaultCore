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
        literal = "weather",
        description = "Checks and sets the weather in your current world or another world."
)
@Permission(Permissions.WeatherCommand)
public class WeatherCommand extends CommandExecutor {
    public WeatherCommand() {
        unregisterExisting();
        register("clearThis", Collections.singletonList(Arguments.createLiteral("clear")));
        register("rainThis", Collections.singletonList(Arguments.createLiteral("rain")));
        register("thunderThis", Collections.singletonList(Arguments.createLiteral("thunder")));

        register("clearOther", Arrays.asList(
                Arguments.createLiteral("clear"),
                Arguments.createArgument("world", Arguments.worldArgument())
        ));
        register("rainOther", Arrays.asList(
                Arguments.createLiteral("rain"),
                Arguments.createArgument("world", Arguments.worldArgument())
        ));
        register("thunderOther", Arrays.asList(
                Arguments.createLiteral("thunder"),
                Arguments.createArgument("world", Arguments.worldArgument())
        ));

        register("setDuration", Arrays.asList(
                Arguments.createLiteral("duration"),
                Arguments.createArgument("duration", Arguments.integerArgument(0))
        ));
        register("setDurationOther", Arrays.asList(
                Arguments.createLiteral("duration"),
                Arguments.createArgument("duration", Arguments.integerArgument(0)),
                Arguments.createArgument("world", Arguments.worldArgument())
        ));
    }

    @SubCommand("clearThis")
    @PlayerOnly
    public void weatherClearThis(VLPlayer sender) {
        sender.getWorld().setStorm(false);
        sender.getWorld().setThundering(false);
        sender.sendMessage(VaultLoader.getMessage("weather.set").replace("{WORLD}", sender.getWorld().getName())
                .replace("{WEATHER}", VaultLoader.getMessage("weather.clear")));
    }

    @SubCommand("rainThis")
    @PlayerOnly
    public void weatherRainThis(VLPlayer sender) {
        sender.getWorld().setStorm(true);
        sender.getWorld().setThundering(false);
        sender.sendMessage(VaultLoader.getMessage("weather.set").replace("{WORLD}", sender.getWorld().getName())
                .replace("{WEATHER}", VaultLoader.getMessage("weather.rain")));
    }

    @SubCommand("thunderThis")
    @PlayerOnly
    public void weatherThunderThis(VLPlayer sender) {
        sender.getWorld().setStorm(true);
        sender.getWorld().setThundering(true);
        sender.sendMessage(VaultLoader.getMessage("weather.set").replace("{WORLD}", sender.getWorld().getName())
                .replace("{WEATHER}", VaultLoader.getMessage("weather.thunder")));
    }

    @SubCommand("clearOther")
    public void weatherClearOther(VLCommandSender sender, World world) {
        world.setStorm(false);
        world.setThundering(false);
        sender.sendMessage(VaultLoader.getMessage("weather.set").replace("{WORLD}", world.getName())
                .replace("{WEATHER}", VaultLoader.getMessage("weather.clear")));
    }

    @SubCommand("rainOther")
    public void weatherRainOther(VLCommandSender sender, World world) {
        world.setStorm(true);
        world.setThundering(false);
        sender.sendMessage(VaultLoader.getMessage("weather.set").replace("{WORLD}", world.getName())
                .replace("{WEATHER}", VaultLoader.getMessage("weather.rain")));
    }

    @SubCommand("thunderOther")
    public void weatherThunderOther(VLCommandSender sender, World world) {
        world.setStorm(true);
        world.setThundering(true);
        sender.sendMessage(VaultLoader.getMessage("weather.set").replace("{WORLD}", world.getName())
                .replace("{WEATHER}", VaultLoader.getMessage("weather.thunder")));
    }

    @SubCommand("setDuration")
    @PlayerOnly
    public void setDuration(VLPlayer sender, int duration) {
        sender.getWorld().setWeatherDuration(duration);
        if (sender.getWorld().isThundering())
            sender.getWorld().setThunderDuration(duration);
        sender.sendMessage(VaultLoader.getMessage("weather.set-duration").replace("{WORLD}", sender.getWorld().getName())
                .replace("{DURATION}", String.valueOf(duration)));
    }

    @SubCommand("setDurationOther")
    public void setDurationOther(VLCommandSender sender, int duration, World world) {
        world.setWeatherDuration(duration);
        if (world.isThundering())
            world.setThunderDuration(duration);
        sender.sendMessage(VaultLoader.getMessage("weather.set-duration").replace("{WORLD}", world.getName())
                .replace("{DURATION}", String.valueOf(duration)));
    }
}
