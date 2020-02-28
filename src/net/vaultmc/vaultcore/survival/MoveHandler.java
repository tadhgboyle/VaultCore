package net.vaultmc.vaultcore.survival;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveHandler extends ConstructorRegisterListener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        double x = e.getTo().getX();
        double z = e.getTo().getZ();
        if (!e.getPlayer().hasPermission(Permissions.Explore)) {
            if (x >= 50000 || x <= -50000 || z >= 50000 || z <= -50000) {
                TextComponent component = new TextComponent(VaultLoader.getMessage("cannot-explore"));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(VaultLoader.getMessage("cannot-explore-why"))}));
                e.getPlayer().sendMessage(component);
            }
        }
    }
}
