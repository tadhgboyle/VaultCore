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

package net.vaultmc.vaultcore.misc.listeners;

import lombok.Getter;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.apache.commons.lang.RandomStringUtils;

import java.util.HashMap;
import java.util.UUID;

public class SessionHandler {

    @Getter
    private static final HashMap<UUID, String> session_ids = new HashMap<>();

    @Getter
    private static final HashMap<String, Long> session_duration = new HashMap<>();

    public void newSession(VLPlayer player) {
        UUID uuid = player.getUniqueId();
        String username = player.getName();

        String ip = player.getAddress().getAddress().getHostAddress();
        String session_id = RandomStringUtils.random(8, true, true);
        long start_time = System.currentTimeMillis();

        session_ids.put(uuid, session_id);
        session_duration.put(session_id, System.currentTimeMillis());

        sessionQuery(session_id, uuid.toString(), username, ip, 0, start_time, 0);
    }

    public void endSession(VLPlayer player) {
        UUID uuid = player.getUniqueId();

        String session_id = session_ids.get(uuid);
        long duration = System.currentTimeMillis() - session_duration.get(session_id);
        long end_time = System.currentTimeMillis();

        sessionQuery(session_id, "", "", "", duration, 0, end_time);
        session_ids.remove(uuid);
    }

    private void sessionQuery(String session_id, String uuid, String username, String ip, long duration,
                              long start_time, long end_time) {
        String query = "INSERT INTO sessions (session_id, uuid, username, ip, start_time) VALUES ('"
                + session_id + "', '" + uuid + "', '" + username + "', '" + ip + "', '" + start_time
                + "') ON DUPLICATE KEY UPDATE duration=" + duration + ", end_time=" + end_time + "";
        VaultCore.getDatabase().executeUpdateStatement(query);
    }
}
