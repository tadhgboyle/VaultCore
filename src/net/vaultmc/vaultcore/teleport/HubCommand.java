package net.vaultmc.vaultcore.teleport;

import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;

import java.util.Collections;

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
