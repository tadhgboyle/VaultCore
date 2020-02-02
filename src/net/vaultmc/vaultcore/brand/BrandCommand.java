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

package net.vaultmc.vaultcore.brand;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(
        literal = "brand",
        description = "Checks for the brand reported by the client for the player."
)
@Permission(Permissions.BrandCommand)
public class BrandCommand extends CommandExecutor {
    public BrandCommand() {
        register("checkBrand", Collections.singletonList(
                Arguments.createArgument("player", Arguments.playerArgument())));
    }

    @SubCommand("checkBrand")
    public void execute(VLCommandSender sender, VLPlayer player) {
        if (!BrandListener.getBrands().containsKey(player)) {
            sender.sendMessage(VaultLoader.getMessage("brand-not-ready"));
            return;
        }

        sender.sendMessage(VaultLoader.getMessage("brand").replace("{PLAYER}", player.getFormattedName())
                .replace("{BRAND}", BrandListener.getBrands().get(player)));
    }
}
