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

package net.vaultmc.vaultcore.forge;

import com.comphenix.protocol.utility.StreamSerializer;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.SneakyThrows;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ForgeModsListener extends ConstructorRegisterListener implements PluginMessageListener {
    @Getter
    private static final Multimap<UUID, String> mods = HashMultimap.create();
    private static final byte[] forceHandshake;

    static {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream opt = new DataOutputStream(bos);
        StreamSerializer s = StreamSerializer.getDefault();
        try {
            s.serializeVarInt(opt, 1);
            s.serializeVarInt(opt, 0);
            s.serializeVarInt(opt, 0);
            s.serializeVarInt(opt, 0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        forceHandshake = bos.toByteArray();
    }

    @EventHandler
    public void onPlayerRegisterChannel(PlayerRegisterChannelEvent e) {
        if (e.getChannel().equalsIgnoreCase("fml:handshake")) {
            System.out.println("Found forge client: " + e.getPlayer().getName());
            e.getPlayer().sendPluginMessage(VaultLoader.getInstance(), "fml:handshake", forceHandshake);
        }
    }

    @Override
    @SneakyThrows
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        Bukkit.getConsoleSender().sendMessage("Received " + Arrays.toString(message));

        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(message));
        try {
            StreamSerializer s = StreamSerializer.getDefault();
            int discriminator = s.deserializeVarInt(stream);
            if (discriminator != 2)
                return;
            int count = s.deserializeVarInt(stream);
            List<String> mods = new ArrayList<>();
            for (int i = 2; i < message.length; ) {
                int end = i + message[i] + 1;
                byte[] range = Arrays.copyOfRange(message, i + 1, end);
                mods.add(new String(range));
                i = end;
            }
            mods = mods.subList(0, count);
            ForgeModsListener.mods.putAll(player.getUniqueId(), mods);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
