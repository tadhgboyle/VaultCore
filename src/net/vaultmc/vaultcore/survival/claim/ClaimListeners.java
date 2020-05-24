package net.vaultmc.vaultcore.survival.claim;

import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class ClaimListeners extends ConstructorRegisterListener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.OFF_HAND) return;
        if (!e.getPlayer().getWorld().getName().contains("Survival")) return;
        if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
        Claim claim = Claim.getClaims().get(e.getClickedBlock() != null ? e.getClickedBlock().getChunk().getChunkKey() :
                e.getPlayer().getChunk().getChunkKey());
        if (claim != null && !e.getPlayer().getUniqueId().toString().equals(claim.owner.getUniqueId().toString()) &&
                !claim.owner.getDataConfig().getStringList("claim-allowed-players").contains(e.getPlayer().getUniqueId().toString())) {
            e.getPlayer().sendMessage(VaultLoader.getMessage("vaultcore.commands.claim.cannot-interact"));
            e.setCancelled(true);
        }
    }
}
