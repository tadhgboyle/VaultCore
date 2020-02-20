package net.vaultmc.vaultcore.teleport;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.messenger.GeneralCallback;
import net.vaultmc.vaultcore.messenger.GetServerService;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.messenger.MessageReceivedEvent;
import net.vaultmc.vaultloader.utils.messenger.SQLMessenger;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Consumer;

@RootCommand(
        literal = "teleport",
        description = "Teleport to a player."
)
@Permission(Permissions.TeleportCommand)
@Aliases("tp")
public class TPCommand extends CommandExecutor implements Listener {
    public TPCommand() {
        unregisterExisting();
        register("teleportToPlayer",
                Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
        register("teleportPlayerToPlayer",
                Arrays.asList(Arguments.createArgument("target", Arguments.playerArgument()),
                        Arguments.createArgument("to", Arguments.playerArgument())));
        register("teleportToLocation",
                Collections.singletonList(Arguments.createArgument("location", Arguments.location3DArgument())));
        register("teleportPlayerTo", Arrays.asList(
                Arguments.createArgument("target", Arguments.playerArgument()),
                Arguments.createArgument("location", Arguments.location3DArgument())
        ));
        VaultCore.getInstance().registerEvents(this);
    }

    @SneakyThrows
    public static void teleport(VLOfflinePlayer sender, VLOfflinePlayer target, Consumer<Boolean> callback) {
        GetServerService.getServer(sender, new GeneralCallback<String>()
                .success(server -> GetServerService.getServer(target, new GeneralCallback<String>()
                        .success(x -> {
                            try {
                                if (x.trim().equalsIgnoreCase(server.trim())) {
                                    VaultCore.getInstance().getLogger().info("Target on the same server. Teleporting.");
                                    Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> sender.getOnlinePlayer().getPlayer().teleport(target.getOnlinePlayer().getPlayer()));
                                } else {
                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                    DataOutputStream stream = new DataOutputStream(bos);
                                    stream.writeUTF("Connect");
                                    stream.writeUTF(x);
                                    sender.getOnlinePlayer().getPlayer().sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", bos.toByteArray());
                                    stream.close();
                                    SQLMessenger.sendGlobalMessage("Teleport" + VaultCore.SEPARATOR + sender.getUniqueId().toString() +
                                            VaultCore.SEPARATOR + target.getUniqueId().toString());
                                }
                                callback.accept(true);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        })
                        .failure(x -> callback.accept(false))))
                .failure(server -> callback.accept(false)));
    }

    @EventHandler
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().startsWith("Teleport")) {
            Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
                String[] parts = e.getMessage().split(VaultCore.SEPARATOR);
                VLPlayer from = VLPlayer.getPlayer(UUID.fromString(parts[1]));
                VLPlayer to = VLPlayer.getPlayer(UUID.fromString(parts[2]));
                if (from == null || to == null) return;
                from.teleport(to);
            }, 5);
        }
    }

    @SubCommand("teleportToLocation")
    @PlayerOnly
    public void teleportToLocation(VLPlayer sender, Location location) {
        sender.teleport(location);
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.teleport.player_to_location"),
                location.getX(), location.getY(), location.getZ()));
    }

    @SubCommand("teleportPlayerTo")
    public void teleportPlayerTo(VLCommandSender sender, VLPlayer target, Location location) {
        target.teleport(location);
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.teleport.player_to_location_sender"),
                target.getFormattedName(), location.getX(), location.getY(), location.getZ()));
        target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.teleport.player_to_location_target"),
                sender.getFormattedName(), location.getX(), location.getY(), location.getZ()));
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

    @SubCommand("teleportPlayerToPlayer")
    @SneakyThrows
    public void teleportPlayerToPlayer(VLCommandSender sender, VLOfflinePlayer target, VLOfflinePlayer to) {
        if (target == sender || to == sender) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.target_error"));
            return;
        }
        teleport(target, to, status -> {
            if (status) {
                SQLMessenger.sendGlobalMessage("312Message" + VaultCore.SEPARATOR + target.getUniqueId().toString() +
                        VaultCore.SEPARATOR + VaultLoader.getMessage("vaultcore.commands.teleport.player_to_player_target")
                        .replace("{SENDER}", sender.getFormattedName())
                        .replace("{TARGET}", to.getFormattedName()));
                SQLMessenger.sendGlobalMessage("312Message" + VaultCore.SEPARATOR + to.getUniqueId().toString() +
                        VaultCore.SEPARATOR + VaultLoader.getMessage("vaultcore.commands.teleport.player_to_player_receiver")
                        .replace("{SENDER}", sender.getFormattedName())
                        .replace("{TARGET}", target.getFormattedName()));
            } else {
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.failed"));
            }
        });
    }
}
