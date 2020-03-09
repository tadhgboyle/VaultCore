package net.vaultmc.vaultcore.teleport.tpa;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.messenger.SQLMessenger;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.Listener;

@RootCommand(
        literal = "tpahere",
        description = "Request a player to teleport to you."
)
@Permission(Permissions.TPAHereCommand)
@PlayerOnly
public class TPAHereCommand extends CommandExecutor implements Listener {
    @SubCommand("tpahere")
    public void tpahere(VLPlayer player, VLOfflinePlayer target) {
        if (target.getUniqueId() == player.getUniqueId()) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.self_error"));
            return;
        }
        SQLMessenger.sendGlobalMessage("TPA2jRequest" + VaultCore.SEPARATOR + player.getUniqueId() + VaultCore.SEPARATOR + target.getUniqueId() +
                VaultCore.SEPARATOR + player.getFormattedName() + VaultCore.SEPARATOR + "normal");
    }
}