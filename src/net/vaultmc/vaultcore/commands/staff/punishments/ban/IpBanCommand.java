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

package net.vaultmc.vaultcore.commands.staff.punishments.ban;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.commands.staff.punishments.PunishmentsDB;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@RootCommand(
        literal = "ipban",
        description = "Disallows a player from joining the server permanently (by their IP address)."
)
@Permission(Permissions.IPBanCommand)
public class IpBanCommand extends CommandExecutor implements Listener {
    public IpBanCommand() {
        register("banNoReason", Collections.singletonList(Arguments.createArgument("player", Arguments.offlinePlayerArgument())));
        register("banSilent", Arrays.asList(
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("silent", Arguments.boolArgument())
        ));
        register("banSilentReason", Arrays.asList(
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("silent", Arguments.boolArgument()),
                Arguments.createArgument("reason", Arguments.greedyString())
        ));
        VaultCore.getInstance().registerEvents(this);
    }

    public static String getPlayerIp(VLOfflinePlayer player) {
        if (player.isOnline()) return player.getOnlinePlayer().getAddress().getAddress().getHostAddress();
        return VaultCore.getInstance().getData().getString("ip." + player.getUniqueId().toString());
    }

    static String getPlayerIp(UUID player) {
        return VaultCore.getInstance().getData().getString("ip." + player.toString());
    }

    @SubCommand("banNoReason")
    public void banNoReason(VLCommandSender sender, VLOfflinePlayer victim) {
        String reason = VaultLoader.getMessage("punishments.default-reason");
        banPlayer(sender, victim, reason, false);
    }

    @SubCommand("banSilent")
    public void banSilent(VLCommandSender sender, VLOfflinePlayer victim, boolean silent) {
        String reason = VaultLoader.getMessage("punishments.default-reason");
        banPlayer(sender, victim, reason, silent);
    }

    @SubCommand("banSilentReason")
    public void banSilentReason(VLCommandSender sender, VLOfflinePlayer victim, boolean silent, String reason) {
        banPlayer(sender, victim, reason, silent);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        FileConfiguration data = VaultCore.getInstance().getData();
        data.set("ip." + e.getPlayer().getUniqueId().toString(), e.getPlayer().getAddress().getAddress().getHostAddress());
        VaultCore.getInstance().saveConfig();
    }

    private void banPlayer(VLCommandSender actor, VLOfflinePlayer victim, String reason, boolean silent) {
        // FileConfiguration data = Main.getInstance().getData();

        if (victim.isOnline()) {
            victim.getOnlinePlayer().kick(VaultLoader.getMessage("punishments.ban.disconnect")
                    .replace("{ACTOR}", actor.getName())
                    .replace("{REASON}", reason));
        }

        PunishmentsDB.registerData("ipbans", new PunishmentsDB.PunishmentData(getPlayerIp(victim),
                true, reason, -1, actor.getName()));

        /*
        data.set("vaultutils." + victim.getUniqueId().toString() + ".ban.status", true);
        data.set("vaultutils." + victim.getUniqueId().toString() + ".ban.reason", reason);
        data.set("vaultutils." + victim.getUniqueId().toString() + ".ban.actor", actor.getName());  // Intended
        Main.getInstance().saveConfig();
         */
        actor.sendMessage(VaultLoader.getMessage("punishments.ban.sent").replace("{PLAYER}", victim.getFormattedName()));

        if (silent) {
            for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                if (player.hasPermission(Permissions.PunishmentNotify)) {
                    player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                            VaultLoader.getMessage("punishments.ban.announcement")
                                    .replace("{ACTOR}", actor.getFormattedName())
                                    .replace("{REASON}", reason)
                                    .replace("{PLAYER}", victim.getFormattedName()));
                }
            }
        } else {
            for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                player.sendMessage(
                        VaultLoader.getMessage("punishments.ban.announcement")
                                .replace("{ACTOR}", actor.getFormattedName())
                                .replace("{REASON}", reason)
                                .replace("{PLAYER}", victim.getFormattedName()));
            }
        }
    }
}
