package net.vaultmc.vaultcore.commands.staff.vanish;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.utils.commands.Aliases;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultutils.VaultUtils;

@RootCommand(
        literal = "vanish",
        description = "Toggles your vanish state or somebody else's."
)
@Permission("vaultutils.vanish")
@Aliases("v")
public class VanishCommand extends CommandExecutor {
	
	String string = Utilities.string;
	String variable1 = Utilities.variable1;
	
    public static final Map<UUID, Boolean> vanished = new HashMap<>();

    public VanishCommand() {
        register("vanishSelf", Collections.emptyList());
        register("vanishOthers", Collections.singletonList(
                Arguments.createArgument("player", Arguments.playerArgument())
        ));
    }

    static void setVanishState(Player player, boolean vanish) {
        if (vanish) {
            vanished.put(player.getUniqueId(), true);

            for (Player i : Bukkit.getOnlinePlayers()) {
                if (i == player) continue;
                i.hidePlayer(VaultUtils.getInstance(), player);
            }
        } else {
            vanished.remove(player.getUniqueId());

            for (Player i : Bukkit.getOnlinePlayers()) {
                if (i == player) continue;
                i.showPlayer(VaultUtils.getInstance(), player);
            }
        }
    }

    static void update(Player player) {
        for (Map.Entry<UUID, Boolean> x : vanished.entrySet()) {
            if (x.getKey().toString().equals(player.getUniqueId().toString())) continue;
            if (Bukkit.getPlayer(x.getKey()) != null && x.getValue()) {
                player.hidePlayer(VaultUtils.getInstance(), Bukkit.getPlayer(x.getKey()));
            }
        }
    }

    @SubCommand("vanishSelf")
    @PlayerOnly
    public void vanishSelf(Player sender) {
        boolean newValue = !vanished.getOrDefault(sender.getUniqueId(), false);
        sender.sendMessage(newValue ? string + "You are now " + variable1 + "invisible" + string + "." :
                string + "You are now " + variable1 + "visible" + string + ".");
        setVanishState(sender, newValue);
    }

    @SubCommand("vanishOthers")
    @Permission(Permissions.VanishCommandOther)
    public void vanishOthers(CommandSender sender, Player player) {
        boolean newValue = !vanished.getOrDefault(player.getUniqueId(), false);
        player.sendMessage(newValue ? string + "You are now " + variable1 + "invisible" + string + "." :
                string + "You are now " + variable1 + "visible" + string + ".");
        sender.sendMessage(newValue ? VaultUtils.getName(player) + string + " is now " + variable1 +
                "invisible" + variable1 + "." : VaultUtils.getName(player) + string + " is now " +
                variable1 + "visible" + string + ".");
        setVanishState(player, newValue);
    }
}
