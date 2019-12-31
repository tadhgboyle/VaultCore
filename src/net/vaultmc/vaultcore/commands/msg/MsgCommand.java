package net.vaultmc.vaultcore.commands.msg;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
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
@Aliases({"tell", "whisper", "w", "pm", "privatemessage"})
public class MsgCommand extends CommandExecutor {
    @Getter private static HashMap<UUID, UUID> replies = new HashMap<>();

    public MsgCommand() {
        unregisterExisting();
        this.register("msg", Arrays.asList(
                Arguments.createArgument("target", Arguments.playerArgument()),
                Arguments.createArgument("message", Arguments.greedyString())));
    }

    @SubCommand("msg")
    public void msg(CommandSender sender, Player target, String message) {
        Player player = (Player) sender;
        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player is offline!");
        }
        if (target == player) {
            player.sendMessage(ChatColor.RED + "You can't message yourself!");
            return;
        }
        if (!VaultCore.getInstance().getPlayerData().getBoolean("players." + target.getUniqueId() + ".settings.msg")) {
            player.sendMessage(ChatColor.RED + "That player has disabled messaging!");
        } else {
            String meTo = (ChatColor.GOLD + VaultCoreAPI.getName(player) + ChatColor.YELLOW + " -> " + ChatColor.GOLD
                    + VaultCoreAPI.getName(target) + ChatColor.YELLOW + ":");
            String toMe = (ChatColor.GOLD + VaultCoreAPI.getName(player) + ChatColor.YELLOW + " -> " + ChatColor.GOLD
                    + VaultCoreAPI.getName(target) + ChatColor.YELLOW + ":");

            player.sendMessage(meTo + " " + ChatColor.RESET + message);
            target.sendMessage(toMe + " " + ChatColor.RESET + message);

            replies.put(target.getUniqueId(), player.getUniqueId());
        }
    }
}
