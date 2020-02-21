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

package net.vaultmc.vaultcore.misc.commands;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RootCommand(
        literal = "afk",
        description = "Toggles your (or a player's) AFK state."
)
@Permission(Permissions.AFKCommand)
public class AFKCommand extends CommandExecutor implements Listener {
    @Getter
    private static final Map<VLPlayer, Location> afk = new HashMap<>();  // I have no plans of saving this in data.yml
    private static final Location afkHub = new Location(Bukkit.getWorld("Lobby"), 121, 103, -6, 0F, 0F);

    public AFKCommand() {
        VaultCore.getInstance().registerEvents(this);
        register("afkSelf", Collections.emptyList());  // Multiple registration for different possible usages.
        register("afkOthers", Collections.singletonList(
                Arguments.createArgument("player", Arguments.playerArgument())
        ));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        if (afk.containsKey(player)) {
            player.teleport(afk.remove(player));
            player.sendMessage(VaultLoader.getMessage("you-no-longer-afk"));

            for (VLPlayer p : VLPlayer.getOnlinePlayers()) {
                p.sendMessage(VaultLoader.getMessage("afk.others-no-longer-afk").replace("{PLAYER}", player.getFormattedName()));
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        if (afk.containsKey(player)) {
            Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> player.teleport(afk.remove(player)));
            player.sendMessage(VaultLoader.getMessage("you-no-longer-afk"));

            for (VLPlayer p : VLPlayer.getOnlinePlayers()) {
                p.sendMessage(VaultLoader.getMessage("afk.others-no-longer-afk").replace("{PLAYER}", player.getFormattedName()));
            }
        }
    }

    @SubCommand("afkSelf")
    public void afkSelf(VLPlayer player) {
        boolean newValue = !afk.containsKey(player);
        Location loc = null;
        if (newValue) afk.put(player, player.getLocation());
        else loc = afk.remove(player);
        player.setTemporaryData("afk", newValue);

        if (newValue) {
            player.sendMessage(VaultLoader.getMessage("you-afk"));
            player.teleport(afkHub);

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(VaultLoader.getMessage("afk.others-afk").replace("{PLAYER}", player.getFormattedName()));
            }
        } else {
            player.sendMessage(VaultLoader.getMessage("you-no-longer-afk"));
            if (loc != null) player.teleport(loc);

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(VaultLoader.getMessage("afk.others-no-longer-afk").replace("{PLAYER}", player.getFormattedName()));
            }
        }
    }

    @SubCommand("afkOthers")
    @Permission(Permissions.AFKCommandOther)
    public void afkOthers(VLCommandSender sender, VLPlayer player) {
        boolean newValue = !afk.containsKey(player);
        Location loc = null;
        if (newValue) afk.put(player, player.getLocation());
        else loc = afk.remove(player);
        player.setTemporaryData("afk", newValue);

        if (newValue) {
            player.sendMessage(VaultLoader.getMessage("you-afk"));
            player.teleport(afkHub);

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(VaultLoader.getMessage("afk.others-afk").replace("{PLAYER}", player.getFormattedName()));
            }
        } else {
            player.sendMessage(VaultLoader.getMessage("you-no-longer-afk"));
            if (loc != null) player.teleport(loc);

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(VaultLoader.getMessage("afk.others-no-longer-afk").replace("{PLAYER}", player.getFormattedName()));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        afk.remove(VLPlayer.getPlayer(e.getPlayer()));
    }
}
