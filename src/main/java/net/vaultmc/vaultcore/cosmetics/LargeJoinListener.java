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

package net.vaultmc.vaultcore.cosmetics;

import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class LargeJoinListener extends ConstructorRegisterListener {
    @EventHandler(priority = EventPriority.HIGHEST)  // At this point appliedCosmetics is loaded
    public void onPlayerJoin(PlayerJoinEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        if (CosmeticsCommand.getAppliedCosmetics().get(player.getUniqueId()).contains(Cosmetic.ACTIONBAR_JOIN_MESSAGE)) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendActionBar(player.getExtraFormattedName() + ChatColor.YELLOW + " joined the game");
            }
        }
    }
}
