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

package net.vaultmc.vaultcore.nametags;

import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Nametags extends ConstructorRegisterListener implements Runnable {
    public static final DefaultNametagProvider defaultProvider = new DefaultNametagProvider();
    private static final Map<INametagProvider, World> providers = new HashMap<>();
    @Getter
    private static final Map<VLPlayer, Team> teams = new HashMap<>();
    private static Scoreboard scoreboard;

    public Nametags() {
        scoreboard = new Scoreboard();
        Bukkit.getScheduler().runTaskTimer(VaultCore.getInstance().getBukkitPlugin(), this, 30 * 20, 30 * 20);
    }

    public static void registerProvider(INametagProvider provider, World world) {
        providers.put(provider, world);
    }

    private static ChatFormatting getColor(VLPlayer p) {
        // Get the sort priority by permissions to a player.
        String group = p.getGroup();

        switch (group) {
            case "admin":
                return ChatFormatting.BLUE;
            case "moderator":
                return ChatFormatting.DARK_AQUA;
            case "helper":
                return ChatFormatting.YELLOW;
            case "trusted":
                return ChatFormatting.DARK_PURPLE;
            case "overlord":
                return ChatFormatting.DARK_RED;
            case "lord":
                return ChatFormatting.RED;
            case "god":
                return ChatFormatting.GOLD;
            case "titan":
                return ChatFormatting.GREEN;
            case "hero":
                return ChatFormatting.AQUA;
            case "patreon":
                return ChatFormatting.WHITE;
            case "member":
                return ChatFormatting.GRAY;
            case "default":
                return ChatFormatting.DARK_GRAY;
        }
        return null;
    }

    public static void forceUpdateAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            displayNametags(player, player.getWorld());
        }
    }

    private static void displayNametags(Player player, World world) {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        for (VLPlayer p : VLPlayer.getOnlinePlayers()) {
            INametagProvider.Nametag nametag = defaultProvider.provideNametag(p, world);

            ServerPlayer nmsPlayer = ((CraftPlayer) p.getPlayer()).getHandle();

            if (p.getWorld().getName().equals(world.getName())) {
                for (INametagProvider provider : providers.keySet()) {
                    if (providers.get(provider) == world) {
                        nametag = provider.provideNametag(p, world);
                    }
                }
            }

            try {
                scoreboard.removePlayerTeam(scoreboard.getPlayerTeam(nametag.getSortPriority() + p.getUniqueId().toString().split("-")[0]));
            } catch (NullPointerException ignored) {
            }

            PlayerTeam team = scoreboard.addPlayerTeam(nametag.getSortPriority() + p.getUniqueId().toString().split("-")[0]);
            team.setCollisionRule(Team.CollisionRule.NEVER);
            teams.put(p, team);
            scoreboard.addPlayerToTeam(p.getName(), team);
            p.setPlayerListName(nametag.getPrefix() + p.getDisplayName() + nametag.getSuffix());

            try {
                team.setPlayerPrefix(Component.Serializer.fromJson("{\"text\":\"" + nametag.getPrefix() + "\"}"));
                team.setPlayerSuffix(Component.Serializer.fromJson("{\"text\":\"" + nametag.getSuffix() + "\"}"));
                team.setColor(getColor(p));
                team.setCollisionRule(Team.CollisionRule.NEVER);

                ClientboundSetPlayerTeamPacket create = new ClientboundSetPlayerTeamPacket(team, 0);  // Creates the team
                ClientboundSetPlayerTeamPacket remove = new ClientboundSetPlayerTeamPacket(team, 1);  // Removes the team
                ClientboundSetPlayerTeamPacket join = new ClientboundSetPlayerTeamPacket(team, Collections.singletonList(p.getName()), 3);  // Joins the team
                ClientboundSetPlayerTeamPacket leave = new ClientboundSetPlayerTeamPacket(team, Collections.singletonList(p.getName()), 4);  // Leaves the team

                entityPlayer.connection.send(leave);
                entityPlayer.connection.send(remove);
                entityPlayer.connection.send(create);
                entityPlayer.connection.send(join);

                nmsPlayer.connection.send(leave);
                nmsPlayer.connection.send(remove);
                nmsPlayer.connection.send(create);
                nmsPlayer.connection.send(join);
            } catch (NullPointerException ignored) {
            }
        }
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            displayNametags(player, player.getWorld());
        }
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent e) {
        displayNametags(e.getPlayer(), e.getPlayer().getWorld());

        for (Player player : e.getPlayer().getWorld().getPlayers()) {
            displayNametags(player, player.getWorld());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            displayNametags(p, p.getWorld());
        }
    }
}
