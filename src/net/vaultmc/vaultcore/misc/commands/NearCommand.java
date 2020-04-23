package net.vaultmc.vaultcore.misc.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

@RootCommand(literal = "near", description = "Find out who is near you.")
@Permission(Permissions.NearCommand)
public class NearCommand extends CommandExecutor {
    public NearCommand() {
        this.unregisterExisting();
        register("near", Collections.emptyList());
        register("nearRadius", Collections.singletonList(Arguments.createArgument("near", Arguments.integerArgument(10))));
    }

    @SubCommand("near")
    public void near(VLPlayer sender) {
        findNear(sender, 10);
    }

    @SubCommand("nearRadius")
    public void nearRadius(VLPlayer sender, int radius) {
        findNear(sender, radius);
    }

    private void findNear(VLPlayer sender, int radius) {
        Collection<String> nearPlayers = new TreeSet<>();
        for (Player players : sender.getLocation().getNearbyPlayers(radius)) {
            if (VLPlayer.getPlayer(players) == sender) continue;
            nearPlayers.add(VLPlayer.getPlayer(players).getFormattedName());
        }
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.near"), nearPlayers.size(), radius, Utilities.listToString(nearPlayers, true)));
    }
}
