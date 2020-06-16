/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

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
