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

package net.vaultmc.vaultcore.rewards;

import net.md_5.bungee.api.ChatColor;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.*;

@RootCommand(
        literal = "refer",
        description = "Refer another player to get rewards!"
)
@Permission(Permissions.ReferCommand)
@PlayerOnly
public class ReferCommand extends CommandExecutor {
    private static final Map<String, UUID> map = new HashMap<>();

    static {
        if (VaultCore.getInstance().getData().contains("refer-codes")) {
            for (String s : VaultCore.getInstance().getData().getConfigurationSection("refer-codes").getKeys(false)) {
                map.put(s, UUID.fromString(VaultCore.getInstance().getData().getString("refer-codes." + s)));
            }
        }
    }

    public ReferCommand() {
        register("getCode", Collections.emptyList());
        register("applyCode", Collections.singletonList(
                Arguments.createArgument("code", Arguments.greedyString())
        ));
    }

    public static void save() {
        VaultCore.getInstance().getData().set("refer-codes", null);
        for (Map.Entry<String, UUID> entry : map.entrySet()) {
            VaultCore.getInstance().getData().set("refer-codes." + entry.getKey(), entry.getValue().toString());
        }
        VaultCore.getInstance().saveConfig();
    }

    @SubCommand("getCode")
    public void getCode(VLPlayer player) {
        if (player.getDataConfig().contains("refer-code")) {
            player.sendMessageByKey("vaultcore.commands.refer.your-code", "code", player.getDataConfig().getString("refer-code"));
        } else {
            String code = RandomStringUtils.random(8, true, true);
            player.getDataConfig().set("refer-code", code);
            player.saveData();
            player.sendMessageByKey("vaultcore.commands.refer.your-code", "code", code);
        }
    }

    @SubCommand("applyCode")
    public void applyCode(VLPlayer player, String code) {
        if (map.containsKey(code)) {
            VLOfflinePlayer target = VLOfflinePlayer.getOfflinePlayer(map.get(code));
            if (target.getDataConfig().getStringList("refer-used").contains(player.getUniqueId().toString())) {
                player.sendMessageByKey("vaultcore.commands.refer.already-used");
                return;
            }
            List<String> list = target.getDataConfig().getStringList("refer-used");
            list.add(player.getUniqueId().toString());
            target.getDataConfig().set("refer-used", list);
            target.saveData();

            target.deposit(Bukkit.getWorld("skyblock"), 50);
            player.deposit(Bukkit.getWorld("skyblock"), 50);

            for (VLOfflinePlayer p : new VLOfflinePlayer[]{target, player}) {
                p.getDataConfig().set("crate-keys", p.getDataConfig().getInt("crate-keys", 0) + 1);
                p.saveData();
                if (p.isOnline() && p.getOnlinePlayer().getWorld().getName().equalsIgnoreCase("lobby")) {
                    p.getOnlinePlayer().getInventory().setItem(2, new ItemStackBuilder(Material.TRIPWIRE_HOOK)
                            .amount(Math.min(127, Math.max(1, p.getDataConfig().getInt("crate-keys", 0))))
                            .name(ChatColor.GREEN + "Crate Keys " + ChatColor.YELLOW + "(" + p.getDataConfig().getInt("crate-keys", 0) + ")")
                            .build());
                }
            }

            player.sendMessageByKey("vaultcore.commands.refer.success", "code", code, "player", target.getFormattedName());
            target.sendOrScheduleMessageByKey("vaultcore.commands.refer.used", "player", player.getFormattedName(), "code", code);
        } else {
            player.sendMessageByKey("vaultcore.commands.refer.invalid-code");
        }
    }
}
