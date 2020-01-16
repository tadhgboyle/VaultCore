package net.vaultmc.vaultcore.commands.staff;

import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.commands.wrappers.WrappedChatComponent;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

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
    public void say(VLCommandSender sender, WrappedChatComponent message) {
        if (sender instanceof VLPlayer) {
            sender.sendMessage(ChatColor.RED + "The specified command can only be executed by the console.");
            return;
        }
        String csay = String.format(ChatColor.BLUE + "" + ChatColor.BOLD + "" + "CONSOLE" + ChatColor.DARK_GRAY
                + ": " + ChatColor.WHITE + "%s", message.toString());
        for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
            player.sendMessage(csay);
        }
    }
}