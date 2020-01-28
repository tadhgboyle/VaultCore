package net.vaultmc.vaultcore.commands.teleport;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

@RootCommand(literal = "tpa", description = "Request to teleport to a player.")
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPACommand extends CommandExecutor {
    @Getter
    private static HashMap<UUID, UUID> requests = new HashMap<>();
    @Getter
    private static HashMap<UUID, UUID> requestsHere = new HashMap<>();

    public TPACommand() {
        register("tpa", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    @SubCommand("tpa")
    public void tpa(VLPlayer player, VLPlayer target) {
        if (target == player) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.self_error"));
            return;
        }
        if (!player.getDataConfig().getBoolean("settings.tpa")) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.target_disabled_tpa"));
        } else if (player.getDataConfig().getBoolean("settings.autotpa")) {
            player.teleport(target);
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.tpa.auto_accept_sender"),
                            target.getFormattedName()));
            target.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.tpa.auto_accept_target"),
                            player.getFormattedName()));
        } else {
            requests.put(target.getUniqueId(), player.getUniqueId());
            player.sendMessage(Utilities.formatMessage(
                    VaultLoader.getMessage("vaultcore.commands.tpa.tpa.request_sent"), target.getFormattedName()));
            target.sendMessage(Utilities.formatMessage(
                    VaultLoader.getMessage("vaultcore.commands.tpa.tpa.request_received"), player.getFormattedName()));
        }
    }
}