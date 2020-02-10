package net.vaultmc.vaultcore.chat.msg;

import lombok.Getter;
import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.commands.arguments.custom.OfflinePlayerArgument;
import net.vaultmc.vaultloader.utils.commands.wrappers.WrappedSuggestion;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RootCommand(literal = "msg", description = "Send a player a message.")
@Permission(Permissions.MsgCommand)
@PlayerOnly
@Aliases({"tell", "whisper", "w", "pm", "privatemessage"})
public class MsgCommand extends CommandExecutor {
    @Getter
    private static HashMap<UUID, UUID> replies = new HashMap<>();

    public MsgCommand() {
        unregisterExisting();
        this.register("msg", Arrays.asList(
                Arguments.createArgument("target", Arguments.word()),
                Arguments.createArgument("message", Arguments.greedyString())));
    }

    @TabCompleter(
            subCommand = "msg",
            argument = "target"
    )
    public List<WrappedSuggestion> suggestPlayers(VLPlayer sender, String remaining) {
        return VLPlayer.getOnlinePlayers().stream().map(p -> new WrappedSuggestion(p.getName())).collect(Collectors.toList());
    }

    @SneakyThrows
    static void pm(VLPlayer sender, OfflinePlayer target, String message) {
        String uuid = target.getUniqueId().toString();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.writeUTF("Forward");
        stream.writeUTF("ALL");
        stream.writeUTF("vaultcore:tell");

        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(dataStream);
        os.writeUTF("TellFromTo");
        UUID id = UUID.randomUUID();
        MsgPMListener.getMsgSessions().put(sender.getUniqueId(), id);
        os.writeUTF(id.toString());
        os.writeUTF(sender.getUniqueId().toString());
        os.writeUTF(uuid);
        os.writeUTF(message);

        byte[] msg = dataStream.toByteArray();
        stream.writeShort(msg.length);
        stream.write(msg);
        os.close();

        sender.getPlayer().sendPluginMessage(VaultLoader.getInstance(), "vaultcore:tell", bos.toByteArray());
        stream.close();
    }

    @SubCommand("msg")
    @SneakyThrows
    public void msg(VLPlayer sender, String target, String message) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(target);
        if (player == null) {
            sender.sendMessage(OfflinePlayerArgument.NO_PLAYERS_FOUND.create().getMessage());
            return;
        }
        pm(sender, player, message);
    }
}
