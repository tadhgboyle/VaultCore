package net.vaultmc.vaultcore.pvp;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;

import java.util.Collections;

@RootCommand(
        literal = "kits",
        description = "Grab a kit!"
)
@PlayerOnly
@Permission(Permissions.KitGuiCommand)
public class KitCommand extends CommandExecutor {
    public KitCommand() {
        this.register("kits", Collections.emptyList(), "vaultpvp");
    }

    @SubCommand("kits")
    public void kits(VLPlayer p) {
        if (!p.getWorld().getName().equalsIgnoreCase("pvp")) {
            p.sendMessage(ChatColor.RED + "You must be in PvP world to use this command!");
            return;
        }

        KitGuis.openKitGui(p);
    }
}
