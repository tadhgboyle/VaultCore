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

package net.vaultmc.vaultcore.ported.economy;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.World;

import java.util.Arrays;

@RootCommand(
        literal = "eco",
        description = "Manages economy!"
)
@Permission("vaultutils.economy")
public class EconomyCommand extends CommandExecutor {
    public EconomyCommand() {
        register("getMoney", Arrays.asList(
                Arguments.createLiteral("get"),
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("world", Arguments.worldArgument())
        ));

        register("giveMoney", Arrays.asList(
                Arguments.createLiteral("give"),
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("world", Arguments.worldArgument()),
                Arguments.createArgument("amount", Arguments.doubleArgument(0))
        ));

        register("takeMoney", Arrays.asList(
                Arguments.createLiteral("take"),
                Arguments.createArgument("player", Arguments.offlinePlayerArgument()),
                Arguments.createArgument("world", Arguments.worldArgument()),
                Arguments.createArgument("amount", Arguments.doubleArgument(0))
        ));
    }

    @SubCommand("getMoney")
    public void getMoney(VLCommandSender sender, VLOfflinePlayer player, World world) {
        sender.sendMessage(VaultLoader.getMessage("economy.get").replace("{PLAYER}", player.getFormattedName())
                .replace("{AMOUNT}", VaultCore.numberFormat.format(player.getBalance(world))));
    }

    @SubCommand("giveMoney")
    public void giveMoney(VLCommandSender sender, VLOfflinePlayer player, World world, double amount) {
        player.deposit(world, amount);
        sender.sendMessage(VaultLoader.getMessage("economy.give").replace("{AMOUNT}", VaultCore.numberFormat.format(amount))
                .replace("{PLAYER}", player.getFormattedName()));

    }

    @SubCommand("takeMoney")
    public void takeMoney(VLCommandSender sender, VLOfflinePlayer player, World world, double amount) {
        player.withdraw(world, amount);
        sender.sendMessage(VaultLoader.getMessage("economy.take").replace("{PLAYER}", player.getFormattedName()
                .replace("{AMOUNT}", VaultCore.numberFormat.format(amount))));
    }
}
