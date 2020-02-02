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

package net.vaultmc.vaultcore.punishments.ban;

import net.vaultmc.vaultcore.punishments.PunishmentUtils;
import net.vaultmc.vaultcore.punishments.PunishmentsDB;
import net.vaultmc.vaultloader.VaultLoader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class BannedListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        PunishmentsDB.PunishmentData banData = PunishmentsDB.retrieveData("bans", e.getUniqueId().toString());
        PunishmentsDB.PunishmentData tempbanData = PunishmentsDB.retrieveData("tempbans", e.getUniqueId().toString());
        PunishmentsDB.PunishmentData ipBanData = PunishmentsDB.retrieveData("ipbans", IpBanCommand.getPlayerIp(e.getUniqueId()));
        PunishmentsDB.PunishmentData ipTempbanData = PunishmentsDB.retrieveData("iptempbans", IpBanCommand.getPlayerIp(e.getUniqueId()));

        if ((banData != null && banData.isStatus()) || (ipBanData != null && ipBanData.isStatus())) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    VaultLoader.getMessage("punishments.ban.disconnect")
                            .replace("{REASON}", (banData != null ? banData.getReason() : ipBanData.getReason()))
                            .replace("{ACTOR}", (banData != null ? banData.getActor() : ipBanData.getActor())));
        } else if (tempbanData != null && tempbanData.isStatus()) {
            if (PunishmentUtils.currentTime() >= tempbanData.getExpiry()) {
                PunishmentsDB.unregisterData("tempbans", tempbanData.getVictim());
                //VaultCore.getInstance().saveConfig();  (Magic?)
            } else {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                        VaultLoader.getMessage("punishments.tempban.disconnect")
                                .replace("{REASON}", tempbanData.getReason())
                                .replace("{ACTOR}", tempbanData.getActor())
                                .replace("{EXPIRY}", PunishmentUtils.humanReadableTime(
                                        tempbanData.getExpiry() - PunishmentUtils.currentTime())));
            }
        } else if (ipTempbanData != null && ipTempbanData.isStatus()) {
            if (PunishmentUtils.currentTime() >= ipTempbanData.getExpiry()) {
                PunishmentsDB.unregisterData("iptempbans", ipTempbanData.getVictim());
            } else {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                        VaultLoader.getMessage("punishments.tempban.disconnect")
                                .replace("{REASON}", tempbanData.getReason())
                                .replace("{ACTOR}", tempbanData.getActor())
                                .replace("{EXPIRY}", PunishmentUtils.humanReadableTime(
                                        tempbanData.getExpiry() - PunishmentUtils.currentTime())));
            }
        }
    }
}
