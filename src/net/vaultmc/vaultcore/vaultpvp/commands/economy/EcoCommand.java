package net.vaultmc.vaultcore.vaultpvp.commands.economy;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.vaultpvp.utils.API;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.util.Arrays;

@RootCommand(
        literal = "coins",
        description = "Configure your economy management!"
)
@Permission(Permissions.BalCommandGive)

public class EcoCommand extends CommandExecutor {

    public EcoCommand() {
        register("getMoney", Arrays.asList(
                Arguments.createLiteral("get"),
                Arguments.createArgument("player", Arguments.playerArgument())
        ), "vaultpvp");


        register("setMoney", Arrays.asList(
                Arguments.createLiteral("set"),
                Arguments.createArgument("player", Arguments.playerArgument()),
                Arguments.createArgument("amount", Arguments.doubleArgument())
        ), "vaultpvp");


    }

    @SubCommand("getMoney")
    public void getMoney(VLCommandSender sender, VLPlayer target) {
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "This player never joined before!");
            return;
        }

        sender.sendMessage(target.getDisplayName() + ChatColor.YELLOW + "'s balance is: " + ChatColor.DARK_GREEN + API.getCoins(target));
    }


    @SubCommand("setMoney")
    public void setMoney(VLCommandSender sender, VLPlayer target, double amount) {

        API.setCoins(target, amount);

        sender.sendMessage(ChatColor.YELLOW + "Successfully set "
                + target.getFormattedName() + ChatColor.YELLOW + "'coins to " + ChatColor.DARK_GREEN + amount);
    }

}

