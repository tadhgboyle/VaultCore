package net.vaultmc.vaultcore.discordbot;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static net.vaultmc.vaultloader.utils.player.VLOfflinePlayer.getOfflinePlayerDiscord;

public class PlayerUpdater {
    public static Multimap<String, Role> mappedRole = HashMultimap.create();
    private static Guild guild = VaultMCBot.getGuild();

    public static void updater() {
        if (VaultMCBot.isStarted()) {
            for (Member member : guild.getMembers()) {
                if (member.isFake() || member.isOwner()) continue;
                VLOfflinePlayer player = getOfflinePlayerDiscord(member.getIdLong());
                if (player == null) {
                    continue;
                }
                List<Role> roles = new ArrayList<>(member.getRoles());
                roles.remove(VaultMCBot.betaTester);
                String group = player.getGroup().toLowerCase();
                try (ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT username FROM players WHERE uuid=?", player.getUniqueId().toString())) {
                    if (rs.next()) {
                        String name = rs.getString("username");
                        if (!member.getEffectiveName().equals(name)) {
                            member.modifyNickname(name).queue();
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
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
