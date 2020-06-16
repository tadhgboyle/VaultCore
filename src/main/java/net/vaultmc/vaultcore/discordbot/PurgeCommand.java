/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

package net.vaultmc.vaultcore.discordbot;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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

        if (msg.contains("!purge")) {
            if (member.getRoles().toString().contains("staff")) {
                if (args.length == 2 && !args[1].equals("1")) {
                    try {
                        event.getTextChannel().deleteMessages(event.getChannel().getHistory().retrievePast(Integer.parseInt(args[1])).complete()).queue();
                        message.delete().queue();
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
