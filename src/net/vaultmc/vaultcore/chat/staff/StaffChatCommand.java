package net.vaultmc.vaultcore.chat.staff;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.MessengerUtils;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RootCommand(literal = "staffchat", description = "Use staff chat.")
@Permission(Permissions.StaffChatCommand)
@Aliases("sc")
public class StaffChatCommand extends CommandExecutor {
    public static final Set<UUID> toggled = new HashSet<>();

    public StaffChatCommand() {
        register("chat", Collections.singletonList(Arguments.createArgument("message", Arguments.greedyString())));
        register("toggle", Collections.singletonList(Arguments.createLiteral("toggle")));
    }

    @SubCommand("chat")
    @SneakyThrows
    public static void chat(VLCommandSender sender, String message) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.writeUTF("Send");
        stream.writeUTF(sender.getFormattedName());
        stream.writeUTF(message);
        MessengerUtils.sendForwarding("vaultcore:staff_chat", "ALL", bos.toByteArray());
        stream.close();
    }

    @SneakyThrows
    private static void setToggled(VLPlayer player, boolean status) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        stream.writeUTF("SetAlwaysOn");
        stream.writeUTF(player.getUniqueId().toString());
        stream.writeBoolean(status);
        MessengerUtils.sendForwarding("vaultcore:staff_chat", "ALL", bos.toByteArray());
        stream.close();
    }

    @SubCommand("toggle")
    @PlayerOnly
    public void toggle(VLPlayer player) {
        if (toggled.contains(player.getUniqueId())) {
            setToggled(player, false);
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.toggle"), "off"));
        } else {
            setToggled(player, true);
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.toggle"), "on"));
        }
    }
}