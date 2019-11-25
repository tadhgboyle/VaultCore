package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.listeners.PlayerTPListener;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(
        literal = "back",
        description = "Teleport to your previous location."
)
@Permission(Permissions.BackCommand)
@PlayerOnly
public class BackCommand extends CommandExecutor {

    public BackCommand() {
        this.register("back", Collections.emptyList(), "VaultCore");
    }

    String string = Utilities.string;

    @SubCommand("back")
    public void execute(CommandSender sender, ArgumentProvider args) {

        Player player = (Player) sender;

        if (PlayerTPListener.teleports.containsKey(player.getUniqueId())) {

            Location before = PlayerTPListener.teleports.get(player.getUniqueId());

            player.teleport(before);
            sender.sendMessage(string + "You have been teleported to your previous location...");
            PlayerTPListener.teleports.remove(player.getUniqueId());
        } else {
            sender.sendMessage(ChatColor.RED + "You have nowhere to teleport to!");
        }
    }
}