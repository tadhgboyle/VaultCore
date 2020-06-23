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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.OAuthData;
import net.dean.jraw.oauth.*;
import net.dean.jraw.references.SubredditReference;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
    private static final Map<String, String> flairs = new HashMap<>();
    private static final Map<String, String> flairText = new HashMap<>();

    static {
        flairs.put("hero", "85d7a366-b0fb-11ea-9a25-0eb2118cc69b");
        flairs.put("titan", "a5adb7a2-b0fb-11ea-9644-0eea9c525e61");
        flairs.put("god", "bb61ef0a-b0fb-11ea-9895-0e3bb9420c09");
        flairs.put("lord", "cd696cbe-b0fb-11ea-9ed5-0e7038133c7b");
        flairs.put("overlord", "dd357728-b0fb-11ea-8e2d-0ee139473957");
        flairs.put("trusted", "34004876-b0fc-11ea-ad55-0e97d85629b1");
        flairs.put("helper", "40d38a0e-b0fc-11ea-8171-0e09c91d6fed");
        flairs.put("moderator", "527abd4a-b0fc-11ea-a011-0e3a1e57ef8f");
        flairs.put("admin", "5e57d3fa-b0fc-11ea-b657-0eaccd85101b");

        flairText.put("hero", "Hero");
        flairText.put("titan", "Titan");
        flairText.put("god", "God");
        flairText.put("lord", "Lord");
        flairText.put("overlord", "Overlord");
        flairText.put("trusted", "Trusted");
        flairText.put("helper", "Helper");
        flairText.put("moderator", "Moderator");
        flairText.put("admin", "Admin");
    }

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

    @EventHandler(priority = EventPriority.HIGHEST /* Called last */)
    public void onPlayerJoin(PlayerJoinEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
        Bukkit.getScheduler().runTaskAsynchronously(VaultLoader.getInstance(), () -> {
            try (ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT reddit_token, reddit_refresh_token, reddit_expiration, reddit_new_link" +
                    " FROM players WHERE uuid=?", player.getUniqueId().toString())) {
                if (rs.next()) {
                    String token = rs.getString("reddit_token");
                    String refreshToken = rs.getString("reddit_refresh_token");
                    long expiration = rs.getLong("reddit_expiration");
                    if (token == null || refreshToken == null) {
                        return;
                    }
                    if (System.currentTimeMillis() >= expiration) {
                        try {
                            HttpURLConnection conn = (HttpURLConnection) new URL("https://www.reddit.com/api/v1/access_token").openConnection();
                            conn.setDoInput(true);
                            conn.setDoOutput(true);
                            conn.setRequestMethod("POST");
                            conn.addRequestProperty("User-Agent", RedditCommand.getAdapter().getUserAgent().getValue());
                            conn.addRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((VaultCore.getInstance().getConfig().getString("reddit-client-id") + ":" +
                                    VaultCore.getInstance().getConfig().getString("reddit-client-secret")).getBytes()));
                            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                            writer.write("grant_type=refresh_token&refresh_token=" + URLEncoder.encode(refreshToken, "utf-8"));
                            writer.flush();
                            writer.close();
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            Scanner s = new Scanner(is).useDelimiter("\\A");
                            if (s.hasNext()) {
                                JsonObject json = new JsonParser().parse(s.next()).getAsJsonObject();
                                token = json.get("access_token").getAsString();
                                VaultCore.getDatabase().executeUpdateStatement("UPDATE players SET reddit_token=?, " +
                                                "reddit_expiration=? WHERE uuid=?", token, System.currentTimeMillis() + json.get("expires_in").getAsLong() * 1000,
                                        player.getUniqueId().toString());
                            }
                            conn.disconnect();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
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

                        if (rs.getBoolean("reddit_new_link")) {
                            VaultCore.getDatabase().executeUpdateStatement("UPDATE players SET reddit_new_link=? WHERE uuid=?", false, player.getUniqueId().toString());
                            player.sendMessageByKey("vaultcore.commands.reddit.new-link", "username", client.me().getUsername());
                        }

                        SubredditReference subreddit = client.subreddit("VaultMinecraft");
                        if (!subreddit.about().isUserSubscriber()) {
                            subreddit.subscribe();
                        }
                        if (flairs.containsKey(player.getGroup())) {
                            LinkRedditBotCommand.getBot().subreddit("VaultMinecraft").otherUserFlair(client.me().getUsername())
                                    .updateToTemplate(flairs.get(player.getGroup()), flairText.get(player.getGroup()));
                        }
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
        if (client == null) {
            return;
        }
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
                String url = auth.getAuthorizationUrl(true, false, "identity", "subscribe");
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
                    loadedRedditClients.remove(sender.getUniqueId());
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