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

package net.vaultmc.vaultcore.vanish;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RootCommand(
        literal = "vanish",
        description = "Toggles your vanish state or somebody else's."
)
@Permission(Permissions.VanishCommand)
@Aliases("v")
public class VanishCommand extends CommandExecutor {
    public static final Map<UUID, Boolean> vanished = new HashMap<>();

    public VanishCommand() {
        register("vanishSelf", Collections.emptyList());
        register("vanishOthers", Collections.singletonList(
                Arguments.createArgument("player", Arguments.playerArgument())
        ));
    }

    public static void setVanishState(VLPlayer player, boolean vanish) {
        if (vanish) {
            vanished.put(player.getUniqueId(), true);

            for (VLPlayer i : VLPlayer.getOnlinePlayers()) {
                if (i == player) continue;
                i.getPlayer().hidePlayer(VaultCore.getInstance().getBukkitPlugin(), player.getPlayer());
            }
        } else {
            vanished.remove(player.getUniqueId());

            for (VLPlayer i : VLPlayer.getOnlinePlayers()) {
                if (i == player) continue;
                i.getPlayer().showPlayer(VaultCore.getInstance().getBukkitPlugin(), player.getPlayer());
            }
        }
        player.setTemporaryData("vanished", vanish);
    }

    public static void update(VLPlayer player) {
        for (Map.Entry<UUID, Boolean> x : vanished.entrySet()) {
            if (x.getKey().toString().equals(player.getUniqueId().toString())) continue;
            if (VLPlayer.getPlayer(x.getKey()) != null && x.getValue()) {
                player.getPlayer().hidePlayer(VaultCore.getInstance().getBukkitPlugin(), Bukkit.getPlayer(x.getKey()));
            }
        }
    }

    @SubCommand("vanishSelf")
    @PlayerOnly
    public void vanishSelf(VLPlayer sender) {
        boolean newValue = !vanished.getOrDefault(sender.getUniqueId(), false);
        String state = newValue ? VaultLoader.getMessage("vanish.invisible") : VaultLoader.getMessage("vanish.visible");
        sender.sendMessage(VaultLoader.getMessage("vanish.player-state").replace("{STATE}", state));
        setVanishState(sender, newValue);
    }

    @SubCommand("vanishOthers")
    public void vanishOthers(CommandSender sender, VLPlayer player) {
        boolean newValue = !vanished.getOrDefault(player.getUniqueId(), false);
        String state = newValue ? VaultLoader.getMessage("vanish.invisible") : VaultLoader.getMessage("vanish.visible");
        player.sendMessage(VaultLoader.getMessage("vanish.player-state").replace("{STATE}", state));
        sender.sendMessage(VaultLoader.getMessage("vanish.others-state").replace("{STATE}", state));
        setVanishState(player, newValue);
    }
}
