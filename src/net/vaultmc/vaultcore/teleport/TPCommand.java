package net.vaultmc.vaultcore.teleport;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.messenger.MessageReceivedEvent;
import net.vaultmc.vaultloader.utils.messenger.SQLMessenger;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.*;
import java.util.function.Consumer;

@RootCommand(
        literal = "teleport",
        description = "Teleport to a player."
)
@Permission(Permissions.TeleportCommand)
@Aliases("tp")
public class TPCommand extends CommandExecutor implements Listener {
    private static final Map<String, Consumer<Boolean>> callbacks = new HashMap<>();

    public TPCommand() {
        unregisterExisting();
        register("teleportToPlayer",
                Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
        register("teleportToLocation",
                Collections.singletonList(Arguments.createArgument("location", Arguments.location3DArgument())));
        register("teleportPlayerTo", Arrays.asList(
                Arguments.createArgument("target", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("location", Arguments.location3DArgument())
        ));
        register("teleportToServer", Arrays.asList(
                Arguments.createLiteral("server"),
                Arguments.createArgument("server", Arguments.word())
        ));
        VaultCore.getInstance().registerEvents(this);
    }

    @SubCommand("teleportToServer")
    @SneakyThrows
    public void teleportToServer(VLPlayer sender, String server) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeUTF("Connect");
        dos.writeUTF(server);
        sender.getPlayer().sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", bos.toByteArray());
        dos.close();
    }

    @SneakyThrows
    public static void teleport(VLOfflinePlayer sender, VLOfflinePlayer target, Consumer<Boolean> callback) {
        String session = UUID.randomUUID().toString();
        callbacks.put(session, callback);
        SQLMessenger.sendGlobalMessage("Teleport" + VaultCore.SEPARATOR + session + VaultCore.SEPARATOR + sender.getUniqueId() +
                VaultCore.SEPARATOR + target.getUniqueId());
    }

    @EventHandler
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().startsWith("TeleportStatus")) {
            String[] parts = e.getMessage().split(VaultCore.SEPARATOR);
            if (callbacks.containsKey(parts[1])) {
                callbacks.remove(parts[1]).accept(parts[2].equalsIgnoreCase("success"));
            }
        } else if (e.getMessage().startsWith("TeleportPleaseHelp")) {
            String[] parts = e.getMessage().split(VaultCore.SEPARATOR);
            VLPlayer from = VLPlayer.getPlayer(UUID.fromString(parts[1]));
            VLPlayer to = VLPlayer.getPlayer(UUID.fromString(parts[2]));
            if (from != null && to != null) {
                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> from.teleport(to));
            }
        }
    }

    @SubCommand("teleportToLocation")
    @PlayerOnly
    public void teleportToLocation(VLPlayer sender, Location location) {
        if (location.getY() > 1000) return;
        sender.teleport(location);
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.teleport.player_to_location"),
                location.getX(), location.getY(), location.getZ()));
    }

    @SubCommand("teleportPlayerTo")
    public void teleportPlayerTo(VLCommandSender sender, VLOfflinePlayer target, Location location) {
        if (location.getY() > 1000) return;
        if (target.isOnline()) {
            target.getOnlinePlayer().teleport(location);
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.teleport.player_to_location_sender"),
                    target.getFormattedName(), location.getX(), location.getY(), location.getZ()));
            target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.teleport.player_to_location_target"),
                    sender.getFormattedName(), location.getX(), location.getY(), location.getZ()));
        } else {
            sender.sendMessage(ChatColor.RED + "No player was found");
        }
    }

    @SubCommand("teleportToPlayer")
    @PlayerOnly
    @SneakyThrows
    public void teleportToPlayer(VLPlayer sender, VLOfflinePlayer target) {
        if (sender == target) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.self_error"));
            return;
        }
        teleport(sender, target, status -> {
            if (status) {
                SQLMessenger.sendGlobalMessage("312Message" + VaultCore.SEPARATOR + sender.getUniqueId().toString() +
                        VaultCore.SEPARATOR + VaultLoader.getMessage("vaultcore.commands.teleport.sender_to_player").replace("{TARGET}", target.getFormattedName()));
            } else {
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.failed"));
            }
        });
    }
}
