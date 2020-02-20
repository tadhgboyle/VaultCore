package net.vaultmc.vaultcore.discordbot;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TokenValidator extends ListenerAdapter {

    public Role adminRole;
    public Role moderatorRole;
    public Role staffRole;
    public Role playersRole;
    private Logger logger = VaultCore.getInstance().getLogger();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        User user = event.getAuthor();
        Member member = event.getMember();
        Message msg = event.getMessage();
        String message = event.getMessage().getContentStripped();
        MessageChannel channel = event.getChannel();

        adminRole = member.getGuild().getRoleById(615457221337153546L);
        moderatorRole = member.getGuild().getRoleById(615457245551001600L);
        staffRole = member.getGuild().getRoleById(615671876928143537L);
        playersRole = member.getGuild().getRoleById(615457277247488010L);

        if (event.isFromType(ChannelType.TEXT)) {
            if (user.isBot()) {
                return;
            }
            if (channel.getId().equalsIgnoreCase("643313973592195093")) {
                validateToken(message, member, channel, msg);
            }
        }
    }

    private void validateToken(String message, Member member, MessageChannel channel, Message msg) {

        try {
            ResultSet select_rs = VaultCore.getDatabase().executeQueryStatement("SELECT username, role FROM web_accounts WHERE token = ?", message);

            if (select_rs.next()) {

                VLOfflinePlayer player = VLOfflinePlayer.getOfflinePlayerDiscord(member.getIdLong());
                String nickname = player.getName();
                member.modifyNickname(nickname).queue();

                Role primaryRole = null;
                Role secondaryRole = null;
                switch (player.getGroup()) {
                    case "admin":
                        primaryRole = adminRole;
                        secondaryRole = staffRole;
                        break;
                    case "moderator":
                        primaryRole = moderatorRole;
                        secondaryRole = staffRole;
                        break;
                    default:
                        primaryRole = playersRole;
                        secondaryRole = null;
                        break;
                }
                member.getGuild().addRoleToMember(member, primaryRole).queue();
                if (secondaryRole != null) {
                    member.getGuild().addRoleToMember(member, secondaryRole).queue();
                }
                logger.log(Level.INFO, member + " entered valid token. Nickname set to: " + nickname);
                msg.delete().queue();
                member.getGuild().getTextChannelById("618221832801353728").sendMessage(member.getAsMention() + " Welcome to the Guild! Your nickname has been set to: `" + nickname + "`.").queue();

                VaultCore.getDatabase().executeUpdateStatement("UPDATE players SET discord_id = ? WHERE token = ?", member.getId(), message);

                // invalid token
            } else {
                channel.sendMessage(member.getAsMention() + ", that token is invalid. If you need help, ask a staff member!").queue();
                msg.delete().queue();
                logger.log(Level.INFO, member + " entered invalid token.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}