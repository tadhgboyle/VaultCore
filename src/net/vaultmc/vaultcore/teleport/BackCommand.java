package net.vaultmc.vaultcore.teleport;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Location;

import java.util.Collections;
import java.util.Stack;

@RootCommand(literal = "back", description = "Teleport to your previous location.")
@Permission(Permissions.BackCommand)
@PlayerOnly
public class BackCommand extends CommandExecutor {
    public BackCommand() {
        this.register("back", Collections.emptyList());
    }

    @SubCommand("back")
    public void back(VLPlayer player) {
        if (PlayerTPListener.teleports.containsKey(player.getUniqueId())) {
            Stack<Location> stack = PlayerTPListener.teleports.get(player.getUniqueId());
            Location before = stack.pop();
            if (!player.hasPermission(Permissions.CooldownBypass)) player.teleportNoMove(before);
            else player.teleport(before);
            if (stack.empty()) {
                PlayerTPListener.teleports.remove(player.getUniqueId());
            }
        } else {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.back.no_teleport_location"));
        }
    }
}