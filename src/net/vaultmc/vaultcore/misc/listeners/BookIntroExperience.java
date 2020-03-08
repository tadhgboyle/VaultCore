package net.vaultmc.vaultcore.misc.listeners;

import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class BookIntroExperience extends ConstructorRegisterListener {
    private static final ItemStack book = new ItemStack(Material.WRITTEN_BOOK);

    static {
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setAuthor("VaultMC");
        meta.setPages("Welcome to VaultMC!\n\n" +
                        "VaultMC is a rather\n" +
                        "small community that is\n" +
                        "focused on being\n" +
                        "simplistic. We offer a\n" +
                        "small set of games for\n" +
                        "you to enjoy.\n\n" +
                        "1. Survival: /sv\n" +
                        "2. Creative: /cr\n" +
                        "3. Factions: /clwtp\n" +
                        "4. SkyBlock: /is",
                "To get more info, you\n" +
                        "are suggested to do a\n" +
                        "short tour via /tour.");
        book.setItemMeta(meta);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPlayedBefore()) {
            e.getPlayer().openBook(book);
        }
    }
}
