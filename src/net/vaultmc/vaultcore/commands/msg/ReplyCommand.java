package net.vaultmc.vaultcore.commands.msg;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.util.Collections;

@RootCommand(literal = "r", description = "Reply to a message.")
@Permission(Permissions.MsgCommand)
@PlayerOnly
@Aliases("reply")
public class ReplyCommand extends CommandExecutor {
    private String string = Utilities.string;
    private String variable1 = Utilities.variable1;

    public ReplyCommand() {
        this.register("r", Collections.singletonList(Arguments.createArgument("message", Arguments.greedyString())));
    }

    @SubCommand("r")
    public void reply(VLPlayer player, String message) {
        if (!MsgCommand.getReplies().containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You have no one to reply to!");
            return;
        }
        VLPlayer target = VLPlayer.getPlayer(MsgCommand.getReplies().get(player.getUniqueId()));
        if (!target.getDataConfig().getBoolean("settings.msg")) {
            player.sendMessage(ChatColor.RED + "That player has disabled messaging!");
            return;
        }
        if (MsgCommand.getReplies().containsKey(player.getUniqueId())) {
            String meTo = player.getFormattedName() + string + " -> " + variable1
                    + target.getFormattedName() + string + ":";
            String toMe = player.getFormattedName() + string + " -> " + variable1
                    + target.getFormattedName() + string + ":";
            player.sendMessage(meTo + " " + ChatColor.RESET + message);
            target.sendMessage(toMe + " " + ChatColor.RESET + message);
            MsgCommand.getReplies().put(target.getUniqueId(), player.getUniqueId());
        } else {
            player.sendMessage(ChatColor.RED + "You have no one to reply to!");
        }
    }
}
