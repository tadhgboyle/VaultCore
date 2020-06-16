/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

package net.vaultmc.vaultcore.survival;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@RootCommand(
        literal = "sv",
        description = "Survival-related commands."
)
@Permission(Permissions.SurvivalCommand)
@PlayerOnly
public class SurvivalCommand extends CommandExecutor {
    private static final Location svLoc = new Location(Bukkit.getWorld("Survival"), -1122.5, 63, -673.5, 0F, 0F);

    public SurvivalCommand() {
        register("sv", Collections.emptyList());
        register("kit", Arrays.asList(
                Arguments.createLiteral("kit"),
                Arguments.createArgument("name", Arguments.word())
        ));
        register("createKit", Arrays.asList(
                Arguments.createLiteral("createkit"),
                Arguments.createArgument("name", Arguments.word()),
                Arguments.createArgument("delay", Arguments.longArgument(1))
        ));
    }

    @SubCommand("createKit")
    public void createKit(VLPlayer sender, String name, long delay) {
        SurvivalKit kit = new SurvivalKit(name, "survival.kits." + name.toLowerCase(),
                Arrays.stream(sender.getInventory().getContents()).filter(Objects::nonNull).collect(Collectors.toList()), delay);
        SurvivalKit.getKits().put(kit.name.toLowerCase(), kit);
        SurvivalKit.save();
        sender.sendMessageByKey("vaultcore.commands.survival.kits.created", "kit", kit.name);
    }

    @SubCommand("kit")
    @MirrorCommand(
            literal = "kit",
            worlds = {"Survival", "Survival_nether", "Survival_the_end"},
            exclusions = 0
    )
    public void kit(VLPlayer sender, String name) {
        SurvivalKit kit = SurvivalKit.getKits().get(name.toLowerCase());
        if (kit == null) {
            sender.sendMessageByKey("vaultcore.commands.survival.kits.doesnt-exist");
            return;
        }
        if (sender.hasPermission(kit.permission) || kit.permission.equals("null")) {
            long canUse = sender.getDataConfig().getLong("survival.kits." + name.toLowerCase(), 0);
            if (System.currentTimeMillis() < canUse) {
                sender.sendMessageByKey("vaultcore.commands.survival.kits.must-wait", "time",
                        Utilities.humanReadableTime((canUse - System.currentTimeMillis()) / 1000));
                return;
            }
            kit.items.forEach(x -> sender.getInventory().addItem(x));
            sender.getDataConfig().set("survival.kits." + name.toLowerCase(), System.currentTimeMillis() + kit.delay);
            sender.saveData();
            sender.sendMessageByKey("vaultcore.commands.survival.kits.success", "kit", kit.name);
        } else {
            sender.sendMessageByKey("vaultcore.commands.survival.kits.couldnt-use");
        }
    }

    @SubCommand("sv")
    public void sv(VLPlayer player) {
        Location sv = Utilities.deserializeLocation(player.getPlayerData().getString("locations.sv"));
        if (sv == null) {
            player.sendMessage(VaultLoader.getMessage("vaultcore.commands.worldtp.never_joined_before"));
            player.teleport(svLoc);
        } else {
            player.teleport(sv);
            player.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.worldtp.teleported"),
                    "Survival"));
        }
    }
}