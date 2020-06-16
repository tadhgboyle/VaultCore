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

package net.vaultmc.vaultcore.punishments.ban;

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.punishments.PunishmentsDB;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class BannedListener extends ConstructorRegisterListener {
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
                            .replace("{ACTOR}", (banData != null ? VLOfflinePlayer.getOfflinePlayer(banData.getActor()).getFormattedName() :
                                    VLOfflinePlayer.getOfflinePlayer(ipBanData.getActor()).getFormattedName())));
        } else if (tempbanData != null && tempbanData.isStatus()) {
            if (Utilities.currentTime() >= tempbanData.getExpiry()) {
                PunishmentsDB.unregisterData("tempbans", tempbanData.getVictim());
                //VaultCore.getInstance().saveConfig();  (Magic?)
            } else {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                        VaultLoader.getMessage("punishments.tempban.disconnect")
                                .replace("{REASON}", tempbanData.getReason())
                                .replace("{ACTOR}", VLOfflinePlayer.getOfflinePlayer(tempbanData.getActor()).getFormattedName())
                                .replace("{EXPIRY}", Utilities.humanReadableTime(
                                        tempbanData.getExpiry() - Utilities.currentTime())));
            }
        } else if (ipTempbanData != null && ipTempbanData.isStatus()) {
            if (Utilities.currentTime() >= ipTempbanData.getExpiry()) {
                PunishmentsDB.unregisterData("iptempbans", ipTempbanData.getVictim());
            } else {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                        VaultLoader.getMessage("punishments.tempban.disconnect")
                                .replace("{REASON}", tempbanData.getReason())
                                .replace("{ACTOR}", VLOfflinePlayer.getOfflinePlayer(ipTempbanData.getActor()).getFormattedName())
                                .replace("{EXPIRY}", Utilities.humanReadableTime(
                                        tempbanData.getExpiry() - Utilities.currentTime())));
            }
        }
    }
}
