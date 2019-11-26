package net.vaultmc.vaultcore.commands.msg;

import com.mojang.brigadier.arguments.StringArgumentType;
import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

@RootCommand(
        literal = "msg",
        description = "Send a player a message."
)
@Permission(Permissions.MsgCommand)
@PlayerOnly
public class MsgCommand extends CommandExecutor {
    @Getter private static HashMap<UUID, UUID> replies = new HashMap<>();

    public MsgCommand() {
        this.register("msg", Arrays.asList(
                Arguments.createArgument("target", Arguments.playerArgument()),
                Arguments.createArgument("message", StringArgumentType.greedyString())), "vaultcore");
    }

    @SubCommand("msg")
    public void msg(CommandSender sender, Player target, String message) {
        Player player = (Player) sender;
        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player is offline!");
        }
        if (target == player) {
            player.sendMessage(ChatColor.RED + "You can't message yourself!");
        }
        if (!VaultCore.getInstance().getPlayerData().getBoolean("players." + target.getUniqueId() + ".settings.msg")) {
            player.sendMessage(ChatColor.RED + "That player has disabled messaging!");
        } else {
            String meTo = (ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " -> " + ChatColor.GOLD
                    + target.getName() + ChatColor.YELLOW + ":");
            String toMe = (ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " -> " + ChatColor.GOLD
                    + target.getName() + ChatColor.YELLOW + ":");

            player.sendMessage(meTo + " " + ChatColor.DARK_GREEN + message);
            target.sendMessage(toMe + " " + ChatColor.DARK_GREEN + message);

            replies.put(target.getUniqueId(), player.getUniqueId());
        }
    }
}
