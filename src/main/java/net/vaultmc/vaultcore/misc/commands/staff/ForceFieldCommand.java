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

package net.vaultmc.vaultcore.misc.commands.staff;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.util.*;

@RootCommand(
        literal = "forcefield",
        description = "Enables a force field around you that prevents players from entering."
)
@Permission(Permissions.ForceFieldCommand)
@PlayerOnly
public class ForceFieldCommand extends CommandExecutor implements Listener {
    public static final Map<UUID, Integer> forcefield = new HashMap<>();

    public ForceFieldCommand() {
        register("on", Arrays.asList(
                Arguments.createLiteral("on"),
                Arguments.createArgument("radius", Arguments.integerArgument(1, 5))
        ));
        register("off", Collections.singletonList(
                Arguments.createLiteral("off")
        ));
        VaultCore.getInstance().registerEvents(this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (!e.getPlayer().getWorld().getName().equalsIgnoreCase("Lobby")) return;
        for (Map.Entry<UUID, Integer> entry : forcefield.entrySet()) {
            if (!e.getPlayer().hasPermission(Permissions.ForceFieldExempt)) {
                Location loc = VLPlayer.getPlayer(entry.getKey()).getLocation();
                if (loc.distance(e.getPlayer().getLocation()) <= entry.getValue()) {
                    Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () ->
                            e.getPlayer().setVelocity(new Vector(e.getPlayer().getLocation().getX() > loc.getX() ? 0.5D : -0.5D,
                                    0.1D, e.getPlayer().getLocation().getZ() > loc.getZ() ? 0.5D : -0.5D)));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        forcefield.remove(e.getPlayer().getUniqueId());
    }

    @SubCommand("on")
    public void on(VLPlayer sender, int radius) {
        if (sender.isVanished()) {
            sender.sendMessage(VaultLoader.getMessage("forcefield.must-be-visible"));
            return;
        }
        if (forcefield.containsKey(sender.getUniqueId())) {
            sender.sendMessage(VaultLoader.getMessage("forcefield.already-on"));
            return;
        }
        forcefield.put(sender.getUniqueId(), radius);
        sender.sendMessage(VaultLoader.getMessage("forcefield.enabled"));
    }

    @SubCommand("off")
    public void off(VLPlayer sender) {
        if (!forcefield.containsKey(sender.getUniqueId())) {
            sender.sendMessage(VaultLoader.getMessage("forcefield.already-off"));
            return;
        }
        forcefield.remove(sender.getUniqueId());
        sender.sendMessage(VaultLoader.getMessage("forcefield.disabled"));
    }
}
