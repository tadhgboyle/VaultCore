package net.vaultmc.vaultcore.punishments;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;
import java.util.HashMap;

@RootCommand(
        literal = "punish",
        description = "Punish a player through a nice looking GUI"
)
@Permission(Permissions.PunishCommand)
public class PunishCommand extends CommandExecutor {

    public static HashMap<Player, Player> punishes = new HashMap<>();

    public PunishCommand() {
        unregisterExisting();
        register("punish", Collections.emptyList());

    }
    @SubCommand("punish")
    public void punish(VLPlayer player) {

        Inventory players = Bukkit.createInventory(null, 27, "Players");
        for(Player all : Bukkit.getOnlinePlayers()) {
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1, (byte) 3);

            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(all.getName());
            meta.setDisplayName(VLPlayer.getPlayer(all).getFormattedName());

            skull.setItemMeta(meta);

            players.addItem(skull);
        }

        player.openInventory(players);

    }

}
