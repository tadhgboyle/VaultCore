package net.vaultmc.vaultcore.commands.staff;

import java.util.Arrays;
import java.util.Collections;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;

@RootCommand(
        literal = "time",
        description = "Sets the time in your world (or a specified world)"
)
@Permission(Permissions.TimeCommand)
public class TimeCommand extends CommandExecutor {
	
	String string = Utilities.string;
	String variable1 = Utilities.variable1;
	
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
    public void getTimeNoWorld(Player sender) {
        World world = sender.getWorld();
        sender.sendMessage(string + "The time in " + variable1 + world.getName() + string + " is " +
                variable1 + world.getTime() + string + ".");
    }

    @SubCommand("getWithWorld")
    public void getTimeWithWorld(CommandSender sender, World world) {
        sender.sendMessage(string + "The time in " + variable1 + world.getName() + string + " is " +
                variable1 + world.getTime() + string + ".");
    }

    @SubCommand("setNoWorld")
    @PlayerOnly
    public void setTimeNoWorld(Player sender, int time) {
        World world = sender.getWorld();
        world.setTime(time);
        sender.sendMessage(string + "Set the time in " + variable1 + world.getName() + string + " to " +
                variable1 + time + string + ".");
    }

    @SubCommand("setWithWorld")
    public void setTimeWithWorld(CommandSender sender, int time, World world) {
        world.setTime(time);
        sender.sendMessage(string + "Set the time in " + variable1 + world.getName() + string + " to " +
                variable1 + time + string + ".");
    }
}
