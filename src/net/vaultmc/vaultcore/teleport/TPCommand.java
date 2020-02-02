package net.vaultmc.vaultcore.teleport;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.Collections;

@RootCommand(literal = "teleport", description = "Teleport to a player.")
@Permission(Permissions.TeleportCommand)
@Aliases("tp")
public class TPCommand extends CommandExecutor {
    public TPCommand() {
        unregisterExisting();
        register("teleportToPlayer",
                Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
        register("teleportPlayerToPlayer",
                Arrays.asList(Arguments.createArgument("target", Arguments.playerArgument()),
                        Arguments.createArgument("to", Arguments.playerArgument())));
        register("teleportToLocation",
                Collections.singletonList(Arguments.createArgument("location", Arguments.location3DArgument())));
        register("teleportPlayerTo", Arrays.asList(
                Arguments.createArgument("target", Arguments.playerArgument()),
                Arguments.createArgument("location", Arguments.location3DArgument())
        ));
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
    public void teleportToPlayer(VLPlayer sender, VLPlayer target) {
        if (sender == target) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.self_error"));
            return;
        }
        sender.teleport(target.getLocation());
        sender.sendMessage(Utilities.formatMessage(
                VaultLoader.getMessage("vaultcore.commands.teleport.sender_to_player"), target.getFormattedName()));
    }

    @SubCommand("teleportPlayerToPlayer")
    public void teleportPlayerToPlayer(VLCommandSender sender, VLPlayer target, VLPlayer to) {
        if (target == sender || to == sender) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.target_error"));
            return;
        }
        target.teleport(to.getLocation());
        sender.sendMessage(
                Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.teleport.player_to_player_sender"),
                        target.getFormattedName(), to.getFormattedName()));
        target.sendMessage(
                Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.teleport.player_to_player_target"),
                        sender.getFormattedName(), to.getFormattedName()));
        to.sendMessage(
                Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.teleport.player_to_player_receiver"),
                        sender.getFormattedName(), target.getFormattedName()));
    }
}
