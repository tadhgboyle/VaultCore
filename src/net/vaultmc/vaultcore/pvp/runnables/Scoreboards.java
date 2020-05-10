package net.vaultmc.vaultcore.pvp.runnables;

import lombok.Getter;
import lombok.SneakyThrows;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.DBConnection;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Scoreboards extends ConstructorRegisterListener {
    @Getter
    private static final Scoreboard scoreboard = new Scoreboard();

    public Scoreboards() {
        super();
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase("pvp")) {
            updateScoreboardFor(e.getPlayer());
        } else {
            removeScoreboard(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().getWorld().getName().equalsIgnoreCase("Pvp")) {
            return;
        }
        updateScoreboardFor(e.getPlayer());

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        removeScoreboard(e.getPlayer());
    }

    private void removeScoreboard(Player player) {
        try {
            Objective obj = scoreboard.getObjective(player.getUniqueId().toString().split("-")[0]);
            ClientboundSetObjectivePacket objectiveRemove = new ClientboundSetObjectivePacket(obj, 1);
            ((CraftPlayer) player).getHandle().connection.send(objectiveRemove);
            scoreboard.unregisterObjective(obj);
        } catch (NullPointerException ignored) {
        }
    }

    @SneakyThrows
    public void updateScoreboardFor(Player player) {
        try {
            scoreboard.unregisterObjective(scoreboard.getObjective(player.getUniqueId().toString().split("-")[0]));
        } catch (NullPointerException ignored) {
        }

        Objective obj = scoreboard.registerObjective(player.getUniqueId().toString().split("-")[0],
                ObjectiveCriteria.DUMMY, Component.Serializer.fromJson("{\"text\":\"\\u00a7e\\u00a7lPvP\"}"),
                ObjectiveCriteria.RenderType.INTEGER);
        scoreboard.setDisplaySlot(1, obj);
        ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;

        ClientboundSetObjectivePacket objectiveRemove = new ClientboundSetObjectivePacket(obj, 1);
        ClientboundSetObjectivePacket objectiveCreate = new ClientboundSetObjectivePacket(obj, 0);
        connection.send(objectiveRemove);
        connection.send(objectiveCreate);

        ClientboundSetDisplayObjectivePacket displayObjective = new ClientboundSetDisplayObjectivePacket(1, obj);
        connection.send(displayObjective);

        int kills = 0;
        int deaths = 0;

        ResultSet rs = VaultCore.getDatabase().executeQueryStatement(
                "SELECT kills, deaths FROM pvp_stats WHERE uuid = ?", player.getUniqueId().toString());
        if (rs.next()) {
            kills = rs.getInt("kills");
            deaths = rs.getInt("deaths");
        }

        double kd = kills;

        if (deaths > 0) {
            kd = (double) kills / deaths;
        }
        DecimalFormat value = new DecimalFormat("#.#");

        DBConnection database = VaultCore.getDatabase();

        ResultSet topKills = database.executeQueryStatement("SELECT DISTINCT(uuid), kills FROM pvp_stats ORDER BY kills DESC LIMIT 3");
        List<String> content = new ArrayList<>(Arrays.asList(
                ChatColor.YELLOW + "You",
                VLPlayer.getPlayer(player).getFormattedName(),
                "  ",
                ChatColor.YELLOW + "Kills: " + ChatColor.DARK_GREEN + kills,
                ChatColor.YELLOW + "Deaths: " + ChatColor.DARK_GREEN + deaths,
                ChatColor.YELLOW + "K/D: " + ChatColor.DARK_GREEN + value.format(kd),
                ChatColor.YELLOW + "Coins: " + ChatColor.GOLD + VLPlayer.getPlayer(player).getBalance(Bukkit.getWorld("pvp")),
                "    ",
                ChatColor.YELLOW + "-----------",
                ChatColor.YELLOW + "Top Kills",
                "      "
        ));
        while (topKills.next()) {
            content.add(VLOfflinePlayer.getOfflinePlayer(UUID.fromString(topKills.getString("uuid"))).getFormattedName() +
                    ChatColor.GREEN + " " + topKills.getInt("kills"));
        }

        for (int i = 0; i < content.size(); i++) {
            connection.send(new ClientboundSetScorePacket(ServerScoreboard.Method.CHANGE, obj.getName(), content.get(i),
                    content.size() - i));
        }
    }
}
