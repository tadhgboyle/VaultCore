package net.vaultmc.vaultcore.chat.msg;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RootCommand(literal = "msg", description = "Send a player a message.")
@Permission(Permissions.MsgCommand)
@PlayerOnly
@Aliases({"tell", "whisper", "w", "pm", "privatemessage"})
public class MsgCommand extends CommandExecutor implements Listener {
    @Getter
    private static Map<UUID, UUID> replies = new HashMap<>();
    @Getter(AccessLevel.PACKAGE)
    private static Map<UUID, UUID> sessions = new HashMap<>();
    @Getter(AccessLevel.PACKAGE)
    private static Map<UUID, UUID> sessionsReversed = new HashMap<>();

    public MsgCommand() {
        unregisterExisting();
        this.register("msg", Arrays.asList(
                Arguments.createArgument("target", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("message", Arguments.greedyString())));
        VaultCore.getInstance().registerEvents(this);
    }

    @SneakyThrows
    static void pm(VLPlayer from, VLOfflinePlayer to, String message) {
        from.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"),
                from.getFormattedName(), to.getFormattedName(), message));
        to.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"),
                from.getFormattedName(), to.getFormattedName(), message));
        MsgCommand.getReplies().put(from.getUniqueId(), to.getUniqueId());

        for (VLPlayer socialspy : SocialSpyCommand.toggled) {
            if (!socialspy.getFormattedName().equals(from.getFormattedName())
                    && !socialspy.getFormattedName().equals(to.getFormattedName())) {
                socialspy.sendMessage(VaultLoader.getMessage("vaultcore.commands.socialspy.prefix")
                        + Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"),
                        from.getFormattedName(), to.getFormattedName(), message));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        sessionsReversed.remove(sessions.get(e.getPlayer().getUniqueId()));
        sessions.remove(e.getPlayer().getUniqueId());
    }

    @SubCommand("msg")
    public void msg(VLPlayer player, VLOfflinePlayer target, String message) {
        pm(player, target, message);
    }
}
