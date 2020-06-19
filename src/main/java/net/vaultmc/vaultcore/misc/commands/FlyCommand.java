/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

package net.vaultmc.vaultcore.misc.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RootCommand(literal = "fly", description = "Enable fly for a player.")
@Permission(Permissions.FlyCommand)
public class FlyCommand extends CommandExecutor implements Listener {
    public static Set<UUID> flying = new HashSet<>();

    public FlyCommand() {
        register("flySelf", Collections.emptyList());
        register("flyOthers",
                Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    @SubCommand("flySelf")
    @PlayerOnly
    public void flySelf(VLPlayer player) {
        if (!player.hasPermission(Permissions.FlyLobbyOnlyBypass) && !player.getWorld().getName().equalsIgnoreCase("Lobby")) {
            player.sendMessageByKey("vaultcore.commands.fly.lobby-only");
            return;
        }
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.fly.gamemode_error"),
                    "You're", Utilities.capitalizeMessage(player.getGameMode().toString().toLowerCase())));
        } else if (!player.getAllowFlight()) {
            flying.add(player.getUniqueId());
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.fly.self"), "enabled"));
            player.setAllowFlight(true);
        } else {
            flying.remove(player.getUniqueId());
            player.sendMessage(
                    Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.fly.self"), "disabled"));
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

    @SubCommand("flyOthers")
    @Permission(Permissions.FlyCommandOther)
    public void flyOthers(VLCommandSender sender, VLPlayer target) {
        if (target.getGameMode() == GameMode.CREATIVE || target.getGameMode() == GameMode.SPECTATOR) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.fly.gamemode_error"),
                    "They're", Utilities.capitalizeMessage(target.getGameMode().toString().toLowerCase())));
        } else if (target.getAllowFlight()) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.fly.other"),
                    "disabled", target.getFormattedName()));
            target.setFlying(false);
            target.setAllowFlight(false);
            target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.fly.receiver"),
                    "disabled", sender.getFormattedName()));
            flying.remove(target.getUniqueId());
        } else {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.fly.other"),
                    "enabled", target.getFormattedName()));
            target.setAllowFlight(true);
            target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.fly.receiver"),
                    "enabled", sender.getFormattedName()));
            flying.add(target.getUniqueId());
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        if (flying.contains(player.getUniqueId()) && !player.hasPermission(Permissions.FlyLobbyOnlyBypass) && !player.getWorld().getName().equalsIgnoreCase("Lobby")) {
            flying.remove(player.getUniqueId());
            player.setAllowFlight(false);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        flying.remove(e.getPlayer().getUniqueId());
    }
}