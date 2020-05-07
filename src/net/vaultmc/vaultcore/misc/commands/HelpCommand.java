package net.vaultmc.vaultcore.misc.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;

@RootCommand(literal = "help", description = "Help me!")
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
            helpMain(sender);
            return;
        }
        if (clazz.isAnnotationPresent(Permission.class) && sender instanceof VLPlayer && !((VLPlayer) sender).hasPermission(clazz.getAnnotation(Permission.class).value())) {
            sender.sendMessage(Bukkit.getPermissionMessage());
            return;
        }
        sender.sendMessage(ChatColor.YELLOW + "Help: " + ChatColor.GOLD + "/" + clazz.getAnnotation(RootCommand.class).literal());
        sender.sendMessage(ChatColor.YELLOW + "Description: " + ChatColor.GOLD + clazz.getAnnotation(RootCommand.class).description());
        sender.sendMessage(ChatColor.DARK_GREEN + "-------------------------");
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(SubCommand.class)) continue;
            if (method.isAnnotationPresent(Permission.class) && sender instanceof VLPlayer && !((VLPlayer) sender).hasPermission(method.getAnnotation(Permission.class).value()))
                continue;
            sender.sendMessage(ChatColor.YELLOW + "Sub-Command: " + ChatColor.GOLD + Utilities.capitalizeMessage(method.getAnnotation(SubCommand.class).value()));
            StringBuilder sb = new StringBuilder();
            if (method.getParameters().length > 1) {
                for (Parameter parameter : method.getParameters()) {
                    if (!parameter.getName().equals("sender"))
                        sb.append("<").append(parameter.getName()).append("> ");
                }
            }
            sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.GOLD + "/" + clazz.getAnnotation(RootCommand.class).literal() + " " + sb.toString().trim());
            sender.sendMessage(ChatColor.DARK_GREEN + "-------------------------");
        }
    }
}
