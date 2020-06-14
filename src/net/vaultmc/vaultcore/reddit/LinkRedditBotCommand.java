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
import net.dean.jraw.RedditClient;
import net.dean.jraw.models.OAuthData;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import org.bukkit.Bukkit;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

@RootCommand(
        literal = "linkredditbot",
        description = "Set the Reddit bot account (Flairs)"
)
@Permission(Permissions.LinkRedditBot)
public class LinkRedditBotCommand extends CommandExecutor {
    private static RedditClient bot;

    public LinkRedditBotCommand() {
        register("link", Collections.emptyList());
        register("readCode", Arrays.asList(
                Arguments.createArgument("code", Arguments.word()),
                Arguments.createArgument("redirect", Arguments.greedyString())
        ));
        if (VaultCore.getInstance().getData().contains("bot-refresh-token")) {
            bot = new RedditClient(RedditCommand.getAdapter(),
                    OAuthData.create(VaultCore.getInstance().getData().getString("bot-access-token"),
                            Arrays.asList("identity", "edit", "flair", "history", "modconfig", "modflair", "modlog", "modposts", "modwiki",
                                    "mysubreddits", "privatemessages", "read", "report", "save", "submit", "subscribe", "vote", "wikiedit", "wikiread"),
                            VaultCore.getInstance().getData().getString("bot-refresh-token"),
                            new Date(VaultCore.getInstance().getData().getLong("bot-expiration"))),
                    RedditCommand.getCredentials(), RedditCommand.getStore(), null);
            if (bot.getAuthManager().needsRenewing()) {
                bot.getAuthManager().renew();
            }
            VaultCore.getInstance().getData().set("bot-access-token", bot.getAuthManager().getAccessToken());
        }
    }

    public static void cleanup() {
        if (bot.getAuthManager().needsRenewing()) {
            bot.getAuthManager().renew();
        }
        VaultCore.getInstance().getData().set("bot-access-token", bot.getAuthManager().getAccessToken());
        VaultCore.getInstance().getData().set("bot-expiration", bot.getAuthManager().getCurrent().getExpiration().getTime());
        VaultCore.getInstance().saveConfig();
    }

    @SubCommand("link")
    public void link(VLCommandSender sender) {
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.reddit.link").replace("{URL}", RedditCommand.getAuth().getAuthorizationUrl(true, false,
                "identity", "edit", "flair", "history", "modconfig", "modflair", "modlog", "modposts", "modwiki",
                "mysubreddits", "privatemessages", "read", "report", "save", "submit", "subscribe", "vote", "wikiedit", "wikiread")));
    }

    @SubCommand("readCode")
    public void readCode(VLCommandSender sender, String code, String redirectURI) {
        Bukkit.getScheduler().runTaskAsynchronously(VaultLoader.getInstance(), () -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL("https://www.reddit.com/api/v1/access_token").openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.addRequestProperty("User-Agent", RedditCommand.getAdapter().getUserAgent().getValue());
                conn.addRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((VaultCore.getInstance().getConfig().getString("reddit-client-id") + ":" +
                        VaultCore.getInstance().getConfig().getString("reddit-client-secret")).getBytes()));
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write("grant_type=authorization_code&code=" + URLEncoder.encode(code, "utf-8") + "&redirect_uri=" +
                        URLEncoder.encode(redirectURI, "utf-8"));
                writer.flush();
                writer.close();
                conn.connect();
                InputStream is = conn.getInputStream();
                Scanner s = new Scanner(is).useDelimiter("\\A");
                if (s.hasNext()) {
                    JsonObject json = new JsonParser().parse(s.next()).getAsJsonObject();
                    String accessToken = json.get("access_token").getAsString();
                    Date expiration = new Date(System.currentTimeMillis() + json.get("expires_in").getAsLong());
                    String refreshToken = json.get("refresh_token").getAsString();

                    VaultCore.getInstance().getData().set("bot-refresh-token", refreshToken);

                    bot = new RedditClient(RedditCommand.getAdapter(),
                            OAuthData.create(accessToken, Arrays.asList("identity", "edit", "flair", "history", "modconfig", "modflair", "modlog", "modposts", "modwiki",
                                    "mysubreddits", "privatemessages", "read", "report", "save", "submit", "subscribe", "vote", "wikiedit", "wikiread"),
                                    refreshToken, expiration), RedditCommand.getCredentials(), RedditCommand.getStore(), null);
                    if (bot.getAuthManager().needsRenewing()) {
                        bot.getAuthManager().renew();
                    }
                    VaultCore.getInstance().getData().set("bot-access-token", bot.getAuthManager().getAccessToken());
                    VaultCore.getInstance().getData().set("bot-expiration", bot.getAuthManager().getCurrent().getExpiration().getTime());
                    VaultCore.getInstance().saveConfig();
                }
                conn.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
