package net.vaultmc.vaultcore.discordbot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.vaultmc.vaultcore.VaultCore;

public class PurgeCommand extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        Message message = event.getMessage();
        String msg = event.getMessage().getContentStripped();
        Member member = event.getMember();

        if (message.getAuthor().isBot()) {
            return;
        }

        message.delete().queue();
        if (msg.contains("!purge")) {
            if (member.getRoles().toString().contains("staff")) {
                if (args.length == 2 && !args[1].equals("1")) {
                    try {
                        VaultCore.getInstance().getLogger().info(member + " purged " + args[1] + " messages from the " + event.getTextChannel().getName() + " channel");
                        event.getTextChannel().deleteMessages(event.getChannel().getHistory().retrievePast(Integer.parseInt(args[1] + 1)).complete()).queue();
                    } catch (NumberFormatException e) {
                        event.getTextChannel().sendMessage(member.getAsMention() + " you must supply a number of messages to purge. (Minimum 2)").queue();
                    }
                } else {
                    event.getTextChannel().sendMessage(member.getAsMention() + " you must supply a number of messages to purge. (Minimum 2)").queue();
                }
            } else {
                event.getTextChannel().sendMessage(member.getAsMention() + " you can't use this command!").queue();
            }
        }
    }
}
