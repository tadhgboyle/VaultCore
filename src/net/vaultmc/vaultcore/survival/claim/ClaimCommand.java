package net.vaultmc.vaultcore.survival.claim;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.commands.wrappers.WrappedSuggestion;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private static VLOfflinePlayer isAlreadyClaimed(Chunk chunk) {
        for (File file : VaultLoader.getPlayerDataFolder().listFiles()) {
            VLOfflinePlayer player = VLOfflinePlayer.getOfflinePlayer(
                    UUID.fromString(file.getName().replace(".yml", "")));
            if (deserializeChunks(player.getDataConfig().getStringList("claim.chunks")).contains(chunk)) {
                return player;
            }
        }
        return null;
    }

    static List<Chunk> deserializeChunks(List<String> chunks) {
        return chunks.stream().map(s -> {
            try {
                String[] data = s.split("\\|");
                return Bukkit.getWorld(data[0]).getChunkAt(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    static List<String> serializeChunks(List<Chunk> chunks) {
        return chunks.stream().map(c -> c.getWorld().getName() + "|" + c.getX() + "|" + c.getZ()).collect(Collectors.toList());
    }

    @SubCommand("add")
    public void add(VLPlayer sender, VLOfflinePlayer player) {
        List<String> players = sender.getDataConfig().getStringList("claim.allowed-players");
        if (players.contains(player.getUniqueId().toString())) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.already-allowed"));
            return;
        }
        players.add(player.getUniqueId().toString());
        sender.getDataConfig().set("claim.allowed-players", players);
        sender.saveData();
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.allowed").replace("{PLAYER}", player.getFormattedName()));
    }

    @SubCommand("remove")
    public void remove(VLPlayer sender, VLOfflinePlayer player) {
        List<String> players = sender.getDataConfig().getStringList("claim.allowed-players");
        if (!players.contains(player.getUniqueId().toString())) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.not-allowed"));
            return;
        }
        players.remove(player.getUniqueId().toString());
        sender.getDataConfig().set("claim.allowed-players", players);
        sender.saveData();
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.removed").replace("{PLAYER}", player.getFormattedName()));
    }

    @TabCompleter(
            subCommand = "remove",
            argument = "player"
    )
    public List<WrappedSuggestion> suggestPlayers(VLPlayer sender, String remaining) {
        return sender.getDataConfig().getStringList("claim.allowed-players")
                .stream().map(uuid -> new WrappedSuggestion(VLPlayer.getPlayer(UUID.fromString(uuid)).getName())).collect(Collectors.toList());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!e.getPlayer().getWorld().getName().contains("Survival")) return;
        VLOfflinePlayer owner = isAlreadyClaimed(e.getClickedBlock() != null ? e.getClickedBlock().getChunk() :
                e.getPlayer().getChunk());
        if (owner != null && !owner.getDataConfig().getStringList("claim.allowed-players").contains(e.getPlayer().getUniqueId().toString())) {
            e.getPlayer().sendMessage("vaultcore.commands.claim.cannot-interact");
            e.setCancelled(true);
        }
    }

    @SubCommand("claim")
    public void claim(VLPlayer sender) {
        if (!sender.getWorld().getName().contains("Survival")) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.must-be-in-survival"));
            return;
        }
        List<Chunk> chunks = deserializeChunks(sender.getDataConfig().getStringList("claim.chunks"));
        if (isAlreadyClaimed(sender.getLocation().getChunk()) != null) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.already-claimed"));
            return;
        }
        chunks.add(sender.getLocation().getChunk());
        sender.getDataConfig().set("claim.chunks", serializeChunks(chunks));
        sender.saveData();
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.claimed").replace("{X}",
                String.valueOf(sender.getLocation().getChunk().getX())).replace("{Z}",
                String.valueOf(sender.getLocation().getChunk().getZ())));
    }
}
