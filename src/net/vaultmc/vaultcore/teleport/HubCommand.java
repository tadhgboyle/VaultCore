package net.vaultmc.vaultcore.teleport;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.messenger.GeneralCallback;
import net.vaultmc.vaultcore.messenger.GetServerService;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.messenger.MessageReceivedEvent;
import net.vaultmc.vaultloader.utils.messenger.SQLMessenger;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@RootCommand(
        literal = "hub",
        description = "Go back to the VaultMC server and the hub."
)
@Aliases({"spawn", "lobby", "l"})
@PlayerOnly
public class HubCommand extends CommandExecutor implements Listener {
    public HubCommand() {
        register("hub", Collections.emptyList());
        VaultCore.getInstance().registerEvents(this);
    }

    @SubCommand("hub")
    @SneakyThrows
    public void hub(VLPlayer sender) {
        if (VaultCore.getInstance().getConfig().getString("server").equalsIgnoreCase("vaultmc")) {
            sender.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
            return;
        }

        GetServerService.getServer(sender, new GeneralCallback<String>()
                .success(server -> {
                    try {
                        if (!server.equalsIgnoreCase("vaultmc")) {
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            DataOutputStream stream = new DataOutputStream(bos);
                            stream.writeUTF("Connect");
                            stream.writeUTF("vaultmc");
                            sender.getPlayer().sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", bos.toByteArray());
                            stream.close();
                            for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                                player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "(" + sender.getName() + " has left to VaultMC)");
                            }
                        }
                        SQLMessenger.sendGlobalMessage("SendToHub" + VaultCore.SEPARATOR + sender.getUniqueId().toString());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                })
                .failure(server -> {
                    try {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        DataOutputStream stream = new DataOutputStream(bos);
                        stream.writeUTF("Connect");
                        stream.writeUTF("vaultmc");
                        sender.getPlayer().sendPluginMessage(VaultLoader.getInstance(), "BungeeCord", bos.toByteArray());
                        stream.close();
                        for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                            player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "(" + sender.getName() + " has left to VaultMC)");
                        }
                        SQLMessenger.sendGlobalMessage("SendToHub" + VaultCore.SEPARATOR + sender.getUniqueId().toString());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }));
    }

    @EventHandler
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().startsWith("SendToHub")) {
            VLPlayer player = VLPlayer.getPlayer(UUID.fromString(e.getMessage().split(VaultCore.SEPARATOR)[1]));
            if (player != null && "vaultmc".equalsIgnoreCase(VaultCore.getInstance().getConfig().getString("server"))) {
                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> player.teleport(Bukkit.getWorld("Lobby").getSpawnLocation()));
            }
        }
    }
}
