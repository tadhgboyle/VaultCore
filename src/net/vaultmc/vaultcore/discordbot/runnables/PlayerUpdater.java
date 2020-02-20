package net.vaultmc.vaultcore.discordbot.runnables;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.vaultmc.vaultcore.discordbot.VaultMCBot;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;

import java.util.List;

public class PlayerUpdater {

    private static Guild guild = VaultMCBot.getGuild();

    public static void updater() {

        for (Member member : guild.getMembers()) {

            Role adminRole = member.getGuild().getRoleById(615457221337153546L);
            Role moderatorRole = member.getGuild().getRoleById(615457245551001600L);
            Role staffRole = member.getGuild().getRoleById(615671876928143537L);

            VLOfflinePlayer player = VLOfflinePlayer.getOfflinePlayerDiscord(member.getIdLong());

            List currentRoles = member.getRoles();
            String currentNickname = member.getEffectiveName();

            String minecraftRole = player.getGroup().toLowerCase();
            String minecraftName = player.getName();

            // so it wont try to update me
            if (member.isOwner()) continue;

            // if they have no roles ignore
            if (currentRoles.isEmpty()) continue;

            // if both are same
            if (currentNickname.equals(minecraftName) && currentRoles.contains(minecraftRole)) {
                // just a check - chance that they have staff roles still
                if (!isStaff(player, minecraftRole)) {
                    guild.removeRoleFromMember(member, staffRole);
                    guild.removeRoleFromMember(member, adminRole);
                    guild.removeRoleFromMember(member, moderatorRole);
                }
            }

            // if both are different
            if (!currentNickname.equals(minecraftName) && !currentRoles.contains(minecraftRole)) {
                member.modifyNickname(minecraftName).queue();
                setRole(member, minecraftRole);
            }

            // if role is different
            if (currentNickname.equals(minecraftName) && !currentRoles.contains(minecraftRole)) {
                setRole(member, minecraftRole);
                continue;
            }

            // if nickname is different
            if (!currentNickname.equals(minecraftName) && currentRoles.contains(minecraftRole)) {
                member.modifyNickname(minecraftName).queue();
                // just a check - chance that they have staff roles still
                if (!isStaff(player, minecraftRole)) {
                    guild.removeRoleFromMember(member, staffRole);
                    guild.removeRoleFromMember(member, adminRole);
                    guild.removeRoleFromMember(member, moderatorRole);
                }
                continue;
            }
        }
    }

    private static boolean isStaff(VLOfflinePlayer player, String minecraftRole) {
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
