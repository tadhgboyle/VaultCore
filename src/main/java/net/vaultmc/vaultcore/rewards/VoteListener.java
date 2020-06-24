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

package net.vaultmc.vaultcore.rewards;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VoteListener extends ConstructorRegisterListener {
    @EventHandler
    public void onVote(VotifierEvent e) {
        Vote vote = e.getVote();
        Bukkit.getScheduler().runTaskAsynchronously(VaultLoader.getInstance(), () -> {
            try (ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT uuid FROM players WHERE username=?", vote.getUsername())) {
                if (rs.next()) {
                    VLOfflinePlayer player = VLOfflinePlayer.getOfflinePlayer(UUID.fromString(rs.getString("uuid")));
                    player.sendOrScheduleMessageByKey("vaultcore.vote.received");
                    Map<Reward, Long> map = RewardsCommand.getAvailableRewards().getOrDefault(player.getUniqueId(), new HashMap<>());
                    map.put(Reward.VOTE_REWARD, -1L);
                    RewardsCommand.getAvailableRewards().put(player.getUniqueId(), map);
                    for (VLPlayer p : VLPlayer.getOnlinePlayers()) {
                        if (!p.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                            p.sendMessageByKey("vaultcore.vote.broadcast", "player", player.getFormattedName());
                        }
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
