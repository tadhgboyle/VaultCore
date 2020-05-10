package net.vaultmc.vaultcore.misc.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.commands.wrappers.WrappedSuggestion;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RootCommand(literal = "warp", description = "Teleport to a warp.")
@Permission(Permissions.WarpCommand)
@PlayerOnly
public class WarpCommand extends CommandExecutor {
    public WarpCommand() {
        register("warp", Collections.singletonList(Arguments.createArgument("warp", Arguments.word())));
        register("setWarp",
                Arrays.asList(Arguments.createLiteral("set"), Arguments.createArgument("name", Arguments.word())));
        register("delWarp",
                Arrays.asList(Arguments.createLiteral("delete"), Arguments.createArgument("name", Arguments.word())));
    }

    @TabCompleter(
            subCommand = "warp|delWarp",
            argument = "warp|name"
    )
    public List<WrappedSuggestion> suggestWarp(VLPlayer sender, String remaining) {
        return VaultCore.getInstance().getLocationFile().getConfigurationSection("warps").getKeys(false).stream().map(WrappedSuggestion::new).collect(Collectors.toList());
    }

    @SubCommand("warp")
    public void warp(VLPlayer sender, String warp) {
        if (VaultCore.getInstance().getLocationFile().get("warps." + warp) == null) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.warp.not_exist"), warp));
        } else {
            if (!sender.hasPermission(Permissions.CooldownBypass))
                sender.teleportNoMove(VaultCore.getInstance().getLocationFile().getLocation("warps." + warp));
            else sender.teleport(VaultCore.getInstance().getLocationFile().getLocation("warps." + warp));
        }
    }

    @SubCommand("setWarp")
    @Permission(Permissions.WarpCommandSet)
    public void setWarp(VLPlayer sender, String warp) {
        if (VaultCore.getInstance().getLocationFile().get("warps." + warp) == null) {
            VaultCore.getInstance().getLocationFile().set("warps." + warp, sender.getLocation());
            VaultCore.getInstance().saveLocations();
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.warp.set"), warp));
        } else {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.warp.already_exist"), warp));
        }
    }

    @SubCommand("delWarp")
    @Permission(Permissions.WarpCommandDelete)
    public void delWarp(VLCommandSender sender, String warp) {
        if (VaultCore.getInstance().getLocationFile().get("warps." + warp) == null) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.warp.not_exist"), warp));
            return;
        }

        VaultCore.getInstance().getLocationFile().set("warps." + warp, null);
        VaultCore.getInstance().saveLocations();
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.warp.deleted"), warp));
    }
}