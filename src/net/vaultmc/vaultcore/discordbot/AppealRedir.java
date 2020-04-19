package net.vaultmc.vaultcore.discordbot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AppealRedir extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        try {
            if (e.getGuild() == VaultMCBot.getGuild() && e.getChannel().getName().endsWith("appeal")) {
                e.getMessage().delete().queue();
                e.getGuild().getTextChannelById("685015915779457024").sendMessage("Message in #appeal sent by " +
                        e.getAuthor().getAsMention()).queue();
                e.getGuild().getTextChannelById("685015915779457024").sendMessage(e.getMessage()).queue();
            }
        } catch (IllegalStateException ignored) {
            // Thrown when seclog reset message is sent
        }
    }
}
