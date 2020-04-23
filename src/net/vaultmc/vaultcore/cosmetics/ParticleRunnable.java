package net.vaultmc.vaultcore.cosmetics;

import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ParticleRunnable implements Listener {

    private static final List<ChatColor> COLORS =
            Collections.unmodifiableList(Arrays.asList(ChatColor.values()));
    private static final int SIZE = COLORS.size();
    private static final Random RANDOM = new Random();

    public static void particleHandler() {

        for (VLPlayer player : CosmeticsInvListener.particles.keySet()) {
            if (player.getWorld().getName().equalsIgnoreCase("Lobby")) {
                switch (CosmeticsInvListener.particles.get(player)) {
                    case FLAME: {
                        // Flamboyant Flame
                        Location playerLoc = player.getLocation();
                        double r = 1.5;
                        double t = 0;
                        while (t < 15) {
                            double x = (r * Math.cos(t)) / 2;
                            double y = (r * Math.sin(t)) + 1;
                            double z = (r * Math.sin(t)) / 2;
                            playerLoc.add(x, y, z);
                            playerLoc.getWorld().spawnParticle(CosmeticsInvListener.particles.get(player), playerLoc, 1, 0.001, 0.001, 0.001, 0.00001);
                            playerLoc.subtract(x, y, z);
                            playerLoc.add(-x, y, -z);
                            playerLoc.getWorld().spawnParticle(CosmeticsInvListener.particles.get(player), playerLoc, 1, 0.001, 0.001, 0.001, 0.00001);
                            playerLoc.subtract(-x, y, -z);
                            t++;
                        }
                    }
                    break;
                    case REDSTONE: {
                        // Ridiculous Rainbow
                        Location playerLoc = player.getLocation();
                        // Particle, location, # of particles, (x, y, z) offset, speed of particles
                        Color color = Color.fromBGR(RANDOM.nextInt(242), RANDOM.nextInt(255), RANDOM.nextInt(255));
                        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 2);
                        playerLoc.getWorld().spawnParticle(Particle.REDSTONE, playerLoc, 100, 0.5, 0.5, 0.5, 0.05, dustOptions);
                    }
                    break;
                    case SMOKE_LARGE: {
                        // Smoggy Smoke
                        Location playerLoc = player.getLocation();
                        // Particle, location, # of particles, (x, y, z) offset, speed of particles
                        // Make this curl upwards like dna
                        playerLoc.getWorld().spawnParticle(Particle.SMOKE_LARGE, playerLoc, 30, 0.5, 0.5, 0.5, 0.001);
                    }
                    default:
                        continue;
                }
            }
        }
    }
}
