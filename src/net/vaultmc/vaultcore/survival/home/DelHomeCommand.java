package net.vaultmc.vaultcore.survival.home;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@RootCommand(
        literal = "delhome",
        description = "Remove a home you have created."
)
@Permission(Permissions.Home)
@PlayerOnly
public class DelHomeCommand extends CommandExecutor {
    public DelHomeCommand() {
        register("delHome", Collections.emptyList());
        register("delHomeHome", Collections.singletonList(Arguments.createArgument("name", Arguments.word())));
    }

    @SubCommand("delHome")
    public void delHome(VLPlayer sender) {
        delHomeHome(sender, "home");
    }

    @SubCommand("delHomeHome")
    public void delHomeHome(VLPlayer sender, String home) {
        Set<String> homes = sender.getPlayerData().getKeys().stream().filter(n -> n.startsWith("home.")).collect(Collectors.toSet());
        if (homes.contains(home)) {
            sender.getPlayerData().remove("home." + home);
            sender.sendMessage(VaultLoader.getMessage("home.deleted").replace("{HOME}", home));
        } else {
            sender.sendMessage(VaultLoader.getMessage("home.no-home").replace("{HOME}", home));
        }
    }
}