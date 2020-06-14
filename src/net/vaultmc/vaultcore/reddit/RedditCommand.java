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

package net.vaultmc.vaultcore.reddit;

import lombok.Getter;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.OAuthData;
import net.dean.jraw.oauth.*;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.ResultSet;
import java.util.*;

@RootCommand(
        literal = "reddit",
        description = "Link your Reddit account with VaultMC."
)
@PlayerOnly
public class RedditCommand extends CommandExecutor implements Listener {
    @Getter
    private static final StatefulAuthHelper auth;
    @Getter
    private static final NetworkAdapter adapter;
    @Getter
    private static final Credentials credentials;
    @Getter
    private static final TokenStore store = new NoopTokenStore();
    @Getter
    private static final Map<UUID, RedditClient> loadedRedditClients = new HashMap<>();

    static {
        credentials = Credentials.webapp(VaultCore.getInstance().getConfig().getString("reddit-client-id"),
                VaultCore.getInstance().getConfig().getString("reddit-client-secret"),
                VaultCore.getInstance().getConfig().getString("reddit-redirect-url"));
        adapter = new OkHttpNetworkAdapter(new UserAgent("VaultMC Reddit Bot/VaultCore Control"));
        auth = OAuthHelper.interactive(adapter, credentials);
    }

    public RedditCommand() {
        register("link", Collections.emptyList());
        register("unlink", Collections.singletonList(Arguments.createLiteral("unlink")));
        VaultCore.getInstance().registerEvents(this);
    }

    public static void cleanup() {
        for (Map.Entry<UUID, RedditClient> entry : loadedRedditClients.entrySet()) {
            RedditClient client = entry.getValue();
            if (client.getAuthManager().needsRenewing()) {
                client.getAuthManager().renew();
            }
            VaultCore.getDatabase().executeUpdateStatement("UPDATE players SET reddit_token=?, reddit_expiration=? WHERE uuid=?",
                    client.getAuthManager().getAccessToken(), client.getAuthManager().getCurrent().getExpiration().getTime(), entry.getKey().toString());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        Bukkit.getScheduler().runTaskAsynchronously(VaultLoader.getInstance(), () -> {
            try (ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT reddit_token, reddit_refresh_token, reddit_expiration" +
                    " FROM players WHERE uuid=?", player.getUniqueId().toString())) {
                if (rs.next()) {
                    String token = rs.getString("reddit_token");
                    String refreshToken = rs.getString("reddit_refresh_token");
                    long expiration = rs.getLong("reddit_expiration");
                    try {
                        RedditClient client = new RedditClient(adapter,
                                OAuthData.create(token, Collections.singletonList("identity"), refreshToken, new Date(expiration)),
                                credentials, store, null);
                        if (client.getAuthManager().needsRenewing()) {
                            client.getAuthManager().renew();
                        }
                        if (!token.equals(client.getAuthManager().getAccessToken())) {
                            VaultCore.getDatabase().executeUpdateStatement("UPDATE players SET reddit_token=?, reddit_expiration=? WHERE uuid=?",
                                    client.getAuthManager().getAccessToken(), client.getAuthManager().getCurrent().getExpiration().getTime(), player.getUniqueId().toString());
                        }
                        loadedRedditClients.put(player.getUniqueId(), client);
                    } catch (Exception ignored) {
                    }
                }
            } catch (Exception ex) {
                VaultLoader.logger().error("An error occurred while attempting to fetch RedditClient object of " + player + ":");
                ex.printStackTrace();
            }
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        RedditClient client = loadedRedditClients.remove(e.getPlayer().getUniqueId());
        if (client.getAuthManager().needsRenewing()) {
            client.getAuthManager().renew();
        }
        VaultCore.getDatabase().executeUpdateStatement("UPDATE players SET reddit_token=?, reddit_expiration=? WHERE uuid=?",
                client.getAuthManager().getAccessToken(), client.getAuthManager().getCurrent().getExpiration().getTime(), e.getPlayer().getUniqueId().toString());
    }

    @SubCommand("link")
    public void link(VLPlayer sender) {
        Bukkit.getScheduler().runTaskAsynchronously(VaultLoader.getInstance(), () -> {
            try (ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT reddit_token, reddit_refresh_token FROM players WHERE uuid=?", sender.getUniqueId().toString())) {
                if (rs.next() && loadedRedditClients.containsKey(sender.getUniqueId())) {
                    sender.sendMessageByKey("vaultcore.commands.reddit.info", "username", loadedRedditClients.get(sender.getUniqueId()).me().getUsername());
                    return;
                }
                String url = auth.getAuthorizationUrl(true, false, "identity");
                sender.sendMessageByKey("vaultcore.commands.reddit.link", "url", url);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    @SubCommand("unlink")
    public void unlink(VLPlayer sender) {
        Bukkit.getScheduler().runTaskAsynchronously(VaultLoader.getInstance(), () -> {
            try (ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT reddit_token, reddit_refresh_token" +
                    " FROM players WHERE uuid=?", sender.getUniqueId().toString())) {
                if (rs.next()) {
                    VaultCore.getDatabase().executeUpdateStatement("UPDATE players SET reddit_token=NULL, reddit_refresh_token=NULL WHERE uuid=?", sender.getUniqueId().toString());
                    sender.sendMessageByKey("vaultcore.commands.reddit.unlinked");
                } else {
                    sender.sendMessageByKey("vaultcore.commands.reddit.not-linked");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
