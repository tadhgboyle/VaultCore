package net.vaultmc.vaultcore.discordbot;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;

import java.util.ArrayList;
import java.util.List;

import static net.vaultmc.vaultloader.utils.player.VLOfflinePlayer.getOfflinePlayerDiscord;

public class PlayerUpdater {
    private static final Guild guild = VaultMCBot.getGuild();
    public static Multimap<String, Role> mappedRole = HashMultimap.create();

    public static void updater() {
        if (VaultMCBot.isStarted()) {
            for (Member member : guild.getMembers()) {
                if (member.isFake() || member.isOwner()) continue;
                VLOfflinePlayer player = getOfflinePlayerDiscord(member.getIdLong());
                if (player == null) {
                    continue;
                }
                List<Role> roles = new ArrayList<>(member.getRoles());
                roles.removeIf(role -> role.getIdLong() != VaultMCBot.admin.getIdLong() && role.getIdLong() != VaultMCBot.moderator.getIdLong() &&
                        role.getIdLong() != VaultMCBot.players.getIdLong());
                String group = player.getGroup().toLowerCase();
                if (!member.getEffectiveName().equals(player.getName())) {
                    member.modifyNickname(player.getName()).queue();
                }

                if (!roles.containsAll(mappedRole.get(group)) || !mappedRole.get(group).containsAll(roles)) {  // Checking for equality
                    VaultMCBot.getGuild().removeRoleFromMember(member, VaultMCBot.admin).queue();
                    VaultMCBot.getGuild().removeRoleFromMember(member, VaultMCBot.moderator).queue();
                    VaultMCBot.getGuild().removeRoleFromMember(member, VaultMCBot.staff).queue();
                    VaultMCBot.getGuild().removeRoleFromMember(member, VaultMCBot.players).queue();
                    for (Role role : mappedRole.get(group)) {
                        guild.addRoleToMember(member, role).queue();
                    }
                }
            }
        }
    }
}
