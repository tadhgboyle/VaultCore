package net.vaultmc.vaultcore.discordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.commands.Commands;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.discordbot.commands.PurgeCommand;

import javax.security.auth.login.LoginException;

public class VaultMCBot extends ListenerAdapter {
    public static void main(String[] args) {
        try {
            JDA jda = new JDABuilder(VaultCore.getInstance().getConfig().getString("token"))
                    .addEventListeners(new PurgeCommand(), new Commands())
                    .setActivity(Activity.playing("on VaultMC.net"))
                    .build();
            jda.awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
