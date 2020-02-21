package net.vaultmc.vaultcore.discordbot.runnables;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.discordbot.VaultMCBot;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import static net.vaultmc.vaultloader.utils.player.VLOfflinePlayer.getOfflinePlayerDiscord;

public class PlayerUpdater {
    private static Guild guild = VaultMCBot.getGuild();
    private static Logger logger = VaultCore.getInstance().getLogger();

    public static Multimap<String, Role> mappedRole = HashMultimap.create();

    public static void updater() {
        for (Member member : guild.getMembers()) {
            if (member.isFake() || member.isOwner()) continue;
            VLOfflinePlayer player = getOfflinePlayerDiscord(member.getIdLong());
            if (player == null) {
                logger.info("Bot: Can't find offline player for " + member.getEffectiveName() + " (" + member.getIdLong() + ")");
                continue;
            }
            List<Role> roles = member.getRoles();
            String group = player.getGroup().toLowerCase();
            try (ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT username FROM players WHERE uuid=?", player.getUniqueId().toString())) {
                if (rs.next()) {
                    String name = rs.getString("username");
                    if (!member.getEffectiveName().equals(name)) {
                        member.modifyNickname(name).queue();
                        logger.info("Bot: Updated nick name for " + member.getEffectiveName() + " (" + member.getIdLong() + ")");
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
                logger.info("Bot: Updated role for " + member.getEffectiveName() + " (" + member.getIdLong() + ")");
            }
        }
    }
}
