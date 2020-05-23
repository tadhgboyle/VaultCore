package net.vaultmc.vaultcore.survival.claim;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RootCommand(
        literal = "claim",
        description = "Claim a chunk for you."
)
@Permission(Permissions.ClaimCommand)
@PlayerOnly
public class ClaimCommand extends CommandExecutor implements Listener {
    public ClaimCommand() {
        register("claim", Collections.emptyList());
        register("add", Arrays.asList(
                Arguments.createLiteral("add"),
                Arguments.createArgument("player", Arguments.offlinePlayerArgument())
        ));
        register("remove", Arrays.asList(
                Arguments.createLiteral("remove"),
                Arguments.createArgument("player", Arguments.offlinePlayerArgument())
        ));
        VaultCore.getInstance().registerEvents(this);
    }

    @SubCommand("add")
    public void add(VLPlayer sender, VLOfflinePlayer player) {
        List<String> list = sender.getDataConfig().getStringList("claim-allowed-players");
        if (list.contains(player.getUniqueId().toString())) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.already-allowed"));
            return;
        }
        list.add(player.getUniqueId().toString());
        sender.getDataConfig().set("claim-allowed-players", list);
        sender.saveData();
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.allowed").replace("{PLAYER}", player.getFormattedName()));
    }

    @SubCommand("remove")
    public void remove(VLPlayer sender, VLOfflinePlayer player) {
        List<String> list = sender.getDataConfig().getStringList("claim-allowed-players");
        if (!list.contains(player.getUniqueId().toString())) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.not-allowed"));
            return;
        }
        list.remove(player.getUniqueId().toString());
        sender.getDataConfig().set("claim-allowed-players", list);
        sender.saveData();
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.removed").replace("{PLAYER}", player.getFormattedName()));
    }

    @SubCommand("claim")
    public void claim(VLPlayer sender) {
        if (!sender.getWorld().getName().contains("Survival")) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.must-be-in-survival"));
            return;
        }
        if (sender.getLocation().getChunk().getX() <= -4488 && sender.getLocation().getChunk().getX() >= -4490 &&
                sender.getLocation().getChunk().getZ() <= -3297 && sender.getLocation().getChunk().getZ() >= -3299) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.reserved"));
            return;
        }
        if (Claim.getClaims().containsKey(sender.getLocation().getChunk().getChunkKey())) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.already-claimed"));
            return;
        }
        if (sender.getPlayer().getLevel() < 3) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.exp-level"));
            return;
        }
        sender.getPlayer().setLevel(sender.getPlayer().getLevel() - 3);
        Claim claim = new Claim(sender, sender.getPlayer().getChunk());
        Claim.getClaims().put(sender.getLocation().getChunk().getChunkKey(), claim);
        VaultCore.getInstance().getVLData().serializeList("claims", new ArrayList<>(Claim.getClaims().values()));
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.claimed").replace("{X}",
                String.valueOf(sender.getLocation().getChunk().getX())).replace("{Z}",
                String.valueOf(sender.getLocation().getChunk().getZ())));
    }
}
