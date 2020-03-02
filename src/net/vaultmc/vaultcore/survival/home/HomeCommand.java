package net.vaultmc.vaultcore.survival.home;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.commands.wrappers.WrappedSuggestion;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RootCommand(
        literal = "home",
        description = "Warps to your home."
)
@Permission(Permissions.Home)
@PlayerOnly
public class HomeCommand extends CommandExecutor implements Listener {
    public HomeCommand() {
        register("home", Collections.emptyList());
        register("homeHome", Collections.singletonList(Arguments.createArgument("name", Arguments.word())));
        VaultCore.getInstance().registerEvents(this);
    }

    @TabCompleter(
            subCommand = "homeHome",
            argument = "name"
    )
    public List<WrappedSuggestion> suggestions(VLPlayer sender, String remaining) {
        return sender.getPlayerData().getKeys().stream().filter(p -> p.startsWith("home.")).map(s ->
                new WrappedSuggestion(s.replaceFirst("home.", ""))).collect(Collectors.toList());
    }

    @SubCommand("home")
    public void home(VLPlayer sender) {
        homeHome(sender, "home");
    }

    @SubCommand("homeHome")
    public void homeHome(VLPlayer sender, String home) {
        if (!sender.getPlayerData().contains("home." + home)) {
            sender.sendMessage(VaultLoader.getMessage("home.no-home").replace("{HOME}", home));
            return;
        }
        sender.teleportNoMove(Utilities.deserializeLocation(sender.getPlayerData().getString("home." + home)));
    }
}
