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

package net.vaultmc.vaultcore.punishments.mute;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.punishments.PunishmentUtils;
import net.vaultmc.vaultcore.punishments.PunishmentsDB;
import net.vaultmc.vaultcore.punishments.ban.IpBanCommand;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class MutedListener implements Listener {
    private void playerAttemptToChat(VLPlayer player, String message, Cancellable e, boolean async) {
        PunishmentsDB.PunishmentData muteData = PunishmentsDB.retrieveData("mutes", player.getUniqueId().toString(), !async);
        PunishmentsDB.PunishmentData tempmuteData = PunishmentsDB.retrieveData("tempmutes", player.getUniqueId().toString(), !async);
        PunishmentsDB.PunishmentData ipMuteData = PunishmentsDB.retrieveData("ipmutes", IpBanCommand.getPlayerIp(player), !async);
        PunishmentsDB.PunishmentData ipTempmuteData = PunishmentsDB.retrieveData("iptempmutes", IpBanCommand.getPlayerIp(player), !async);

        if ((muteData != null && muteData.isStatus()) || (ipMuteData != null && ipMuteData.isStatus())) {
            player.sendMessage(VaultLoader.getMessage("punishments.mute.message")
                    .replace("{REASON}", (muteData != null ? muteData.getReason() : ipMuteData.getReason()))
                    .replace("{ACTOR}", (muteData != null ? VLOfflinePlayer.getOfflinePlayer(muteData.getActor()).getFormattedName() :
                            VLOfflinePlayer.getOfflinePlayer(ipMuteData.getActor()).getFormattedName())));
            e.setCancelled(true);

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("vaultutils.mute")) {
                    p.sendMessage(VaultLoader.getMessage("punishments.mute.player-message")
                            .replace("{PLAYERFULLNAME}", player.getFormattedName())
                            .replace("{MESSAGE}", message));
                }
            }
        } else if (tempmuteData != null && tempmuteData.isStatus()) {
            if (PunishmentUtils.currentTime() >= tempmuteData.getExpiry()) {
                PunishmentsDB.unregisterData("tempmutes", player.getUniqueId().toString());
            } else {
                player.sendMessage(VaultLoader.getMessage("punishments.tempmute.message")
                        .replace("{REASON}", tempmuteData.getReason())
                        .replace("{ACTOR}", VLOfflinePlayer.getOfflinePlayer(tempmuteData.getActor()).getFormattedName())
                        .replace("{EXPIRY}", PunishmentUtils.humanReadableTime(tempmuteData.getExpiry() - PunishmentUtils.currentTime()
                        )));
                e.setCancelled(true);

                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("vaultutils.mute")) {
                        p.sendMessage(VaultLoader.getMessage("punishments.mute.player-message")
                                .replace("{PLAYERFULLNAME}", player.getFormattedName())
                                .replace("{MESSAGE}", message));
                    }
                }
            }
        } else if (ipTempmuteData != null && ipTempmuteData.isStatus()) {
            if (PunishmentUtils.currentTime() >= ipTempmuteData.getExpiry()) {
                PunishmentsDB.unregisterData("iptempmutes", player.getUniqueId().toString());
            } else {
                player.sendMessage(VaultLoader.getMessage("punishments.tempmute.message")
                        .replace("{REASON}", ipTempmuteData.getReason())
                        .replace("{ACTOR}", VLOfflinePlayer.getOfflinePlayer(ipTempmuteData.getActor()).getFormattedName())
                        .replace("{EXPIRY}", PunishmentUtils.humanReadableTime(ipTempmuteData.getExpiry() - PunishmentUtils.currentTime()
                        )));
                e.setCancelled(true);

                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("vaultutils.mute")) {
                        p.sendMessage(VaultLoader.getMessage("punishments.mute.player-message")
                                .replace("{PLAYERFULLNAME}", player.getFormattedName())
                                .replace("{MESSAGE}", message));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        playerAttemptToChat(VLPlayer.getPlayer(e.getPlayer()), e.getMessage(), e, true);
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        playerAttemptToChat(VLPlayer.getPlayer(e.getPlayer()), "Sign Edit: " + e.getLine(0) + ", " + e.getLine(1) + ", " + e.getLine(2) + ", " + e.getLine(3), e, false);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        FileConfiguration config = VaultCore.getInstance().getConfig();
        boolean flag = false;
        for (String s : config.getStringList("vaultutils.muted-commands-disallow")) {
            if (e.getMessage().contains(s)) {
                flag = true;
                break;
            }
        }

        if (!flag) return;

        playerAttemptToChat(VLPlayer.getPlayer(e.getPlayer()), "Command: " + e.getMessage(), e, false);
    }
}
