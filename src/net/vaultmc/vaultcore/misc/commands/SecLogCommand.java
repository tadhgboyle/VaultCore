package net.vaultmc.vaultcore.misc.commands;

import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Member;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.discordbot.VaultMCBot;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RootCommand(
        literal = "seclog",
        description = "Secure your login on VaultMC with a password."
)
@PlayerOnly
public class SecLogCommand extends CommandExecutor implements Listener {
    private static final Location auth = new Location(Bukkit.getWorld("Lobby"), 185.5, 120, 125.5, -90F, 90F);
    private static final Set<UUID> setPrompt = new HashSet<>();
    private static final Map<UUID, String> setConfirmPrompt = new HashMap<>();
    private static final Set<UUID> unsetPrompt = new HashSet<>();
    private static final Set<UUID> resetPrompt = new HashSet<>();
    private static final Set<UUID> resetNewPrompt = new HashSet<>();
    private static final Map<UUID, String> resetConfirmPrompt = new HashMap<>();
    @Getter
    private static final Map<UUID, Location> loggingPlayers = new HashMap<>();
    @Getter
    private static final Map<UUID, String> resetingPlayers = new HashMap<>();

    public SecLogCommand() {
        register("set", Collections.singletonList(Arguments.createLiteral("set")));
        register("unset", Collections.singletonList(Arguments.createLiteral("unset")));
        register("unsetConfirm", Arrays.asList(
                Arguments.createLiteral("unset"),
                Arguments.createLiteral("confirm")
        ));
        register("reset", Collections.singletonList(Arguments.createLiteral("reset")));
        register("forgot", Collections.singletonList(Arguments.createLiteral("forgot")));
        VaultCore.getInstance().registerEvents(this);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        setPrompt.remove(e.getPlayer().getUniqueId());
        setConfirmPrompt.remove(e.getPlayer().getUniqueId());
        unsetPrompt.remove(e.getPlayer().getUniqueId());
        resetPrompt.remove(e.getPlayer().getUniqueId());
        resetNewPrompt.remove(e.getPlayer().getUniqueId());
        resetConfirmPrompt.remove(e.getPlayer().getUniqueId());
        loggingPlayers.remove(e.getPlayer().getUniqueId());
        resetingPlayers.remove(e.getPlayer().getUniqueId());
        e.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
    }

    @SubCommand("set")
    public void set(VLPlayer sender) {
        if (sender.getPlayerData().contains("password")) {
            sender.sendMessage(VaultLoader.getMessage("sec-log.set.already-set"));
            return;
        }
        setPrompt.add(sender.getUniqueId());
        sender.sendMessage(VaultLoader.getMessage("sec-log.set.create"));
        sender.sendMessage(VaultLoader.getMessage("sec-log.set.enter-password"));
    }

    @SubCommand("forgot")
    @SneakyThrows
    public void forgot(VLPlayer sender) {
        if (!VaultCore.getInstance().getConfig().getString("server").equalsIgnoreCase("vaultmc")) {
            sender.kick(VaultLoader.getMessage("sec-log.forgot.unusable"));
            return;
        }
        if (!loggingPlayers.containsKey(sender.getUniqueId())) {
            sender.kick(VaultLoader.getMessage("sec-log.forgot.unusable"));
            return;
        }
        if (!sender.getPlayerData().contains("password")) {
            sender.sendMessage(VaultLoader.getMessage("sec-log.unset.not-set"));
            return;
        }

        Location loc = loggingPlayers.remove(sender.getUniqueId());

        try (ResultSet rs = VaultCore.getDatabase().executeQueryStatement("SELECT discord_id FROM players WHERE uuid=?", sender.getUniqueId().toString())) {
            if (rs.next() && rs.getLong("discord_id") != 0) {
                Member discordUser = VaultMCBot.getGuild().getMemberById(rs.getLong("discord_id"));
                try {
                    discordUser.getUser().openPrivateChannel().queue(channel -> {
                        if (channel == null) {
                            sender.sendMessage(VaultLoader.getMessage("sec-log.forgot-dm-on"));
                            return;
                        }
                        String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000000, 999999999));
                        resetingPlayers.put(sender.getUniqueId(), code);
                        sender.sendMessage(VaultLoader.getMessage("sec-log.forgot.received"));
                        channel.sendMessage(VaultLoader.getMessage("sec-log.forgot.dm").replace("{CODE}", code)).queue();
                    });
                } catch (Exception ex) {
                    sender.sendMessage(VaultLoader.getMessage("sec-log.forgot.dm-on"));
                    sender.sendMessage(VaultLoader.getMessage("sec-log.set.enter-password"));
                    loggingPlayers.put(sender.getUniqueId(), loc);
                }
            } else {
                loggingPlayers.put(sender.getUniqueId(), loc);
                sender.sendMessage(VaultLoader.getMessage("sec-log.forgot.link"));
                sender.sendMessage(VaultLoader.getMessage("sec-log.set.enter-password"));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        if ((loggingPlayers.containsKey(e.getPlayer().getUniqueId()) || resetingPlayers.containsKey(e.getPlayer().getUniqueId())) && !e.getMessage().equals("/seclog forgot"))
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {
        if (loggingPlayers.containsKey(e.getWhoClicked().getUniqueId()) || resetingPlayers.containsKey(e.getWhoClicked().getUniqueId()))
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        VLPlayer player = VLPlayer.getPlayer(e.getPlayer());

        e.getRecipients().removeIf(p -> loggingPlayers.containsKey(p.getUniqueId()));

        if (setPrompt.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            setConfirmPrompt.put(e.getPlayer().getUniqueId(), e.getMessage());
            setPrompt.remove(e.getPlayer().getUniqueId());
            e.getPlayer().sendMessage(VaultLoader.getMessage("sec-log.set.confirm-password"));
        } else if (setConfirmPrompt.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            if (e.getMessage().equals(setConfirmPrompt.remove(e.getPlayer().getUniqueId()))) {
                player.getPlayerData().set("password", Hashing.sha512().hashString(e.getMessage(), StandardCharsets.UTF_8).toString());
                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> e.getPlayer().kickPlayer(VaultLoader.getMessage("sec-log.set.set")));
            } else {
                e.getPlayer().sendMessage(VaultLoader.getMessage("sec-log.set.not-match"));
            }
        } else if (unsetPrompt.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            unsetPrompt.remove(e.getPlayer().getUniqueId());
            if (Hashing.sha512().hashString(e.getMessage(), StandardCharsets.UTF_8).toString().equals(player.getPlayerData().getString("password"))) {
                player.getPlayerData().remove("password");
                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> e.getPlayer().kickPlayer(VaultLoader.getMessage("sec-log.set.set")));
            } else {
                player.sendMessage(VaultLoader.getMessage("sec-log.unset.incorrect-password"));
            }
        } else if (resetPrompt.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            resetPrompt.remove(e.getPlayer().getUniqueId());
            if (Hashing.sha512().hashString(e.getMessage(), StandardCharsets.UTF_8).toString().equals(player.getPlayerData().getString("password"))) {
                player.getPlayerData().remove("password");
                resetNewPrompt.add(player.getUniqueId());
                player.sendMessage(VaultLoader.getMessage("sec-log.reset.enter-new-password"));
            } else {
                player.sendMessage(VaultLoader.getMessage("sec-log.unset.incorrect-password"));
            }
        } else if (resetNewPrompt.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            resetNewPrompt.remove(e.getPlayer().getUniqueId());
            resetConfirmPrompt.put(e.getPlayer().getUniqueId(), e.getMessage());
            player.sendMessage(VaultLoader.getMessage("sec-log.reset.confirm-new-password"));
        } else if (resetConfirmPrompt.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            if (e.getMessage().equals(resetConfirmPrompt.remove(e.getPlayer().getUniqueId()))) {
                player.getPlayerData().set("password", Hashing.sha512().hashString(e.getMessage(), StandardCharsets.UTF_8).toString());
                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> e.getPlayer().kickPlayer(VaultLoader.getMessage("sec-log.set.set")));
            } else {
                player.sendMessage(VaultLoader.getMessage("sec-log.set.not-match"));
            }
        } else if (loggingPlayers.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            if (Hashing.sha512().hashString(ChatColor.stripColor(e.getMessage()), StandardCharsets.UTF_8).toString().equals(player.getPlayerData().getString("password"))) {
                player.sendMessage(VaultLoader.getMessage("sec-log.success"));
                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> {
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    player.teleport(loggingPlayers.remove(e.getPlayer().getUniqueId()));
                });
            } else {
                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> e.getPlayer().kickPlayer(VaultLoader.getMessage("sec-log.unset.incorrect-password")));
            }
        } else if (resetingPlayers.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            if (e.getMessage().equals(resetingPlayers.remove(e.getPlayer().getUniqueId()))) {
                player.sendMessage(VaultLoader.getMessage("sec-log.forgot.new-password"));
                setPrompt.add(player.getUniqueId());
                player.sendMessage(VaultLoader.getMessage("sec-log.set.enter-password"));
            } else {
                Bukkit.getScheduler().runTask(VaultLoader.getInstance(), () -> e.getPlayer().kickPlayer(VaultLoader.getMessage("sec-log.forgot.incorrect-code")));
            }
        }
    }

    @SubCommand("unsetConfirm")
    public void realUnset(VLPlayer sender) {
        if (!sender.getPlayerData().contains("password")) {
            sender.sendMessage(VaultLoader.getMessage("sec-log.unset.not-set"));
            return;
        }

        sender.sendMessage(VaultLoader.getMessage("sec-log.set.enter-password"));
        unsetPrompt.add(sender.getUniqueId());
    }

    @SubCommand("unset")
    public void unset(VLPlayer sender) {
        if (!sender.getPlayerData().contains("password")) {
            sender.sendMessage(VaultLoader.getMessage("sec-log.unset.not-set"));
            return;
        }

        if (sender.getGroup().equalsIgnoreCase("moderator") || sender.getGroup().equalsIgnoreCase("admin")) {
            sender.sendMessage(VaultLoader.getMessage("sec-log.unset.mod"));
        } else {
            sender.sendMessage(VaultLoader.getMessage("sec-log.unset.default"));
        }
    }

    @SubCommand("reset")
    public void reset(VLPlayer sender) {
        if (!sender.getPlayerData().contains("password")) {
            sender.sendMessage(VaultLoader.getMessage("sec-log.unset.not-set"));
            return;
        }
        resetPrompt.add(sender.getUniqueId());
        sender.sendMessage(VaultLoader.getMessage("sec-log.set.enter-password"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (VaultCore.getInstance().getConfig().getString("server").equalsIgnoreCase("vaultmc")) {
            VLPlayer player = VLPlayer.getPlayer(e.getPlayer());
            if ((player.getGroup().equalsIgnoreCase("moderator") || player.getGroup().equalsIgnoreCase("admin")) && !player.getPlayerData().contains("password")) {
                Location loc = player.getLocation().clone();
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 255, false, false, false));
                player.teleport(auth);
                loggingPlayers.put(player.getUniqueId(), loc);
                Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
                    TextComponent comp = new TextComponent(VaultLoader.getMessage("sec-log.priv"));
                    comp.setHoverEvent(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(VaultLoader.getMessage("sec-log.priv-hover"))}));
                    comp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/seclog forgot"));
                    player.sendMessage(comp);
                }, 1);
                return;
            }

            if (player.getPlayerData().contains("password")) {
                Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
                    player.sendMessage("\n" + VaultLoader.getMessage("sec-log.set.enter-password"));
                    TextComponent comp = new TextComponent(VaultLoader.getMessage("sec-log.forgot-msg"));
                    comp.setHoverEvent(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(VaultLoader.getMessage("sec-log.forgot-hover"))}));
                    comp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/seclog forgot"));
                    player.sendMessage(comp);
                }, 1);
                Location loc = player.getLocation().clone();
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 255, false, false, false));
                player.teleport(auth);
                loggingPlayers.put(player.getUniqueId(), loc);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent e) {
        if (loggingPlayers.containsKey(e.getPlayer().getUniqueId()) || resetingPlayers.containsKey(e.getPlayer().getUniqueId()))
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (loggingPlayers.containsKey(e.getPlayer().getUniqueId()) || resetingPlayers.containsKey(e.getPlayer().getUniqueId()))
            e.setCancelled(true);
    }
}
