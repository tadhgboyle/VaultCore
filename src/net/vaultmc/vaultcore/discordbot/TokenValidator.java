package net.vaultmc.vaultcore.discordbot;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.discordbot.runnables.PlayerUpdater;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public class TokenValidator extends ListenerAdapter {
    private Logger logger = VaultCore.getInstance().getLogger();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        Member member = event.getMember();
        Message msg = event.getMessage();
        String message = event.getMessage().getContentStripped();
        MessageChannel channel = event.getChannel();

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
        try (ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT uuid, username FROM players WHERE token=?", message)) {
            if (rs.next()) {
                if (isDuplicate(message)) {
                    channel.sendMessage(member.getAsMention() + ": That token is already used.").queue();
                    msg.delete().queue();
                    return;
                }

                VLOfflinePlayer player = VLOfflinePlayer.getOfflinePlayer(UUID.fromString(rs.getString("uuid")));
                if (player == null) {
                    channel.sendMessage(member.getAsMention() + ": Failed to obtain player information from UUID. Please contact an administrator for help.").queue();
                    msg.delete().queue();
                    return;
                }

                msg.delete().queue();
                member.modifyNickname(rs.getString("username")).queue();
                for (Role role : PlayerUpdater.mappedRole.get(player.getGroup())) {
                    VaultMCBot.getGuild().addRoleToMember(member, role).queue();
                }
                logger.info("Successfully linked Minecraft account of player: " + rs.getString("username"));
                VaultMCBot.getGuild().getTextChannelById("618221832801353728").sendMessage(member.getAsMention() + " Welcome to the Guild! Your nickname has been set to: `" + rs.getString("username") + "`.").queue();
                VaultCore.getDatabase().executeUpdateStatement("UPDATE players SET discord_id = ? WHERE token = ?", member.getId(), message);
            } else {
                channel.sendMessage(member.getAsMention() + ": The token is invalid. If you believe this information is wrong, please contact an administrator for help.").queue();
                msg.delete().queue();
            }
        } catch (SQLException ex) {
            channel.sendMessage(member.getAsMention() + ": Failed to validate your token. Please contact an administrator for help. (SQLException: " + ex.getMessage() + ")").queue();
        }
    }

    private boolean isDuplicate(String token) {
        try {
            ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT discord_id FROM players WHERE token = ?", token);
            if (rs.next()) {
                return rs.getString("discord_id") != null && !rs.getString("discord_id").isEmpty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
