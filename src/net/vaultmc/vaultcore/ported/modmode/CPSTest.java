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

package net.vaultmc.vaultcore.ported.modmode;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public class CPSTest implements Listener {
    private static final Map<VLPlayer, Integer> cpsMap = new HashMap<>();

    public static void run(VLPlayer tester, VLPlayer tested) {
        if (cpsMap.containsKey(tested)) throw new UnsupportedOperationException("Already running CPS");
        cpsMap.put(tested, 0);
        Bukkit.getScheduler().runTaskLater(VaultCore.getInstance().getBukkitPlugin(), () -> {
            tester.sendMessage(VaultLoader.getMessage("mod-mode.last-three-seconds")
                    .replace("{PLAYER}", tested.getFormattedName())
                    .replace("{TIME}", "3"));
            cpsMap.remove(tested);
        }, 7 * 20L);
        Bukkit.getScheduler().runTaskLater(VaultCore.getInstance().getBukkitPlugin(), () -> {
            tester.sendMessage(VaultLoader.getMessage("mod-mode.last-three-seconds")
                    .replace("{PLAYER}", tested.getFormattedName())
                    .replace("{TIME}", "2"));
            cpsMap.remove(tested);
        }, 8 * 20L);
        Bukkit.getScheduler().runTaskLater(VaultCore.getInstance().getBukkitPlugin(), () -> {
            tester.sendMessage(VaultLoader.getMessage("mod-mode.last-three-seconds")
                    .replace("{PLAYER}", tested.getFormattedName())
                    .replace("{TIME}", "1"));
            cpsMap.remove(tested);
        }, 9 * 20L);
        Bukkit.getScheduler().runTaskLater(VaultCore.getInstance().getBukkitPlugin(), () -> {
            tester.sendMessage(VaultLoader.getMessage("mod-mode.cps-test-result")
                    .replace("{PLAYER}", tested.getFormattedName())
                    .replace("{CPS}", String.valueOf(cpsMap.get(tested) / 10)));
            cpsMap.remove(tested);
        }, 10 * 20L);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.PHYSICAL) {
            VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
            if (cpsMap.containsKey(player)) {
                cpsMap.replace(player, cpsMap.get(player) + 1);
            }
        }
    }
}
