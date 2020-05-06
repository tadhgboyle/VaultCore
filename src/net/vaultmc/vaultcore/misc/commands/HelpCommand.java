package net.vaultmc.vaultcore.misc.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Registry;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.lang.reflect.Method;
import java.util.Collections;

@RootCommand(literal = "vchelp", description = "Help me!")
@Permission(Permissions.HelpCommand)
@PlayerOnly
public class HelpCommand extends CommandExecutor {

    public HelpCommand() {
        register("helpMain", Collections.emptyList());
        register("helpCommand", Collections.singletonList(Arguments.createArgument("command", Arguments.string())));
    }

    @SubCommand("helpMain")
    public void helpMain(VLPlayer sender) {

    }

    @SubCommand("helpCommand")
    public void helpCommand(VLPlayer sender, String command) {
        Class<?> clazz = Registry.getCommandClasses().get(command);
        if (clazz == null) {
            sender.sendMessage("not vaultcore command");
            return;
        }
        sender.sendMessage("class: " + clazz.getSimpleName());
        sender.sendMessage("description: " + clazz.getAnnotation(RootCommand.class).description());
        for (Method method : clazz.getDeclaredMethods()) {
            sender.sendMessage("method: " + method.getName());
        }
    }
}
