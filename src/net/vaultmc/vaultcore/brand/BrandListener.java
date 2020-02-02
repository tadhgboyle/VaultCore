/*
 * VaultUtils: VaultMC functionalities provider.
 * Copyright (C) 2020 yangyang200
 *
 * VaultUtils is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VaultUtils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VaultCore.  If not, see <https://www.gnu.org/licenses/>.
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
