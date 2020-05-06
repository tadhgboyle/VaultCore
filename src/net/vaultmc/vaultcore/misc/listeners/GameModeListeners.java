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

package net.vaultmc.vaultcore.misc.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class GameModeListeners implements Listener {
    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        String world = e.getPlayer().getWorld().getName();

        if (world.contains("clans") || world.contains("Survival") || world.toLowerCase().contains("skyblock")) {
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
        } else if (world.contains("Lobby")) {
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
        } else if (world.contains("Creative")) {
            e.getPlayer().setGameMode(GameMode.CREATIVE);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity().getWorld().getName().equalsIgnoreCase("Lobby"))
            e.setCancelled(true);
    }
}
