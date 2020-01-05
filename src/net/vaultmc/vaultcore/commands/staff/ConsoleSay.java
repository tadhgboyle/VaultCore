package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.commands.wrappers.WrappedChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(
        literal = "say",
        description = "Used by the console to send messages."
)
@Aliases("chat")
public class ConsoleSay extends CommandExecutor {
    public ConsoleSay() {
        register("say", Collections.singletonList(Arguments.createArgument("message", Arguments.messageArgument())));
    }

    @SubCommand("say")
    public void say(CommandSender sender, WrappedChatComponent message) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + "Only console can use this command!");
            return;
        }
        String csay = String.format(ChatColor.BLUE + "" + ChatColor.BOLD + "" + "CONSOLE" + ChatColor.DARK_GRAY
                + ": " + ChatColor.WHITE + "%s", message.toString());
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(csay);
        }
    }
}