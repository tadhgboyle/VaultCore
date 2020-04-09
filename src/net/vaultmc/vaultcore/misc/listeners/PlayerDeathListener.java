package net.vaultmc.vaultcore.misc.listeners;

import net.vaultmc.vaultcore.misc.commands.SuicideCommand;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener extends ConstructorRegisterListener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (SuicideCommand.getSuicidalPlayers().containsKey(VLPlayer.getPlayer(e.getEntity()))) {
            // TODO: The hashmap should find that the player who died is in it, so the vanilla "Aberdeener died" message should be set to null, and instead be the custom one, but this doesnt seem to work.
            e.setDeathMessage(null);
            SuicideCommand.getSuicidalPlayers().remove(VLPlayer.getPlayer(e.getEntity()));
        }
    }
}
