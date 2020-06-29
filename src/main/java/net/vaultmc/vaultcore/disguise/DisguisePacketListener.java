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

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import net.vaultmc.vaultloader.VaultLoader;

import java.util.ArrayList;
import java.util.List;

public class DisguisePacketListener extends PacketAdapter {
    public DisguisePacketListener() {
        super(VaultLoader.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.PLAYER_INFO);
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    @Override
    public void onPacketSending(PacketEvent e) {
        if (e.getPacketType() == PacketType.Play.Server.PLAYER_INFO) {
            PacketContainer packet = e.getPacket();
            List<PlayerInfoData> data = packet.getPlayerInfoDataLists().read(0);
            List<PlayerInfoData> newData = new ArrayList<>();
            for (PlayerInfoData d : data) {
                if (DisguiseCommand.getDisguisedPlayers().containsKey(d.getProfile().getUUID())) {
                    PlayerInfoData pid = new PlayerInfoData(WrappedGameProfile.fromHandle(DisguiseCommand.getDisguisedPlayers().get(d.getProfile().getUUID())), d.getLatency(), d.getGameMode(), WrappedChatComponent.fromText(DisguiseCommand.getDisguisedDN().get(d.getProfile().getUUID())));
                    newData.add(pid);
                } else {
                    newData.add(d);
                }
            }
            packet.getPlayerInfoDataLists().write(0, newData);
        }
    }
}
