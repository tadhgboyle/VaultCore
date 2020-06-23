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

package net.vaultmc.vaultcore.crate;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

@RootCommand(
        literal = "crate",
        description = "Manage crates."
)
@Permission(Permissions.ManageCrates)
@PlayerOnly
public class CrateCommand extends CommandExecutor {
    @Getter
    private static final List<Location> accessPoints;
    @Getter
    private static final Map<ItemStack, Integer> items = new HashMap<>();
    @Getter
    private static final List<ItemStack> itemsList = new ArrayList<>();

    static {
        if (VaultCore.getInstance().getData().contains("crate-items")) {
            for (String s : VaultCore.getInstance().getData().getConfigurationSection("crate-items").getKeys(false)) {
                items.put(VaultCore.getInstance().getData().getItemStack("crate-items." + s + ".item"),
                        VaultCore.getInstance().getData().getInt("crate-items." + s + ".weight"));
                itemsList.add(VaultCore.getInstance().getData().getItemStack("crate-items." + s + ".item"));
            }
        }

        if (!VaultCore.getInstance().getData().contains("crate-access-points")) {
            accessPoints = new ArrayList<>();
        } else {
            accessPoints = (List<Location>) VaultCore.getInstance().getData().getList("crate-access-points");
        }
    }

    public CrateCommand() {
        register("add", Arrays.asList(
                Arguments.createLiteral("add"),
                Arguments.createLiteral("item"),
                Arguments.createArgument("item", Arguments.itemArgument()),
                Arguments.createArgument("weight", Arguments.integerArgument(1, 100))
        ));
        register("addAccessPoint", Arrays.asList(
                Arguments.createLiteral("add"),
                Arguments.createLiteral("accesspoint"),
                Arguments.createArgument("world", Arguments.worldArgument()),
                Arguments.createArgument("location", Arguments.blockLocationArgument())
        ));
        register("remove", Arrays.asList(
                Arguments.createLiteral("remove"),
                Arguments.createLiteral("item"),
                Arguments.createArgument("hash", Arguments.integerArgument())
        ));
        register("removeAccessPoint", Arrays.asList(
                Arguments.createLiteral("remove"),
                Arguments.createLiteral("accesspoint"),
                Arguments.createArgument("world", Arguments.worldArgument()),
                Arguments.createArgument("location", Arguments.blockLocationArgument())
        ));
        register("list", Arrays.asList(
                Arguments.createLiteral("list"),
                Arguments.createLiteral("item")
        ));
        register("listAccessPoint", Arrays.asList(
                Arguments.createLiteral("list"),
                Arguments.createLiteral("accesspoint")
        ));
        register("key", Arrays.asList(
                Arguments.createLiteral("key"),
                Arguments.createArgument("player", Arguments.playersArgument())
        ));
    }

    public static void save() {
        VaultCore.getInstance().getData().set("crate-items", null);
        items.forEach((item, weight) -> {
            String uuid = UUID.randomUUID().toString();
            VaultCore.getInstance().getData().set("crate-items." + uuid + ".item", item);
            VaultCore.getInstance().getData().set("crate-items." + uuid + ".weight", weight);
        });
        VaultCore.getInstance().getData().set("crate-access-points", accessPoints);
        VaultCore.getInstance().saveConfig();
    }

    @SubCommand("key")
    public void key(VLPlayer player, Collection<VLPlayer> players) {
        for (VLPlayer p : players) {
            p.getDataConfig().set("crate-keys", p.getDataConfig().getInt("crate-keys", 0) + 1);
            p.saveData();
            if (p.getWorld().getName().equalsIgnoreCase("lobby")) {
                p.getInventory().setItem(2, new ItemStackBuilder(Material.TRIPWIRE_HOOK)
                        .amount(Math.min(127, Math.max(1, player.getDataConfig().getInt("crate-keys", 0))))
                        .name(ChatColor.GREEN + "Crate Keys " + ChatColor.YELLOW + "(" + player.getDataConfig().getInt("crate-keys", 0) + ")")
                        .build());
            }
            p.sendMessageByKey("vaultcore.commands.crate.obtained-key", "player", player.getFormattedName());
        }
        player.sendMessageByKey("vaultcore.commands.crate.given-key", "players", String.join(", ", players.stream().map(VLPlayer::getFormattedName).collect(Collectors.toList())));
    }

    @SubCommand("list")
    public void list(VLPlayer player) {
        items.forEach((item, weight) -> player.sendMessageByKey("vaultcore.commands.crate.list-item", "itemType", item.getI18NDisplayName(),
                "weight", String.valueOf(weight), "hash", String.valueOf(item.hashCode())));
    }

    @SubCommand("listAccessPoint")
    public void listAccessPoint(VLPlayer player) {
        accessPoints.forEach(loc -> player.sendMessageByKey("vaultcore.commands.crate.list-access-point", "w", loc.getWorld().getName(),
                "x", String.valueOf(loc.getBlockX()), "y", String.valueOf(loc.getBlockY()), "z", String.valueOf(loc.getBlockZ())));
    }

    @SubCommand("add")
    public void add(VLPlayer player, ItemStack item, int weight) {
        items.put(item, weight);
        itemsList.add(item);
        player.sendMessageByKey("vaultcore.commands.crate.added-item");
    }

    @SubCommand("addAccessPoint")
    public void addAccessPoint(VLPlayer player, World world, Location loc) {
        loc.setWorld(world);
        accessPoints.add(loc);
        player.sendMessageByKey("vaultcore.commands.crate.created-access-point");
    }

    @SubCommand("remove")
    public void remove(VLPlayer player, int hash) {
        ItemStack item = null;
        for (ItemStack i : items.keySet()) {
            if (i.hashCode() == hash) {
                item = i;
                break;
            }
        }
        items.remove(item);
        player.sendMessageByKey("vaultcore.commands.crate.removed-item");
    }

    @SubCommand("removeAccessPoint")
    public void removeAccessPoint(VLPlayer player, World world, Location loc) {
        loc.setWorld(world);
        accessPoints.remove(loc);
        player.sendMessageByKey("vaultcore.commands.crate.removed-access-point");
    }
}
