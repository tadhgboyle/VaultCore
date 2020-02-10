package net.vaultmc.vaultcore.chat.staff;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;

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
        ByteArrayOutputStream extern = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(extern);
        dos.writeUTF("Forward");
        dos.writeUTF("ALL");

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(os);
        stream.writeUTF("Send");
        stream.writeUTF(sender.getFormattedName());
        stream.writeUTF(message);
        byte[] bytes = os.toByteArray();
        dos.writeShort(bytes.length);
        dos.write(bytes);
        stream.close();

        Bukkit.getWorlds().get(0).sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", extern.toByteArray());
        dos.close();
    }

    @SubCommand("toggle")
    @PlayerOnly
    @SneakyThrows
    public void toggle(VLPlayer player) {
        ByteArrayOutputStream extern = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(extern);
        dos.writeUTF("Forward");
        dos.writeUTF("ALL");

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(os);
        stream.writeUTF("SetAlwaysOn");
        stream.writeUTF(player.getUniqueId().toString());
        if (toggled.contains(player.getUniqueId())) {
            stream.writeBoolean(false);
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.toggle"), "off"));
        } else {
            stream.writeBoolean(true);
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.staffchat.toggle"), "on"));
        }
        byte[] bytes = os.toByteArray();
        dos.writeShort(bytes.length);
        dos.write(bytes);
        stream.close();
        player.getPlayer().sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", extern.toByteArray());
    }
}