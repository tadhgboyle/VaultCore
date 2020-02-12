package net.vaultmc.vaultcore.chat.msg;

import lombok.Getter;
import lombok.SneakyThrows;
import net.vaultmc.vaultcore.MessengerUtils;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
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

    @SneakyThrows
    public static void pm(VLPlayer player, VLPlayer target, String message) {
        if (!target.getDataConfig().getBoolean("settings.msg")) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.msg.player_disabled_messaging"));
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream stream = new DataOutputStream(bos);
            stream.writeUTF("TellFromTo");
            UUID session = UUID.randomUUID();
            MsgPMListener.getMsgSessions().put(player.getUniqueId(), session);
            stream.writeUTF(session.toString());
            stream.writeUTF(player.getUniqueId().toString());
            stream.writeUTF(target.getUniqueId().toString());
            stream.writeUTF(message);
            MessengerUtils.sendForwarding("vaultcore:tell", "ALL", bos.toByteArray());
            stream.close();
        }
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
        pm(player, target, message);
    }
}
