package net.vaultmc.vaultcore.combat;

import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatLog extends ConstructorRegisterListener {
    private static final Map<UUID, TagInfo> inCombat = new HashMap<>();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (inCombat.containsKey(e.getPlayer().getUniqueId())) {
            e.getPlayer().setHealth(0);
            if (inCombat.get(e.getPlayer().getUniqueId()) != null)
                inCombat.get(e.getPlayer().getUniqueId()).getTask().cancel();
            inCombat.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerDead(PlayerDeathEvent e) {
        if (inCombat.get(e.getEntity().getUniqueId()) != null)
            inCombat.get(e.getEntity().getUniqueId()).getTask().cancel();
        inCombat.remove(e.getEntity().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamagedEntity(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;

        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            if (inCombat.containsKey(e.getEntity().getUniqueId())) {
                if (inCombat.get(e.getEntity().getUniqueId()).getAttacker() == e.getDamager().getUniqueId()) {
                    inCombat.get(e.getEntity().getUniqueId()).getTask().cancel();
                    inCombat.get(e.getEntity().getUniqueId()).setTask(Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
                        ((Player) e.getEntity()).sendActionBar(VaultLoader.getMessage("combat-log.out"));
                        inCombat.remove(e.getEntity().getUniqueId());
                    }, 18000L));
                    return;
                } else {
                    inCombat.get(e.getEntity().getUniqueId()).getTask().cancel();
                }
            }
            ((Player) e.getEntity()).sendActionBar(VaultLoader.getMessage("combat-log.tagged").replace("{PLAYER}",
                    VLPlayer.getPlayer((Player) e.getDamager()).getFormattedName()));
            ((Player) e.getDamager()).sendActionBar(VaultLoader.getMessage("combat-log.tagged").replace("{PLAYER}",
                    VLPlayer.getPlayer((Player) e.getEntity()).getFormattedName()));
            inCombat.put(e.getEntity().getUniqueId(), new TagInfo(e.getDamager().getUniqueId(), Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
                ((Player) e.getEntity()).sendActionBar(VaultLoader.getMessage("combat-log.out"));
                inCombat.remove(e.getEntity().getUniqueId());
            }, 18000L)));
        }
    }
}
