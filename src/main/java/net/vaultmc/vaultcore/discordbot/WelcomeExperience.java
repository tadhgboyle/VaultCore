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

import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.OffsetDateTime;
import java.util.Collections;

public class WelcomeExperience extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        e.getMember().getUser().openPrivateChannel().queue(channel -> {
            channel.sendMessage(new MessageEmbed("", "Welcome to the VaultMC Discord!",
                    "Please review the rules in #read-me before starting to use VaultMC Discord. You must follow Discord's Term of Service and Community Guidelines when using VaultMC Discord, which means you must be at least 13. If you do not agree to these rules, please immediately stop using all the services of VaultMC. To get access to the chat channels, please link your Minecraft account with your Discord account by running /token in-game and pasting the output in #token.\n" +
                            "\n\nThank you for joining VaultMC!", EmbedType.RICH, OffsetDateTime.now(), 0xffaa00,
                    null, null, null, null, null, null, Collections.emptyList())).queue();
        });
    }
}
