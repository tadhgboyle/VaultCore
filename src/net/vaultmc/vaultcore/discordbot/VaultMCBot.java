package net.vaultmc.vaultcore.discordbot;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.discordbot.commands.PurgeCommand;
import net.vaultmc.vaultcore.discordbot.runnables.PlayerUpdater;

import javax.security.auth.login.LoginException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class VaultMCBot extends ListenerAdapter {

    @Getter
    public static Guild guild;

    public static void startVaultMCBot() {
        try {
            VaultCore.getInstance().getLogger().log(Level.INFO, "VaultMC Bot starting up...");
            JDA jda = new JDABuilder(VaultCore.getInstance().getConfig().getString("token"))
                    .addEventListeners(new TokenValidator(), new PurgeCommand())
                    .setActivity(Activity.playing("on VaultMC.net"))
                    .build();
            jda.awaitReady();

            guild = jda.getGuildById("615457047403560960");

            VaultCore.getInstance().getLogger().log(Level.INFO, "VaultMC Bot started successfully...");

            long minute = 1200L;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    PlayerUpdater.updater();
                }
            }, minute, minute * 2);

        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
            VaultCore.getInstance().getLogger().log(Level.INFO, "VaultMC Bot failed to start :(");
        }
    }
}
