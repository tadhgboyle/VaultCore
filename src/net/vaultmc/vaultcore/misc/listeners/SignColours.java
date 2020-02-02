package net.vaultmc.vaultcore.misc.listeners;

import net.vaultmc.vaultcore.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignColours implements Listener {
    @EventHandler
    public void signChange(SignChangeEvent e) {
        if (!e.getPlayer().hasPermission(Permissions.ChatColor)) return;
        String[] lines = e.getLines();
        for (int n = 0; n <= 3; n++)
            e.setLine(n, ChatColor.translateAlternateColorCodes('&', lines[n]));
    }
}