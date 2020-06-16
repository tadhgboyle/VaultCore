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

package net.vaultmc.vaultcore.economy;

import net.vaultmc.vaultcore.Permissions;
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
@Permission(Permissions.TransferCommand)
@PlayerOnly
public class TransferCommand extends CommandExecutor {
    public TransferCommand() {
        register("transfer", Arrays.asList(
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
        player.sendMessage(VaultLoader.getMessage("economy.transfer-you").replace("{PLAYER}", sender.getFormattedName())
                .replace("{AMOUNT}", VaultCore.numberFormat.format(amount)).replace("{WORLD}", sender.getWorld().getName()));
    }
}
