package net.vaultmc.vaultcore.tour;

import lombok.SneakyThrows;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.lang.reflect.Method;
import java.util.Collections;

/**
 * Start a tour on the executor.
 *
 * @author yangyang200
 */
@RootCommand(
        literal = "tour",
        description = "Start a tour on VaultMC!"
)
@Permission(Permissions.Tour)
@PlayerOnly
public class TourCommand extends CommandExecutor {
    public TourCommand() {
        register("tour", Collections.emptyList());
        register("tourStage", Collections.singletonList(Arguments.createArgument("method",
                Arguments.word())));
    }

    @SubCommand("tour")
    public void tour(VLPlayer sender) {
        Tour.start(sender);
    }

    @SubCommand("tourStage")
    @SneakyThrows
    public void tourStage(VLPlayer sender, String stage) {
        if (!Tour.getTouringPlayers().contains(sender.getUniqueId())) {
            sender.sendMessage(VaultLoader.getMessage("you-shouldnt-do-this"));
            return;
        }
        Tour.class.getDeclaredMethod(stage, VLPlayer.class).invoke(null, sender);
    }
}
