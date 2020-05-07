package net.vaultmc.vaultcore.misc.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;

@RootCommand(literal = "vchelp", description = "Help me!")
@Permission(Permissions.HelpCommand)
public class HelpCommand extends CommandExecutor {

    public HelpCommand() {
        register("helpMain", Collections.emptyList());
        register("helpCommand", Collections.singletonList(Arguments.createArgument("command", Arguments.string())));
    }

    @SubCommand("helpMain")
    public void helpMain(VLCommandSender sender) {
        sender.sendMessage("todo");
    }

    @SubCommand("helpCommand")
    public void helpCommand(VLCommandSender sender, String command) {
        Class<?> clazz = VaultLoader.getCommandClasses().get(command);
        if (clazz == null) {
            sender.sendMessage("not vaultloader command");
            return;
        }
        sender.sendMessage("root command: " + clazz.getAnnotation(RootCommand.class).literal());
        sender.sendMessage("description: " + clazz.getAnnotation(RootCommand.class).description());
        sender.sendMessage("-------------------------");
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(SubCommand.class)) continue;
            sender.sendMessage("subcommand: " + method.getAnnotation(SubCommand.class).value());
            StringBuilder sb = new StringBuilder();
            for (Parameter parameter : method.getParameters()) {
                sb.append(parameter.getType().getClass().getSimpleName()).append(" ");
            }
            sender.sendMessage("usage: /" + clazz.getAnnotation(RootCommand.class).literal() + " " + sb.toString());
            sender.sendMessage("-------------------------");
        }
    }
}
