package net.vaultmc.vaultcore.misc.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    @SubCommand(
            value = "helpCommand",
            advanced = true
    )
    public void helpCommand(CommandContext<CommandSourceStack> context, String command) {
        VLCommandSender sender = VLCommandSender.getCommandSender(context.getSource().getBukkitSender());

        Class<?> clazz = VaultLoader.getCommandClasses().get(command);
        if (clazz == null) {
            helpMain(sender);
            return;
        }
        if (clazz.isAnnotationPresent(Permission.class) && sender instanceof VLPlayer && !((VLPlayer) sender).hasPermission(clazz.getAnnotation(Permission.class).value())) {
            sender.sendMessage(Bukkit.getPermissionMessage());
            return;
        }
        sender.sendMessage(ChatColor.DARK_GREEN + "--== [Help] ==--");
        sender.sendMessage(ChatColor.YELLOW + "Command: " + ChatColor.GOLD + "/" + clazz.getAnnotation(RootCommand.class).literal());
        sender.sendMessage(ChatColor.YELLOW + "Description: " + ChatColor.GOLD + clazz.getAnnotation(RootCommand.class).description());
        sender.sendMessage(ChatColor.DARK_GREEN + "-------------------------");

        sender.sendMessage(ChatColor.YELLOW + "Usage:");

        Set<String> results = new HashSet<>();

        for (CommandNode<CommandSourceStack> node : CommandExecutor.getCommands().stream().filter(c -> c.getClass() == clazz).collect(Collectors.toList()).get(0).getRegisteredNodes()) {
            String usage = node.getUsageText();
            if (usage.contains(":")) {
                continue;
            }
            if (!usage.equalsIgnoreCase(command)) {
                continue;
            }
            for (String u : CommandExecutor.getCommands_().getDispatcher().getAllUsage(node, context.getSource(), true)) {
                results.add((ChatColor.GOLD + "/" + usage + " " + u).trim());
            }
        }

        for (String r : results) {
            sender.sendMessage(r);
        }

        sender.sendMessage(ChatColor.DARK_GREEN + "-------------------------");
    }
}
