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
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
    private static final Map<UUID, UUID> fromTo = new HashMap<>();
    private static final Map<String, String> idServer = new HashMap<>();
    private static final Map<String, UUID> senderMap = new HashMap<>();

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
        VaultCore.getInstance().registerEvents(this);
    }

    @SneakyThrows
    public static void teleport(VLOfflinePlayer sender, VLOfflinePlayer target, Consumer<Boolean> callback) {
        GetServerService.getServer(sender, new GeneralCallback<String>()
                .success(server -> GetServerService.getServer(target, new GeneralCallback<String>()
                        .success(x -> {
                            if (x.trim().equalsIgnoreCase(server.trim())) {
                                VaultCore.getInstance().getLogger().info("Target on the same server. Teleporting.");
                                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> sender.getOnlinePlayer().getPlayer().teleport(target.getOnlinePlayer().getPlayer()));
                            } else {
                                String session = UUID.randomUUID().toString();
                                SQLMessenger.sendGlobalMessage("TeleportPresetup" + VaultCore.SEPARATOR + session +
                                        VaultCore.SEPARATOR + x +
                                        VaultCore.SEPARATOR + sender.getUniqueId().toString() +
                                        VaultCore.SEPARATOR + target.getUniqueId().toString());
                                idServer.put(session, x);
                                senderMap.put(session, sender.getUniqueId());
                            }
                            callback.accept(true);
                        })
                        .failure(x -> callback.accept(false))))
                .failure(server -> callback.accept(false)));
    }

    @EventHandler
    @SneakyThrows
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().startsWith("TeleportPresetup")) {
            String[] parts = e.getMessage().split(VaultCore.SEPARATOR);
            if (parts[2].equalsIgnoreCase(VaultCore.getInstance().getConfig().getString("server"))) {
                fromTo.put(UUID.fromString(parts[3]), UUID.fromString(parts[4]));
                System.out.println("fromTo = " + fromTo);
                SQLMessenger.sendGlobalMessage("TeleportSetupFinished" + VaultCore.SEPARATOR + parts[1]);
            }
        } else if (e.getMessage().startsWith("TeleportSetupFinished")) {
            String[] parts = e.getMessage().split(VaultCore.SEPARATOR);
            if (idServer.containsKey(parts[1])) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream stream = new DataOutputStream(bos);
                stream.writeUTF("Connect");
                stream.writeUTF(idServer.get(parts[1]));
                Bukkit.getPlayer(senderMap.get(parts[1])).sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", bos.toByteArray());
                stream.close();
                idServer.remove(parts[1]);
                senderMap.remove(parts[1]);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        System.out.println("fromTo = " + fromTo);
        if (fromTo.containsKey(e.getPlayer().getUniqueId())) {
            System.out.println("Bukkit.getPlayer(fromTo.get(e.getPlayer().getUniqueId())) = " + Bukkit.getPlayer(fromTo.get(e.getPlayer().getUniqueId())));
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> {
                e.getPlayer().teleport(Bukkit.getPlayer(fromTo.get(e.getPlayer().getUniqueId())));
                fromTo.remove(e.getPlayer().getUniqueId());
            });
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
    public void teleportPlayerTo(VLCommandSender sender, VLOfflinePlayer target, Location location) {
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
