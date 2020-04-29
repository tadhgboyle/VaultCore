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
        description = "Go back to the hub."
)
@Aliases({"lobby"})
@PlayerOnly
public class HubCommand extends CommandExecutor {

    public HubCommand() {
        register("hub", Collections.emptyList());
    }

    @SubCommand("hub")
    public void hub(VLPlayer sender) {
        sender.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
        return;
    }
}
