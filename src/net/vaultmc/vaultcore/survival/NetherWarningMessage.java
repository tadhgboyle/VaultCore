package net.vaultmc.vaultcore.survival;

import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class NetherWarningMessage extends ConstructorRegisterListener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase("survival_nether")) {
            e.getPlayer().sendMessage(VaultLoader.getMessage("vaultcore.survival.nether_warning"));
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase("survival_nether")) {
            e.getPlayer().sendMessage(VaultLoader.getMessage("vaultcore.survival.nether_warning"));
        }
    }
}
