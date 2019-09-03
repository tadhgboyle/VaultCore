package me.aberdeener.vaultcore.listeners;
 
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
 
import me.aberdeener.vaultcore.VaultCore;
 
public class VaultSuiteChat implements Listener {
 
	private static String clansChatHook(Player player, String prefix, String name, String text) {
		try {
			String clan = (String) Class.forName("net.vaultmc.gameplay.clans.api.ClansAPI").getDeclaredMethod("getCurrentClan", Player.class)
					.invoke(player);
			return prefix + name + "<" + clan + ">" + ChatColor.DARK_GRAY + " → " + ChatColor.WHITE + text;
		} catch (ReflectiveOperationException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
 
    @EventHandler
    public void chatFormat(AsyncPlayerChatEvent event) {
 
        Player p = event.getPlayer();
 
        // chat variables and strings
        String groupPrefix = VaultCore.getChat().getPlayerPrefix(p);
        String prefix = ChatColor.translateAlternateColorCodes('&', groupPrefix);
        String name = p.getName();
        String text = event.getMessage();
        text = text.replace("%", "%%");
 
        // check for chatcolor perm
        if (!p.hasPermission("vc.chat.color")) {
            String message = !p.getWorld().getName().equals("clan") ? (prefix + name + ChatColor.DARK_GRAY + " → " + ChatColor.WHITE + text) : clansChatHook(p, prefix, name, text);
            event.setFormat(message);
        }
 
        else {
            String message = !p.getWorld().getName().equals("clan") ? (prefix + name + ChatColor.DARK_GRAY + " → " + ChatColor.WHITE
                    + ChatColor.translateAlternateColorCodes('&', text)) : clansChatHook(p, prefix, name, ChatColor.translateAlternateColorCodes('&', text));
            event.setFormat(message);
        }
 
        // staffchat variables
        String SCmessage = (prefix + name + ChatColor.DARK_GRAY + " » " + ChatColor.WHITE + ChatColor.AQUA
                + ChatColor.translateAlternateColorCodes('&', text));
        String SCprefix = (ChatColor.translateAlternateColorCodes('&', VaultCore.getInstance().getConfig().getString("staffchat-prefix")));
        String staffchat = SCprefix + SCmessage;
 
        // staffchat console send and players
        if (event.getMessage().charAt(0) == ',') {
            if (event.getPlayer().hasPermission("vc.sc")) {
                Bukkit.getConsoleSender().sendMessage(staffchat);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("vc.sc")) {
                        player.sendMessage(staffchat.replaceFirst(",", ""));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}