package net.vaultmc.vaultcore.discordbot;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.discordbot.commands.PurgeCommand;
import net.vaultmc.vaultcore.discordbot.runnables.PlayerUpdater;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class VaultMCBot extends ListenerAdapter {
    @Getter
    public static JDA jda;
    @Getter
    public static Guild guild;
    @Getter
    @Setter
    public static boolean started = false;
    public static Role admin;
    public static Role moderator;
    public static Role staff;
    public static Role players;

    public static void startVaultMCBot() {
        try {
            VaultCore.getInstance().getLogger().log(Level.INFO, "VaultMC Bot starting up...");
            jda = new JDABuilder(VaultCore.getInstance().getConfig().getString("token"))
                    .addEventListeners(new TokenValidator(), new PurgeCommand())
                    .setActivity(Activity.playing("on VaultMC.net"))
                    .build();
            jda.awaitReady();
            started = true;

            guild = jda.getGuildById(615457047403560960L);
            admin = guild.getRoleById(615457221337153546L);
            moderator = guild.getRoleById(615457245551001600L);
            staff = guild.getRoleById(615671876928143537L);
            players = guild.getRoleById(615457277247488010L);

            PlayerUpdater.mappedRole.putAll("admin", Arrays.asList(admin, staff, players));
            PlayerUpdater.mappedRole.putAll("moderator", Arrays.asList(moderator, staff, players));
            PlayerUpdater.mappedRole.put("trusted", players);
            PlayerUpdater.mappedRole.put("patreon", players);
            PlayerUpdater.mappedRole.put("member", players);
            PlayerUpdater.mappedRole.put("default", players);

            VaultCore.getInstance().getLogger().log(Level.INFO, "VaultMC Bot started successfully...");

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    PlayerUpdater.updater();
                }
            }, 120000, 240000);

        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
            VaultCore.getInstance().getLogger().log(Level.INFO, "VaultMC Bot failed to start :(");
        }
    }
}
