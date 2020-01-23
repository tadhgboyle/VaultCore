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
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.World;

import java.util.Arrays;

@RootCommand(
        literal = "transfer",
        description = "Transfers money to another player!"
)
@Permission("vaultutils.transfer")
@PlayerOnly
public class TransferCommand extends CommandExecutor {
    public TransferCommand() {
        this.register("transfer", Arrays.asList(
                Arguments.createArgument("player", Arguments.playerArgument()),
                Arguments.createArgument("amount", Arguments.doubleArgument(0))
        ));
    }

    @SubCommand("transfer")
    public void transferMoney(VLPlayer sender, VLPlayer player, double amount) {
        if (sender.hasMoney(sender.getWorld(), amount)) {
            sender.sendMessage(VaultLoader.getMessage("economy.money-not-enough"));
            return;
        }

        if (sender.getWorld().getEnvironment() == World.Environment.NETHER || sender.getWorld().getEnvironment() == World.Environment.THE_END) {
            sender.sendMessage(VaultLoader.getMessage("economy.transfer-world-incorrect"));
            return;
        }

        sender.withdraw(sender.getWorld(), amount);
        player.deposit(sender.getWorld(), amount);
        sender.sendMessage(VaultLoader.getMessage("economy.transfer-me").replace("{PLAYER}", player.getFormattedName())
                .replace("{WORLD}", sender.getWorld().getName()).replace("{AMOUNT}", VaultCore.numberFormat.format(amount)));
        sender.sendMessage(VaultLoader.getMessage("economy.transfer-you").replace("{PLAYER}", sender.getFormattedName())
                .replace("{AMOUNT}", VaultCore.numberFormat.format(amount)).replace("{WORLD}", sender.getWorld().getName()));
    }
}
