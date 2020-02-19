package net.vaultmc.vaultcore.teleport;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(literal = "tphere", description = "Teleport a player to you.")
@Permission(Permissions.TeleportCommandHere)
@Aliases("tph")
@PlayerOnly
public class TPHereCommand extends CommandExecutor {
    public TPHereCommand() {
        register("tphere", Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
    }

    @SubCommand("tphere")
    public void tpaHere(VLPlayer player, VLOfflinePlayer target) {
        if (target == player) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.self_error"));
            return;
        }
        TPCommand.teleport(target, player, status -> {
            if (status) {
                player.sendMessage(VaultLoader.getMessage("vaultcore.commands.tphere.sender").replace("{TARGET}", target.getFormattedName()));
                target.getOnlinePlayer().sendMessage(VaultLoader.getMessage("vaultcore.commands.tphere.target").replace("{SENDER}", player.getFormattedName()));
            } else {
                player.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.failed"));
            }
        });
    }
}