package net.vaultmc.vaultcore.discordbot.runnables;

import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.discordbot.VaultMCBot;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;

import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerUpdater {

    private static Guild guild = VaultMCBot.getGuild();
    private static Logger logger = VaultCore.getInstance().getLogger();

    public static void updater() {

        for (Member member : guild.getMembers()) {

            Role adminRole = member.getGuild().getRoleById(615457221337153546L);
            Role moderatorRole = member.getGuild().getRoleById(615457245551001600L);
            Role staffRole = member.getGuild().getRoleById(615671876928143537L);

            if (member.isFake()) continue;

            // so it wont try to update me
            if (member.isOwner()) continue;

            VLOfflinePlayer player = getOfflinePlayerDiscord(member.getIdLong());

            if (player == null) {
                logger.log(Level.INFO, "[VaultMC Bot] " + member.getEffectiveName() + " has not linked their Minecraft account.");
                continue;
            }

            List currentRoles = member.getRoles();
            String currentNickname = member.getEffectiveName();

            String minecraftRole = player.getGroup().toLowerCase();
            String minecraftName = player.getName();

            // if they have no roles ignore
            if (currentRoles.isEmpty()) continue;

            // if both are same
            if (currentNickname.equals(minecraftName) && currentRoles.contains(minecraftRole)) {
                // just a check - chance that they have staff roles still
                if (!isStaff(minecraftRole)) {
                    guild.removeRoleFromMember(member, staffRole);
                    guild.removeRoleFromMember(member, adminRole);
                    guild.removeRoleFromMember(member, moderatorRole);
                }
                logger.log(Level.INFO, "[VaultMC Bot] " + member.getEffectiveName() + " is up to date.");
                continue;
            }

            // if both are different
            boolean nonDiscordRank = minecraftRole.equals("member") || minecraftRole.equals("patreon") || minecraftRole.equals("trusted");
            if (!currentNickname.equals(minecraftName) && !currentRoles.contains(minecraftRole)) {
                if (nonDiscordRank) continue;
                member.modifyNickname(minecraftName).queue();
                setRole(member, minecraftRole);
                logger.log(Level.INFO, "[VaultMC Bot] Updated " + member.getEffectiveName() + "'s role to " + minecraftRole);
                logger.log(Level.INFO, "[VaultMC Bot] Updated " + member.getEffectiveName() + "'s nickname to " + minecraftName);
                continue;
            }

            // if role is different
            if (currentNickname.equals(minecraftName) && !currentRoles.contains(minecraftRole)) {
                if (nonDiscordRank) continue;
                setRole(member, minecraftRole);
                logger.log(Level.INFO, "[VaultMC Bot] Updated " + member.getEffectiveName() + "'s role to " + minecraftRole);
                continue;
            }

            // if nickname is different
            if (!currentNickname.equals(minecraftName) && currentRoles.contains(minecraftRole)) {
                member.modifyNickname(minecraftName).queue();
                // just a check - chance that they have staff roles still
                if (!isStaff(minecraftRole)) {
                    guild.removeRoleFromMember(member, staffRole);
                    guild.removeRoleFromMember(member, adminRole);
                    guild.removeRoleFromMember(member, moderatorRole);
                }
                logger.log(Level.INFO, "[VaultMC Bot] Updated " + member.getEffectiveName() + "'s nickname to " + minecraftName);
                continue;
            }
        }
    }

    @SneakyThrows
    public static VLOfflinePlayer getOfflinePlayerDiscord(long id) {
        ResultSet rs = VaultLoader.getUDatabase().executeQueryStatement("SELECT uuid FROM players WHERE discord_id = ?", id);
        if (rs.next()) {
            UUID uuid = UUID.fromString(rs.getString("uuid"));
            VLOfflinePlayer player = VLOfflinePlayer.getOfflinePlayer(uuid);
            return player;
        } else {
            return null;
        }
    }

    private static boolean isStaff(String minecraftRole) {
        switch (minecraftRole) {
            case "admin":
            case "moderator":
                return true;
            default:
                return false;
        }
    }

    private static void setRole(Member member, String minecraftRole) {
        Role adminRole = member.getGuild().getRoleById(615457221337153546L);
        Role moderatorRole = member.getGuild().getRoleById(615457245551001600L);
        Role staffRole = member.getGuild().getRoleById(615671876928143537L);
        Role playersRole = member.getGuild().getRoleById(615457277247488010L);

        switch (minecraftRole) {
            case "admin":
                guild.addRoleToMember(member, adminRole);
                guild.addRoleToMember(member, staffRole);
                break;
            case "moderator":
                guild.addRoleToMember(member, moderatorRole);
                guild.addRoleToMember(member, staffRole);
                break;
            default:
                // this shouldnt need to happen but might
                guild.addRoleToMember(member, playersRole);
                break;
        }
        return;
    }
}
