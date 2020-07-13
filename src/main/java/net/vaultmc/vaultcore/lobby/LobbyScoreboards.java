package net.vaultmc.vaultcore.lobby;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftChatMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyScoreboards extends ConstructorRegisterListener {
    private static final Map<VLPlayer, Objective> objectives = new HashMap<>();
    private static final Scoreboard scoreboard = new Scoreboard();

    public LobbyScoreboards() {
        Bukkit.getScheduler().runTaskTimer(VaultCore.getInstance().getBukkitPlugin(), () -> {
            for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
                if (player.getWorld().getName().toLowerCase().contains("lobby")) {
                    showScoreboard(player);
                }
            }
        }, 200L, 200L);
    }

    public static int onlinePlayers() {
        int players = 0;
        for (VLPlayer player : VLPlayer.getOnlinePlayers()) {
            if (!player.isVanished()) {
                players++;
            }
        }
        return players;
    }

    public static String groupName(String group) {
        switch (group) {
            case "admin":
                return ChatColor.BLUE + "Admin";
            case "mod":
                return ChatColor.DARK_AQUA + "Mod";
            case "helper":
                return ChatColor.YELLOW + "Helper";
            case "trusted":
                return ChatColor.DARK_GREEN + "Trusted";
            case "overlord":
                return ChatColor.DARK_RED + "" + ChatColor.BOLD + "Overlord";
            case "lord":
                return ChatColor.RED + "" + ChatColor.BOLD + "Lord";
            case "god":
                return ChatColor.GOLD + "God";
            case "titan":
                return ChatColor.GREEN + "Titan";
            case "hero":
                return ChatColor.AQUA + "Hero";
            case "patreon":
                return ChatColor.WHITE + "Patreon";
            case "member":
                return ChatColor.GRAY + "Member";
            default:
                return ChatColor.DARK_GRAY + "Default";
        }
    }

    private void showScoreboard(VLPlayer player) {
        removeScoreboard(player);
        Objective obj = new Objective(scoreboard, "sb_" + player.getUniqueId().toString().split("-")[0],
                ObjectiveCriteria.DUMMY, CraftChatMessage.fromStringOrNull(ChatColor.YELLOW + "" + ChatColor.BOLD + "VaultMC"),
                ObjectiveCriteria.RenderType.INTEGER);
        objectives.put(player, obj);

        ClientboundSetObjectivePacket objCreate = new ClientboundSetObjectivePacket(obj, 0 /* Create */);
        ClientboundSetDisplayObjectivePacket displaySlot = new ClientboundSetDisplayObjectivePacket(1 /* Sidebar */, obj);
        ((CraftPlayer) player.getPlayer()).getHandle().connection.send(objCreate);
        ((CraftPlayer) player.getPlayer()).getHandle().connection.send(displaySlot);

        List<String> content = Arrays.asList(
                " ",
                ChatColor.WHITE + "Rank: " + ChatColor.GREEN + groupName(player.getGroup()),
                ChatColor.WHITE + "Players: " + ChatColor.GREEN + onlinePlayers(),
                ChatColor.WHITE + "Latency: " + ChatColor.GREEN + player.getPing(),
                "  ",
                ChatColor.YELLOW + "vaultmc.net"
        );

        for (int i = 0; i < content.size(); i++) {
            ((CraftPlayer) player.getPlayer()).getHandle().connection.send(new ClientboundSetScorePacket(ServerScoreboard.Method.CHANGE, obj.getName(),
                    content.get(i), content.size() - i));
        }
    }

    private void removeScoreboard(VLPlayer player) {
        if (!objectives.containsKey(player)) return;
        ClientboundSetObjectivePacket objRemove = new ClientboundSetObjectivePacket(objectives.get(player), 1 /* Remove */);
        ((CraftPlayer) player.getPlayer()).getHandle().connection.send(objRemove);
        objectives.remove(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        removeScoreboard(VLPlayer.getPlayer(e.getPlayer()));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().getWorld().getName().toLowerCase().contains("lobby")) {
            showScoreboard(VLPlayer.getPlayer(e.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().toLowerCase().contains("lobby") && !e.getFrom().getName().toLowerCase().contains("lobby")) {
            showScoreboard(VLPlayer.getPlayer(e.getPlayer()));  // I hope there's something like containsIgnoreCase
        } else if (!e.getPlayer().getWorld().getName().toLowerCase().contains("lobby") && e.getFrom().getName().toLowerCase().contains("lobby")) {
            removeScoreboard(VLPlayer.getPlayer(e.getPlayer()));
        }
    }
}
