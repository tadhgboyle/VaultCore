package net.vaultmc.vaultcore.vaultpvp.commands.stats;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.vaultpvp.utils.KitGuis;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

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
        KitGuis.openKitGui(p);
    }
}
