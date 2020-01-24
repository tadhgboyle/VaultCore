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

package net.vaultmc.vaultcore.listeners;

import net.vaultmc.vaultcore.commands.staff.VanishCommand;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class VanishListeners extends ConstructorRegisterListener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        if (!e.getJoinMessage().startsWith("[VANISH_FAKE_JOIN]")) {
            VanishCommand.update(player);
            if (VanishCommand.vanished.getOrDefault(e.getPlayer().getUniqueId(), false)) {
                VanishCommand.setVanishState(player, true);
                e.getPlayer().sendMessage(ChatColor.YELLOW + "You are still invisible!");
                e.setJoinMessage(null);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        VanishCommand.update(player);
        if (VanishCommand.vanished.getOrDefault(player.getUniqueId(), false)) {
            VanishCommand.setVanishState(player, true);
        }
    }
}
