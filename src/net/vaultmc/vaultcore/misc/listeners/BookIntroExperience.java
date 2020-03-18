package net.vaultmc.vaultcore.misc.listeners;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class BookIntroExperience extends ConstructorRegisterListener {
    private static final ItemStack book = VaultCore.getInstance().getData().getItemStack("book");

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPlayedBefore()) {
            e.getPlayer().openBook(book);
        }
    }
}
