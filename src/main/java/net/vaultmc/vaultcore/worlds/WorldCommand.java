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

package net.vaultmc.vaultcore.worlds;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RootCommand(
        literal = "world",
        description = "World Management"
)
@Permission(Permissions.WorldCommand)
public class WorldCommand extends CommandExecutor {
    @Getter
    private static final List<String> worlds;

    static {
        worlds = VaultCore.getInstance().getWorlds().getStringList("worlds");
    }

    public WorldCommand() {
        register("create", Arrays.asList(
                Arguments.createLiteral("create"),
                Arguments.createArgument("name", Arguments.word()),
                Arguments.createArgument("environment", Arguments.enumArgument(World.Environment.class)),
                Arguments.createArgument("type", Arguments.enumArgument(WorldType.class))
        ));
        register("createWithGenerator", Arrays.asList(
                Arguments.createLiteral("create"),
                Arguments.createArgument("name", Arguments.word()),
                Arguments.createArgument("environment", Arguments.enumArgument(World.Environment.class)),
                Arguments.createArgument("type", Arguments.enumArgument(WorldType.class)),
                Arguments.createArgument("generator", Arguments.greedyString())
        ));
        register("import", Arrays.asList(
                Arguments.createLiteral("import"),
                Arguments.createArgument("world", Arguments.string())
        ));
        register("rule", Arrays.asList(
                Arguments.createLiteral("rule"),
                Arguments.createArgument("world", Arguments.worldArgument()),
                Arguments.createArgument("name", Arguments.word()),
                Arguments.createArgument("value", Arguments.boolArgument())
        ));
        register("ruleInt", Arrays.asList(
                Arguments.createLiteral("ruleinteger"),
                Arguments.createArgument("world", Arguments.worldArgument()),
                Arguments.createArgument("name", Arguments.word()),
                Arguments.createArgument("value", Arguments.integerArgument())
        ));
        register("tp", Arrays.asList(
                Arguments.createLiteral("tp"),
                Arguments.createArgument("world", Arguments.worldArgument())
        ));
        register("setSpawn", Collections.singletonList(
                Arguments.createLiteral("setspawn")
        ));
        register("list", Collections.singletonList(Arguments.createLiteral("list")));
    }

    public static void save() {
        VaultCore.getInstance().getWorlds().set("worlds", worlds);
    }

    @SubCommand("setSpawn")
    @PlayerOnly
    public void setSpawn(VLPlayer sender) {
        sender.getWorld().setSpawnLocation(sender.getLocation());
        sender.sendMessageByKey("vaultcore.commands.world.set-spawn");
    }

    @SubCommand("list")
    public void list(VLCommandSender sender) {
        sender.sendMessageByKey("vaultcore.commands.world.world-list", "worlds",
                worlds.stream().map(s -> ChatColor.GOLD + s).collect(Collectors.joining(ChatColor.YELLOW + ", ")));
    }

    @SubCommand("create")
    public void create(VLCommandSender sender, String name, World.Environment environment, WorldType type) {
        Bukkit.createWorld(new WorldCreator(name)
                .type(type)
                .environment(environment));
        worlds.add(name);
        sender.sendMessageByKey("vaultcore.commands.world.world-created", "world", name);
    }

    @SubCommand("createWithGenerator")
    public void createWithGenerator(VLCommandSender sender, String name, World.Environment environment, WorldType type, String generator) {
        Bukkit.createWorld(new WorldCreator(name)
                .type(type)
                .environment(environment)
                .generator(generator));
        worlds.add(name);
        sender.sendMessageByKey("vaultcore.commands.world.world-created", "world", name);
    }

    @SubCommand("import")
    public void importWorld(VLCommandSender sender, String name) {
        Bukkit.createWorld(new WorldCreator(name));
        worlds.add(name);
        sender.sendMessageByKey("vaultcore.commands.world.world-imported", "world", name);
    }

    @SubCommand("rule")
    public void booleanRule(VLCommandSender sender, World world, String name, boolean value) {
        GameRule<?> rule = GameRule.getByName(name);
        if (rule == null) {
            sender.sendMessageByKey("vaultcore.commands.world.rule-not-found");
            return;
        }
        if (rule.getType() != Boolean.class) {
            sender.sendMessageByKey("vaultcore.commands.world.rule-must-be-boolean");
            return;
        }
        world.setGameRule((GameRule<Boolean>) rule, value);
        sender.sendMessageByKey("vaultcore.commands.world.set-game-rule", "rule", rule.getName(), "value", String.valueOf(value));
    }

    @SubCommand("ruleInt")
    public void intRule(VLCommandSender sender, World world, String name, int value) {
        GameRule<?> rule = GameRule.getByName(name);
        if (rule == null) {
            sender.sendMessageByKey("vaultcore.commands.world.rule-not-found");
            return;
        }
        if (rule.getType() != Integer.class) {
            sender.sendMessageByKey("vaultcore.commands.world.rule-must-be-boolean");
            return;
        }
        world.setGameRule((GameRule<Integer>) rule, value);
        sender.sendMessageByKey("vaultcore.commands.world.set-game-rule", "rule", rule.getName(), "value", String.valueOf(value));
    }

    @SubCommand("tp")
    @PlayerOnly
    public void tp(VLPlayer player, World world) {
        player.teleport(world.getSpawnLocation());
    }
}
