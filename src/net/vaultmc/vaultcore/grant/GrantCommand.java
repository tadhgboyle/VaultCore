package net.vaultmc.vaultcore.grant;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.util.Collections;

@RootCommand(
        literal = "grant",
        description = "Rank GUI"
)
@Permission(Permissions.GrantCommand)
@PlayerOnly
public class GrantCommand extends CommandExecutor {
    public GrantCommand() {
        register("grant", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    @SubCommand("grant")
    public void grant(VLPlayer player, VLPlayer target) {
        if (target == player) {
            player.sendMessage(ChatColor.RED + "You can't give yourself a rank!");
            return;
        }
        if (player.hasPermission(Permissions.GrantCommandAdmin)) {
            player.openInventory(GrantCommandInv.getGrantInventoryAdmin(target));
        } else if (player.hasPermission(Permissions.GrantCommandMod)) {
        	if (target.getGroup().equalsIgnoreCase("admin") || target.getGroup().equalsIgnoreCase("moderator")) {
        		player.sendMessage(VaultLoader.getMessage("vaultcore.commands.grant.mod-no-perm"));
        	}
            player.openInventory(GrantCommandInv.getGrantInventoryMod(target));
        }
    }
}