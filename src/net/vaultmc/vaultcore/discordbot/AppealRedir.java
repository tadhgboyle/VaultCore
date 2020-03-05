package net.vaultmc.vaultcore.discordbot;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AppealRedir extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getGuild() == VaultMCBot.getGuild() && e.getChannel().getName().endsWith("appeal")) {
            e.getMessage().delete().queue();
            String content = e.getMessage().getContentStripped();
            ((TextChannel) e.getGuild().getGuildChannelById(ChannelType.TEXT, 68501591577945702L)).sendMessage("Message in #appeal sent by " +
                    e.getAuthor().getAsMention() + ": \n\n" + content);
        }
    }
}
