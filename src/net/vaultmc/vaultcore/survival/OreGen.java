package net.vaultmc.vaultcore.survival;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import lombok.SneakyThrows;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.concurrent.ThreadLocalRandom;

public class OreGen extends ConstructorRegisterListener {
    @EventHandler
    @SneakyThrows
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getPlayer().getWorld().getName().toLowerCase().contains("survival") || e.getPlayer().getWorld().getName().contains("clans")) {
            if (e.getFrom().getChunk() != e.getTo().getChunk()) {
                VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
                if (player.isNew() || player.isQuality() || player.getName().equals("yangyang200") /* Testing */) {
                    int rand = ThreadLocalRandom.current().nextInt(0, 100);
                    if (rand <= 15) {
                        try (EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(e.getPlayer().getWorld()), -1,
                                BukkitAdapter.adapt(e.getPlayer()))) {
                            ParserContext context = new ParserContext();
                            session.replaceBlocks(new CuboidRegion(BlockVector3.at(e.getPlayer().getLocation().getX() - 5,
                                    e.getPlayer().getLocation().getY() - 5, e.getPlayer().getLocation().getZ() - 5),
                                    BlockVector3.at(e.getPlayer().getLocation().getX() + 5, e.getPlayer().getLocation().getY() + 5,
                                            e.getPlayer().getLocation().getZ() + 5)), WorldEdit.getInstance().getMaskFactory().parseFromInput("stone",
                                    context), WorldEdit.getInstance().getPatternFactory().parseFromInput("1%diamond_ore,99%stone", context));
                        }
                    }
                }
            }
        }
    }
}
