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
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RootCommand(literal = "nightvision", description = "Enable or disable nightvision.")
@Permission(Permissions.NightvisionCommand)
@PlayerOnly
@Aliases("nv")
public class NightvisionCommand extends CommandExecutor implements Listener {

    static Set<VLPlayer> nightvisionPlayers = new HashSet<>();

    public NightvisionCommand() {
        register("nightvision", Collections.emptyList());
    }

    @SubCommand("nightvision")
    public void nightvision(VLPlayer sender) {
        if (nightvisionPlayers.contains(sender)) {
            nightvisionPlayers.remove(sender);
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.nightvision"), "off"));
            sender.removePotionEffect(PotionEffectType.NIGHT_VISION);
        } else {
            nightvisionPlayers.add(sender);
            sender.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 1));
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.nightvision"), "on"));
        }
    }

    @EventHandler
    public void worldChangeEvent(PlayerChangedWorldEvent e) {
        // For donors. They will not have admin permission
        if (nightvisionPlayers.contains(VLPlayer.getPlayer(e.getPlayer())) && !e.getPlayer().hasPermission(Permissions.NightvisionCommandAdmin)) {
            VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
            nightvisionPlayers.remove(player);
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    }
}
