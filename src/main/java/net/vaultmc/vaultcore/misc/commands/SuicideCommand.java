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
import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@RootCommand(literal = "suicide", description = "Sometimes, you just dont want to be alive :(")
@Permission(Permissions.SuicideCommand)
@PlayerOnly
public class SuicideCommand extends CommandExecutor implements Listener {

    List<String> messages = Arrays.asList(
            "{SENDER}&e fell of a cliff while fighting a pigman was exploded by a ghast and burned to a crisp.",
            "{SENDER}&e committed alive'nt.",
            "{SENDER}&e found a tall ladder and short rope.",
            "{SENDER}&e didn't listen to Logic.",
            // Thanks to @psrcek
            "{SENDER}&e stepped on Lego.",
            "{SENDER}&e stubbed their toe.",
            // Thanks to @Clikz_
            "{SENDER}&e fell and couldn't get up."
    );

    public SuicideCommand() {
        unregisterExisting();
        register("suicide", Collections.emptyList());
    }

    @SubCommand("suicide")
    public void suicide(VLPlayer sender) {
        Player player = Bukkit.getPlayer(sender.getUniqueId());
        // Kill the player and replace the death message with the custom one
        EntityDamageEvent event = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.CUSTOM, sender.getHealth());
        player.setLastDamageCause(event);
        sender.setHealth(0);
        String message = ChatColor.translateAlternateColorCodes('&', messages.toArray()[new Random().nextInt(messages.size())].toString().replace("{SENDER}", sender.getFormattedName()));
        for (VLPlayer players : VLPlayer.getOnlinePlayers()) {
            if (PlayerSettings.getSetting(players, "minimal_chat")) continue;
            players.sendMessage(message);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity().getPlayer().getLastDamageCause() != null && event.getEntity().getPlayer().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.CUSTOM)) {
            event.setDeathMessage("");
        }
    }
}
