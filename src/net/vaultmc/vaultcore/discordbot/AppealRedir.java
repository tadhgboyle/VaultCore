package net.vaultmc.vaultcore.discordbot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AppealRedir extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getGuild() == VaultMCBot.getGuild() && e.getChannel().getName().endsWith("appeal")) {
            e.getMessage().delete().queue();
            String content = e.getMessage().getContentStripped();
            e.getGuild().getTextChannelById("685015915779457024").sendMessage("Message in #appeal sent by " +
                    e.getAuthor().getAsMention() + ": \n\n" + content).queue();
        }
    }
}
