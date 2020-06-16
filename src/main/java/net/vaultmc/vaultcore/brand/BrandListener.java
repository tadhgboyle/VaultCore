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

package net.vaultmc.vaultcore.brand;

import lombok.Getter;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BrandListener extends ConstructorRegisterListener implements PluginMessageListener {
    @Getter
    private static final Map<VLPlayer, String> brands = new HashMap<>();

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        brands.remove(VLPlayer.getPlayer(e.getPlayer()));
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        try {
            brands.put(VLPlayer.getPlayer(player), new String(message, StandardCharsets.UTF_8).substring(1));
        } catch (Exception e) {
            VaultCore.getInstance().getLogger().severe("A severe error occurred while attempting to determine client brand for player.");
            e.printStackTrace();
        }
    }
}
