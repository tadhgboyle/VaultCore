package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(
        literal = "ping",
        description = "Check the ping of yourself or other players."
)
@Permission(Permissions.PingCommand)
public class PingCommand extends CommandExecutor {
    public PingCommand() {
        register("pingSelf", Collections.emptyList(), "vaultcore");
        register("pingOthers", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    private String string = VaultCore.getInstance().getConfig().getString("string");
    private String variable1 = VaultCore.getInstance().getConfig().getString("variable-1");
    private String variable2 = VaultCore.getInstance().getConfig().getString("variable-2");

    public static int getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }

    @SubCommand("pingSelf")
    @PlayerOnly
    public void pingSelf(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                string + "" + "Your ping is: " + variable2 + "" + getPing((Player) sender) + string + "ms"));
    }

    @SubCommand("pingOthers")
    @Permission(Permissions.PingCommandOther)
    public void pingOthers(CommandSender sender, Player target) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', variable1 + "" + VaultCoreAPI.getName(target)
                + string + "" + "'s ping is: " + variable2 + "" + getPing(target) + string + "ms"));
    }
}