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

package net.vaultmc.vaultcore.disguise;

import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.nametags.Nametags;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;

@RootCommand(
        literal = "undisguise",
        description = "Removes the disguise on you."
)
@Permission(Permissions.DisguiseCommand)
@PlayerOnly
public class UndisguiseCommand extends CommandExecutor {
    public UndisguiseCommand() {
        register("undisguise", Collections.emptyList());
    }

    public static void undisguise(VLPlayer player) {
        DisguiseCommand.getDisguisedPlayers().remove(player.getUniqueId());
        DisguiseCommand.getDisguisedDN().remove(player.getUniqueId());
        ((CraftPlayer) player.getPlayer()).getHandle().setProfile(DisguiseCommand.getActualGameProfile().remove(player.getUniqueId()));
        player.getPlayer().setDisplayName(DisguiseCommand.getActualDN().remove(player.getUniqueId()));
        ClientboundPlayerInfoPacket tabRemove = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER,
                ((CraftPlayer) player.getPlayer()).getHandle());
        ClientboundPlayerInfoPacket tabAdd = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER,
                ((CraftPlayer) player.getPlayer()).getHandle());
        ClientboundRemoveEntitiesPacket remove = new ClientboundRemoveEntitiesPacket(player.getPlayer().getEntityId());
        ClientboundAddPlayerPacket add = new ClientboundAddPlayerPacket(((CraftPlayer) player.getPlayer()).getHandle());
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getUniqueId().toString().equals(player.getPlayer().getUniqueId().toString())) {
                continue;
            }
            ((CraftPlayer) p).getHandle().connection.send(tabRemove);
            ((CraftPlayer) p).getHandle().connection.send(tabAdd);
            ((CraftPlayer) p).getHandle().connection.send(remove);
            ((CraftPlayer) p).getHandle().connection.send(add);
        }
        Nametags.forceUpdateAll();
    }

    @SubCommand("undisguise")
    public void undisguiseCommand(VLPlayer sender) {
        if (!DisguiseCommand.getDisguisedPlayers().containsKey(sender.getUniqueId())) {
            sender.sendMessageByKey("vaultcore.commands.disguise.not-disguised");
            return;
        }
        undisguise(sender);
        sender.sendMessageByKey("vaultcore.commands.disguise.undisguised");
    }
}
