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
import org.bukkit.command.Command;

import java.util.*;
import java.util.stream.Collectors;

@RootCommand(literal = "help", description = "Help me!")
@Permission(Permissions.HelpCommand)
public class HelpCommand extends CommandExecutor {

    private static final List<Command> commandCache = new ArrayList<>(Bukkit.getCommandMap().getKnownCommands().values());
    private static final int PAGE_SIZE = 7;

    public HelpCommand() {
        register("helpMain", Collections.emptyList());
        register("helpPage", Arrays.asList(Arguments.createLiteral("page"), Arguments.createArgument("page", Arguments.integerArgument(1))));
        register("helpCommand", Collections.singletonList(Arguments.createArgument("command", Arguments.string())));
    }

    public static void paginateCommands(VLCommandSender sender, int page) {
        int fromIndex = (page - 1) * PAGE_SIZE;
        if (commandCache == null || commandCache.size() < fromIndex) {
            sender.sendMessage("errorrrrr");
            return;
        }
        sender.sendMessage("page: " + page);
        for (Command command : commandCache.subList(fromIndex, Math.min(fromIndex + PAGE_SIZE, commandCache.size()))) {
            sender.sendMessage(command.getLabel());
        }
    }

    @SubCommand("helpMain")
    public void helpMain(VLCommandSender sender) {
        paginateCommands(sender, 1);
    }

    @SubCommand("helpPage")
    public void helpPage(VLCommandSender sender, int page) {
        paginateCommands(sender, page);
    }

    @SubCommand(
            value = "helpCommand",
            advanced = true
    )
    public void helpCommand(CommandContext<CommandSourceStack> context, String command) {
        VLCommandSender sender = VLCommandSender.getCommandSender(context.getSource().getBukkitSender());

        Class<?> clazz = VaultLoader.getCommandClasses().get(command);
        if (clazz == null) {
            // Regular command, not VL. Take from bukkit command map
            Command bukkitCommand = Bukkit.getCommandMap().getCommand(command);
            if (bukkitCommand == null) {
                sender.sendMessage("invalid command");
                return;
            }
            sender.sendMessage("command: /" + bukkitCommand.getLabel());
            sender.sendMessage("desc: " + bukkitCommand.getDescription());
            String usage = bukkitCommand.getUsage();
            if (!usage.isEmpty()) sender.sendMessage("usage: " + usage.replace("<command>", bukkitCommand.getLabel()));
            return;
        }
        // VL command -> We can provide more information
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
