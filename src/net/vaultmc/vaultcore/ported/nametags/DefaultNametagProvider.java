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

package net.vaultmc.vaultcore.ported.nametags;

import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.World;

public class DefaultNametagProvider implements INametagProvider {
    private static String getSortPriority(VLPlayer p) {
        // Get the sort priority by permissions to a player.
        String group = p.getGroup();
        String priority = null;

        switch (group) {
            case "admin":
                priority = "a";
                break;
            case "moderator":
                priority = "b";
                break;
            case "trusted":
                priority = "c";
                break;
            case "patreon":
                priority = "d";
                break;
            case "member":
                priority = "e";
                break;
            case "default":
                priority = "f";
                break;
        }

        return priority;
    }

    @Override
    public Nametag provideNametag(VLPlayer player, World world) {
        return new Nametag("", "", getSortPriority(player));
    }
}
