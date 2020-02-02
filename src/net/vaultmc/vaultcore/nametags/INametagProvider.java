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

package net.vaultmc.vaultcore.nametags;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.World;

public interface INametagProvider {
    Nametag provideNametag(VLPlayer player, World world);

    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode

    class Nametag {
        @Getter
        private String prefix;
        @Getter
        private String suffix;
        @Getter
        private String sortPriority;
    }
}
