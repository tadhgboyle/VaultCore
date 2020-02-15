package net.vaultmc.vaultcore.chat.msg;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
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
    static void pm(VLPlayer player, VLOfflinePlayer target, String message) {
        UUID session = UUID.randomUUID();
        sessions.put(player.getUniqueId(), session);
        sessionsReversed.put(session, player.getUniqueId());
        MsgSocketListener.getWriter().write("TellFromTo" + VaultCore.SEPARATOR + session.toString() + VaultCore.SEPARATOR +
                player.getUniqueId().toString() + VaultCore.SEPARATOR + target.getUniqueId().toString() + VaultCore.SEPARATOR + message);
        MsgSocketListener.getWriter().flush();
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
