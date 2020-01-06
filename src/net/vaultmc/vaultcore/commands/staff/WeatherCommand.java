package net.vaultmc.vaultcore.commands.staff;

import java.util.Arrays;
import java.util.Collections;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;

@RootCommand(
        literal = "weather",
        description = "Checks and sets the weather in your current world or another world."
)
@Permission(Permissions.WeatherCommand)
public class WeatherCommand extends CommandExecutor {
	
	String string = Utilities.string;
	String variable1 = Utilities.variable1;
	
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
    public void weatherClearThis(Player sender) {
        sender.getWorld().setStorm(false);
        sender.getWorld().setThundering(false);
        sender.sendMessage(string + "Set weather in " + variable1 + sender.getWorld().getName() +
                string + " to " + variable1 + "clear" + string + ".");
    }

    @SubCommand("rainThis")
    @PlayerOnly
    public void weatherRainThis(Player sender) {
        sender.getWorld().setStorm(true);
        sender.getWorld().setThundering(false);
        sender.sendMessage(string + "Set weather in " + variable1 + sender.getWorld().getName() +
                string + " to " + variable1 + "rain" + string + ".");
    }

    @SubCommand("thunderThis")
    @PlayerOnly
    public void weatherThunderThis(Player sender) {
        sender.getWorld().setStorm(true);
        sender.getWorld().setThundering(true);
        sender.sendMessage(string + "Set weather in " + variable1 + sender.getWorld().getName() +
                string + " to " + variable1 + "thunder" + string + ".");
    }

    @SubCommand("clearOther")
    public void weatherClearOther(CommandSender sender, World world) {
        world.setStorm(false);
        world.setThundering(false);
        sender.sendMessage(string + "Set weather in " + variable1 + world.getName() + string +
                " to " + variable1 + "clear" + string + ".");
    }

    @SubCommand("rainOther")
    public void weatherRainOther(CommandSender sender, World world) {
        world.setStorm(true);
        world.setThundering(false);
        sender.sendMessage(string + "Set weather in " + variable1 + world.getName() + string +
                " to " + variable1 + "rain" + string + ".");
    }

    @SubCommand("thunderOther")
    public void weatherThunderOther(CommandSender sender, World world) {
        world.setStorm(true);
        world.setThundering(true);
        sender.sendMessage(string + "Set weather in " + variable1 + world.getName() + string +
                " to " + variable1 + "thunder" + string + ".");
    }

    @SubCommand("setDuration")
    @PlayerOnly
    public void setDuration(Player sender, int duration) {
        sender.getWorld().setWeatherDuration(duration);
        if (sender.getWorld().isThundering())
            sender.getWorld().setThunderDuration(duration);
        sender.sendMessage(string + "Set weather duration in " + variable1 + sender.getWorld().getName() +
                string + " to " + variable1 + duration + string + ".");
    }

    @SubCommand("setDurationOther")
    public void setDurationOther(Player sender, int duration, World world) {
        world.setWeatherDuration(duration);
        if (world.isThundering())
            world.setThunderDuration(duration);
        sender.sendMessage(string + "Set weather duration in " + variable1 + world.getName() +
                string + " to " + variable1 + duration + string + ".");
    }
}
