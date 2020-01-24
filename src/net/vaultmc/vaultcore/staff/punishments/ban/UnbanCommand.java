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

package net.vaultmc.vaultcore.staff.punishments.ban;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.staff.punishments.PunishmentsDB;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

@RootCommand(
        literal = "unban",
        description = "Re-allows a banned player to join the server."
)
@Permission(Permissions.UnbanCommand)
public class UnbanCommand extends CommandExecutor {
    public UnbanCommand() {
        register("unban", Collections.singletonList(Arguments.createArgument("player", Arguments.offlinePlayerArgument())));
        register("unbanSilent", Arrays.asList(
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("silent", Arguments.boolArgument())
        ));
    }

    @SubCommand("unban")
    public void unban(VLCommandSender sender, VLOfflinePlayer victim) {
        unbanPlayer(sender, victim, false);
    }

    @SubCommand("unbanSilent")
    public void unbanSilent(VLCommandSender sender, VLOfflinePlayer victim, boolean silent) {
        unbanPlayer(sender, victim, silent);
    }

    private void unbanPlayer(VLCommandSender actor, VLOfflinePlayer victim, boolean silent) {
        /*
        FileConfiguration data = Main.getInstance().getData();

        data.set("vaultutils." + victim.getUniqueId().toString() + ".ban.status", false);
        data.set("vaultutils." + victim.getUniqueId().toString() + ".tempban.status", false);

        Main.getInstance().saveConfig();
         */

        PunishmentsDB.unregisterData("bans", victim.getUniqueId().toString());
        PunishmentsDB.unregisterData("tempbans", victim.getUniqueId().toString());
        PunishmentsDB.unregisterData("ipbans", IpBanCommand.getPlayerIp(victim));  // I felt something might go wrong here, but
        PunishmentsDB.unregisterData("iptempbans", IpBanCommand.getPlayerIp(victim));  // I don't know what it is.

        actor.sendMessage(VaultLoader.getMessage("punishments.unban.sent").replace("{PLAYER}", victim.getFormattedName()));

        if (silent) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("vaultutils.silentnotify")) {
                    player.sendMessage(VaultLoader.getMessage("punishments.silent-flag") +
                            VaultLoader.getMessage("punishments.unban.announcement")
                                    .replace("{ACTOR}", actor.getName())
                                    .replace("{PLAYER}", victim.getFormattedName()));
                }
            }
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(
                        VaultLoader.getMessage("punishments.unban.announcement")
                                .replace("{ACTOR}", actor.getName())
                                .replace("{PLAYER}", victim.getFormattedName()));
            }
        }
    }
}
