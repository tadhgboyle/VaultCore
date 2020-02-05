package net.vaultmc.vaultcore.msg;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

@RootCommand(literal = "msg", description = "Send a player a message.")
@Permission(Permissions.MsgCommand)
@PlayerOnly
@Aliases({"tell", "whisper", "w", "pm", "privatemessage"})
public class MsgCommand extends CommandExecutor {
    @Getter
    private static HashMap<UUID, UUID> replies = new HashMap<>();

    public MsgCommand() {
        unregisterExisting();
        this.register("msg", Arrays.asList(Arguments.createArgument("target", Arguments.playerArgument()),
                Arguments.createArgument("message", Arguments.greedyString())));
    }

    @SubCommand("msg")
    public void msg(VLPlayer player, VLPlayer target, String message) {
        if (target == null) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.player_offline"));
        }
        if (target == player) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.msg.self_error"));
            return;
        }
        if (!target.getDataConfig().getBoolean("settings.msg")) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.msg.player_disabled_messaging"));
        } else {
            player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"),
                    player.getFormattedName(), target.getFormattedName(), message));
            target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"),
                    player.getFormattedName(), target.getFormattedName(), message));
            replies.put(player.getUniqueId(), target.getUniqueId());

            for (VLPlayer socialspy : SocialSpyCommand.toggled) {
                if (!socialspy.getFormattedName().equals(player.getFormattedName())
                        && !socialspy.getFormattedName().equals(target.getFormattedName())) {
                    socialspy.sendMessage(VaultLoader.getMessage("vaultcore.commands.socialspy.prefix")
                            + Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"),
                            player.getFormattedName(), target.getFormattedName(), message));
                }
            }

        }
    }
}