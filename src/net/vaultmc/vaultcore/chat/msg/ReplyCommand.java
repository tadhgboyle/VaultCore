package net.vaultmc.vaultcore.chat.msg;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;

import java.util.Collections;

@RootCommand(literal = "r", description = "Reply to a message.")
@Permission(Permissions.MsgCommand)
@PlayerOnly
@Aliases("reply")
public class ReplyCommand extends CommandExecutor {
    public ReplyCommand() {
        this.register("r", Collections.singletonList(Arguments.createArgument("message", Arguments.greedyString())));
    }

    @SubCommand("r")
    public void reply(VLPlayer player, String message) {
        if (!MsgCommand.getReplies().containsValue(player.getUniqueId())) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.reply.noone_error"));
            return;
        }
        MsgCommand.pm(player, Bukkit.getOfflinePlayer(MsgCommand.getReplies().get(player.getUniqueId())), message);
    }
}
