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

package net.vaultmc.vaultcore.commands.economy;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;

@RootCommand(
        literal = "money",
        description = "Checks for your current balance."
)
@Permission(Permissions.BalanceCommand)
@PlayerOnly
@Aliases({"b", "balance", "bal"})
public class MoneyCommand extends CommandExecutor {
    public MoneyCommand() {
        this.register("checkBalance", Collections.emptyList());
    }

    @SubCommand("checkBalance")
    public void execute(VLPlayer sender) {
        sender.sendMessage(VaultLoader.getMessage("economy.balance").replace("{AMOUNT}",
                VaultCore.numberFormat.format(sender.getBalance(sender.getWorld()))));
    }
}
