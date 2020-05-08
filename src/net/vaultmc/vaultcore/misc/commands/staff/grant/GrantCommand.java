package net.vaultmc.vaultcore.misc.commands.staff.grant;

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
    public void grant(VLPlayer sender, VLPlayer target) {
        if (target == sender) {
            sender.sendMessage(ChatColor.RED + "You can't give yourself a rank!");
            return;
        }
        if (sender.hasPermission(Permissions.GrantCommandAdmin)) {
            sender.openInventory(GrantCommandInv.getGrantInventoryAdmin(target));
        } else if (sender.hasPermission(Permissions.GrantCommandMod)) {
            if (target.getGroup().equalsIgnoreCase("admin") || target.getGroup().equalsIgnoreCase("moderator")) {
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.grant.mod-no-perm"));
            }
            sender.openInventory(GrantCommandInv.getGrantInventoryMod(target));
        }
    }
}