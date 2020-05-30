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
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import org.bukkit.World;

import java.util.Arrays;

@RootCommand(
        literal = "eco",
        description = "Manages economy!"
)
@Permission(Permissions.EconomyCommand)
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
        sender.sendMessage(VaultLoader.getMessage("economy.take").replace("{PLAYER}", player.getFormattedName())
                .replace("{AMOUNT}", VaultCore.numberFormat.format(amount)));
    }
}
