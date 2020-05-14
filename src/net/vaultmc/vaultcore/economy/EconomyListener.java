package net.vaultmc.vaultcore.economy;

import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.concurrent.ThreadLocalRandom;

public class EconomyListener extends ConstructorRegisterListener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null && (e.getEntity().getWorld().getName().startsWith("skyblock") || e.getEntity().getWorld().getName().startsWith("survival"))) {
            double money = ThreadLocalRandom.current().nextInt(10, 30);
            VLPlayer player = VLPlayer.getPlayer(e.getEntity().getKiller());
            player.sendMessage(ChatColor.GOLD + "+$" + money);
            player.deposit(player.getWorld(), money);
        }
    }
}
