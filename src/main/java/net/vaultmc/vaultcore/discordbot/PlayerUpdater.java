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

package net.vaultmc.vaultcore.discordbot;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerUpdater extends ConstructorRegisterListener {
    private static final Guild guild = VaultMCBot.getGuild();
    public static Multimap<String, Role> mappedRole = HashMultimap.create();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (VaultMCBot.isStarted()) {
            VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
            if (player.getDiscord() == 0) {
                return;
            }
            Member member = guild.getMemberById(player.getDiscord());
            List<Role> roles = new ArrayList<>(member.getRoles());
            roles.removeIf(role -> role.getIdLong() != VaultMCBot.admin.getIdLong() && role.getIdLong() != VaultMCBot.moderator.getIdLong() &&
                    role.getIdLong() != VaultMCBot.staff.getIdLong() && role.getIdLong() != VaultMCBot.players.getIdLong() && role.getIdLong() != VaultMCBot.overlord.getIdLong() &&
                    role.getIdLong() != VaultMCBot.helper.getIdLong() && role.getIdLong() != VaultMCBot.lord.getIdLong() && role.getIdLong() != VaultMCBot.god.getIdLong() &&
                    role.getIdLong() != VaultMCBot.titan.getIdLong() && role.getIdLong() != VaultMCBot.hero.getIdLong());
            String group = player.getGroup().toLowerCase();
            if (!member.getEffectiveName().equals(player.getName())) {
                member.modifyNickname(player.getName()).queue();
            }

            if (!roles.containsAll(mappedRole.get(group)) || !mappedRole.get(group).containsAll(roles)) {  // Checking for equality
                VaultMCBot.getGuild().removeRoleFromMember(member, VaultMCBot.admin).queue(v -> {
                    VaultMCBot.getGuild().removeRoleFromMember(member, VaultMCBot.moderator).queue(a -> VaultMCBot.getGuild().removeRoleFromMember(member, VaultMCBot.helper).queue(b -> VaultMCBot.getGuild().removeRoleFromMember(member, VaultMCBot.staff).queue(c -> VaultMCBot.getGuild().removeRoleFromMember(member, VaultMCBot.players).queue(d -> {
                        for (Role role : mappedRole.get(group)) {
                            VaultMCBot.getGuild().addRoleToMember(member, role).queue();
                        }
                    }))));
                });
            }
        }
    }
}
