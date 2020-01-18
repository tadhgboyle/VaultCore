package net.vaultmc.vaultcore.commands.msg;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.commands.Aliases;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

@RootCommand(literal = "msg", description = "Send a player a message.")
@Permission(Permissions.MsgCommand)
@PlayerOnly
@Aliases({"tell", "whisper", "w", "pm", "privatemessage"})
public class MsgCommand extends CommandExecutor {
    @Getter
    private static HashMap<UUID, UUID> replies = new HashMap<>();
    private String string = Utilities.string;

    public MsgCommand() {
        unregisterExisting();
        this.register("msg", Arrays.asList(Arguments.createArgument("target", Arguments.playerArgument()),
                Arguments.createArgument("message", Arguments.greedyString())));
    }

    @SubCommand("msg")
    public void msg(VLPlayer player, VLPlayer target, String message) {
        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player is offline!");
        }
        if (target == player) {
            player.sendMessage(ChatColor.RED + "You can't message yourself!");
            return;
        }
        if (!target.getDataConfig().getBoolean("settings.msg")) {
            player.sendMessage(ChatColor.RED + "That player has disabled messaging!");
        } else {
            String msgPrefix = player.getFormattedName() + string + " -> " + target.getFormattedName()
                    + string + ":";

            player.sendMessage(msgPrefix + " " + ChatColor.RESET + message);
            target.sendMessage(msgPrefix + " " + ChatColor.RESET + message);

            replies.put(target.getUniqueId(), player.getUniqueId());
        }
    }
}
