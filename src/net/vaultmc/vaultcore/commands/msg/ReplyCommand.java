package net.vaultmc.vaultcore.commands.msg;

import java.util.Collections;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.Aliases;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

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
        if (!MsgCommand.getReplies().containsKey(player.getUniqueId())) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.reply.noone_error"));
            return;
        }
        VLPlayer target = VLPlayer.getPlayer(MsgCommand.getReplies().get(player.getUniqueId()));
        if (!target.getDataConfig().getBoolean("settings.msg")) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.msg.player_disabled_messaging"));
            return;
        }
        if (MsgCommand.getReplies().containsKey(player.getUniqueId())) {
			player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"),
					player.getFormattedName(), target.getFormattedName(), message));
			target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"),
					player.getFormattedName(), target.getFormattedName(), message));
            MsgCommand.getReplies().put(target.getUniqueId(), player.getUniqueId());
        } else {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.reply.noone_error"));
        }
    }
}
