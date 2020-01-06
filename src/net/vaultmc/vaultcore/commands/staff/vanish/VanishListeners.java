package net.vaultmc.vaultcore.commands.staff.vanish;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultutils.utils.ConstructorRegisterListener;

public class VanishListeners extends ConstructorRegisterListener {
	
	String string = Utilities.string;
	
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!e.getJoinMessage().startsWith("[VANISH_FAKE_JOIN]")) {
            VanishCommand.update(e.getPlayer());
            if (VanishCommand.vanished.getOrDefault(e.getPlayer().getUniqueId(), false)) {
                VanishCommand.setVanishState(e.getPlayer(), true);
                e.getPlayer().sendMessage(string + "You are still invisible!");
                e.setJoinMessage(null);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        VanishCommand.update(e.getPlayer());
        if (VanishCommand.vanished.getOrDefault(e.getPlayer().getUniqueId(), false)) {
            VanishCommand.setVanishState(e.getPlayer(), true);
        }
    }
}
