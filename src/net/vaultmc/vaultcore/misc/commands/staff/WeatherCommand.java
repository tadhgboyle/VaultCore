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
