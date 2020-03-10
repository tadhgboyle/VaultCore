package net.vaultmc.vaultcore.tour;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class Tour extends ConstructorRegisterListener {
    @Getter
    private static final Set<UUID> touringPlayers = new HashSet<>();

    private static final Set<UUID> preventMoving = new HashSet<>();
    private static final Set<UUID> preventMovingOnly = new HashSet<>();

    private static final World world = Bukkit.getWorld("tour");

    private static final Location legacyLobby = new Location(world, 148.5, 71, 23.5, -24.6F, 27.6F);
    private static final Location players = new Location(world, 488.5, 48, 513.5, -90F, 0F);
    private static final Location initial = new Location(world, 440.5, 53, 469.5, 0F, -90F);
    private static final Location secondLobby = new Location(world, 0, 90, 0, -90F, 0F);
    private static final Location secondLobbySky = new Location(world, 0, 120, 0, 0F, 90F);
    private static final Location secondLobbySvClans = new Location(world, 0.5, 84, 0.5, 90F, 33F);
    private static final Location survival = new Location(world, 198.5, 68, 148.5, -40F, 35F);
    // TODO  Add new lobby once it's finished

    private static final HoverEvent nextHover = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
            new BaseComponent[]{new TextComponent(VaultLoader.getMessage("tour.continue"))});
    private static final Map<UUID, BukkitTask> houseBuildingTasks = new HashMap<>();
    private static final Map<Location, BlockData> houseBlocks = new HashMap<>();
    private static final Location secondLobbyCreative = new Location(world, 0, 84, 0, 0F, 33F);
    private static final Location plot = new Location(world, -106.5, 50, 99, 42F, 37F);
    private static final Location secondLobbySB = new Location(world, 0, 84, 0, -90F, 33F);
    private static final Location skyblock = new Location(world, -91.5, 75, -22.5, 135F, 43F);

    static {
        BlockData oakPlanks = Material.OAK_PLANKS.createBlockData();
        houseBlocks.put(new Location(world, 214, 52, 168), oakPlanks);
        houseBlocks.put(new Location(world, 213, 52, 168), oakPlanks);
        houseBlocks.put(new Location(world, 212, 52, 168), oakPlanks);

        for (int x = 211; x <= 215; x++) {
            houseBlocks.put(new Location(world, x, 52, 167), oakPlanks);
        }

        for (int x = 211; x <= 216; x++) {
            houseBlocks.put(new Location(world, x, 52, 166), oakPlanks);
        }

        for (int x = 211; x <= 215; x++) {
            houseBlocks.put(new Location(world, x, 52, 165), oakPlanks);
        }

        for (int x = 212; x <= 214; x++) {
            houseBlocks.put(new Location(world, x, 52, 164), oakPlanks);
        }

        BlockData stripped = Bukkit.createBlockData("minecraft:stripped_oak_wood[axis=y]");
        houseBlocks.put(new Location(world, 216, 55, 166), Bukkit.createBlockData("minecraft:jungle_stairs[facing=west,half=top,shape=straight]"));
        houseBlocks.put(new Location(world, 216, 56, 166), stripped);

        for (int y = 53; y <= 56; y++) {
            houseBlocks.put(new Location(world, 216, y, 165), stripped);
            houseBlocks.put(new Location(world, 216, y, 167), stripped);
            houseBlocks.put(new Location(world, 215, y, 168), stripped);
            houseBlocks.put(new Location(world, 214, y, 169), stripped);
            houseBlocks.put(new Location(world, 212, y, 169), stripped);
            houseBlocks.put(new Location(world, 211, y, 168), stripped);
            houseBlocks.put(new Location(world, 210, y, 167), stripped);
            houseBlocks.put(new Location(world, 210, y, 165), stripped);
            houseBlocks.put(new Location(world, 211, y, 164), stripped);
            houseBlocks.put(new Location(world, 212, y, 163), stripped);
            houseBlocks.put(new Location(world, 214, y, 163), stripped);
            houseBlocks.put(new Location(world, 215, y, 164), stripped);
        }

        for (int y : new int[]{53, 56}) {
            houseBlocks.put(new Location(world, 213, y, 169), stripped);
            houseBlocks.put(new Location(world, 210, y, 166), stripped);
            houseBlocks.put(new Location(world, 213, y, 163), stripped);
        }

        BlockData glass = Material.GLASS_PANE.createBlockData();
        for (int y = 54; y <= 55; y++) {
            houseBlocks.put(new Location(world, 213, y, 169), glass);
            houseBlocks.put(new Location(world, 210, y, 166), glass);
            houseBlocks.put(new Location(world, 213, y, 163), glass);
        }

        BlockData jungleSlab = Bukkit.createBlockData("minecraft:jungle_slab[type=bottom]");

        for (int x = 211; x <= 215; x++) {
            houseBlocks.put(new Location(world, x, 57, 171), jungleSlab);
            houseBlocks.put(new Location(world, x, 57, 161), jungleSlab);
        }

        for (int z = 164; z <= 168; z++) {
            houseBlocks.put(new Location(world, 208, 57, z), jungleSlab);
            houseBlocks.put(new Location(world, 218, 57, z), jungleSlab);
        }
        houseBlocks.put(new Location(world, 217, 57, 169), jungleSlab);
        houseBlocks.put(new Location(world, 216, 57, 170), jungleSlab);
        houseBlocks.put(new Location(world, 210, 57, 170), jungleSlab);
        houseBlocks.put(new Location(world, 209, 57, 169), jungleSlab);
        houseBlocks.put(new Location(world, 209, 57, 163), jungleSlab);
        houseBlocks.put(new Location(world, 210, 57, 162), jungleSlab);
        houseBlocks.put(new Location(world, 216, 57, 162), jungleSlab);
        houseBlocks.put(new Location(world, 217, 57, 163), jungleSlab);

        BlockData wood = Bukkit.createBlockData("minecraft:oak_wood[axis=y]");
        BlockData air = Material.AIR.createBlockData();

        for (int z = 162; z <= 170; z++) {
            for (int x = 209; x <= 217; x++) {
                houseBlocks.put(new Location(world, x, 57, z), wood);
            }
        }
        houseBlocks.put(new Location(world, 210, 57, 162), air);
        houseBlocks.put(new Location(world, 209, 57, 162), air);
        houseBlocks.put(new Location(world, 209, 57, 163), air);
        houseBlocks.put(new Location(world, 217, 57, 163), air);
        houseBlocks.put(new Location(world, 217, 57, 162), air);
        houseBlocks.put(new Location(world, 216, 57, 162), air);
        houseBlocks.put(new Location(world, 216, 57, 170), air);
        houseBlocks.put(new Location(world, 217, 57, 170), air);
        houseBlocks.put(new Location(world, 217, 57, 169), air);
        houseBlocks.put(new Location(world, 210, 57, 170), air);
        houseBlocks.put(new Location(world, 209, 57, 170), air);
        houseBlocks.put(new Location(world, 209, 57, 169), air);

        for (int z = 164; z <= 168; z++) {
            for (int x = 211; x <= 215; x++) {
                houseBlocks.put(new Location(world, x, 58, z), wood);
            }
        }

        for (int x : new int[]{210, 216}) {
            for (int z = 165; z <= 167; z++) {
                houseBlocks.put(new Location(world, x, 58, z), wood);
            }
        }

        for (int z : new int[]{163, 169}) {
            for (int x = 212; x <= 214; x++) {
                houseBlocks.put(new Location(world, x, 58, z), wood);
            }
        }

        for (int x = 212; x <= 214; x++) {
            for (int z = 165; z <= 167; z++) {
                houseBlocks.put(new Location(world, x, 59, z), wood);
            }
        }

        for (int x : new int[]{211, 215}) {
            houseBlocks.put(new Location(world, x, 59, 166), wood);
        }

        for (int z : new int[]{164, 168}) {
            houseBlocks.put(new Location(world, 213, 59, z), wood);
        }
    }

    private static void clear(VLPlayer player) {
        for (int i = 0; i < 100; i++) {
            player.sendMessage("\n");
        }
    }

    public static void start(VLPlayer player) {
        touringPlayers.add(player.getUniqueId());
        player.setGameMode(GameMode.ADVENTURE);
        player.getPlayer().setGravity(false);
        player.getPlayer().setInvulnerable(true);
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 255, false, false, false));
        player.setAllowFlight(true);
        player.setFlying(true);
        preventMovingOnly.add(player.getUniqueId());
        player.teleport(initial);

        clear(player);

        player.sendMessage(VaultLoader.getMessage("tour.welcome"));
        player.getPlayer().sendTitle(VaultLoader.getMessage("tour.title"), "", 10, 70, 20);

        Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () ->
                player.getPlayer().sendTitle(VaultLoader.getMessage("tour.history.title"), "", 10, 70, 20), 80L);
        Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
            player.teleport(legacyLobby);
            clear(player);
            player.sendMessage(VaultLoader.getMessage("tour.history.displaying-legacy-lobby"));
            TextComponent component = new TextComponent(VaultLoader.getMessage("tour.next"));
            component.setHoverEvent(nextHover);
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tourstage displayingPlayers"));
            player.sendMessage(component);
        }, 110L);
    }

    public static void displayingPlayers(VLPlayer player) {
        player.teleport(players);
        clear(player);

        player.sendMessage(VaultLoader.getMessage("tour.history.displaying-players"));
        TextComponent component = new TextComponent(VaultLoader.getMessage("tour.next"));
        component.setHoverEvent(nextHover);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tourstage displayingNewLobby"));
        player.sendMessage(component);
    }

    public static void displayingNewLobby(VLPlayer player) {
        player.teleport(secondLobby);
        clear(player);

        player.sendMessage(VaultLoader.getMessage("tour.history.displaying-new-lobby"));
        TextComponent component = new TextComponent(VaultLoader.getMessage("tour.next"));
        component.setHoverEvent(nextHover);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tourstage lobbyIntroduction"));
        player.sendMessage(component);
    }

    public static void lobbyIntroduction(VLPlayer player) {
        clear(player);
        player.teleport(secondLobbySky);
        preventMovingOnly.remove(player.getUniqueId());
        preventMoving.add(player.getUniqueId());

        player.getPlayer().sendTitle(VaultLoader.getMessage("tour.games.title"), "", 10, 70, 20);
        Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> player.getPlayer().sendTitle(VaultLoader.getMessage("tour.games.lobby.title"), "", 10, 70, 20), 80L);
        Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
            player.sendMessage(VaultLoader.getMessage("tour.games.lobby.text"));
            TextComponent component = new TextComponent(VaultLoader.getMessage("tour.next"));
            component.setHoverEvent(nextHover);
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tourstage survivalAndClans"));
            player.sendMessage(component);
        }, 140L);
    }

    public static void survivalAndClans(VLPlayer player) {
        clear(player);
        player.teleport(secondLobbySvClans);
        AtomicInteger count = new AtomicInteger();
        AtomicInteger count0 = new AtomicInteger();
        Bukkit.getScheduler().runTaskTimer(VaultLoader.getInstance(), task -> {
            count.getAndIncrement();
            if (count.get() >= 165) {
                Bukkit.getScheduler().runTaskTimer(VaultLoader.getInstance(), t -> {
                    count0.getAndIncrement();
                    if (count0.get() >= 133) {
                        survivalAndClans2(player);
                        t.cancel();
                        return;
                    }
                    Location location = player.getLocation().clone();
                    location.setX(location.getX() - 0.2);
                    location.setYaw(90F);
                    location.setPitch(33F);
                    player.teleport(location);
                }, 0, 1);

                task.cancel();
                return;
            }
            Location location = player.getLocation().clone();
            location.setX(location.getX() - 0.2);
            location.setY(location.getY() - 0.2);
            location.setYaw(90F);
            location.setPitch(33F);
            player.teleport(location);
        }, 0, 1);
    }

    public static void survivalAndClans3(VLPlayer player) {
        clear(player);
        player.sendMessage(VaultLoader.getMessage("tour.games.survival.text-clans"));
        Bukkit.getScheduler().runTaskTimer(VaultLoader.getInstance(), task -> {
            if (!houseBuildingTasks.containsKey(player.getUniqueId())) {
                TextComponent component = new TextComponent(VaultLoader.getMessage("tour.next"));
                component.setHoverEvent(nextHover);
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tourstage creative"));
                player.sendMessage(component);
                task.cancel();
            }
        }, 0, 10);
    }

    public static void survivalAndClans2(VLPlayer player) {
        clear(player);
        player.teleport(survival);
        player.getPlayer().sendTitle(VaultLoader.getMessage("tour.games.survival.title"), "", 10, 70, 20);
        Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
            player.sendMessage(VaultLoader.getMessage("tour.games.survival.text-survival"));
            TextComponent component = new TextComponent(VaultLoader.getMessage("tour.next"));
            component.setHoverEvent(nextHover);
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tourstage survivalAndClans3"));
            player.sendMessage(component);
        }, 80L);

        Iterator<Map.Entry<Location, BlockData>> iterator = houseBlocks.entrySet().iterator();
        houseBuildingTasks.put(player.getUniqueId(), Bukkit.getScheduler().runTaskTimer(VaultLoader.getInstance(), () -> {
            if (iterator.hasNext()) {
                Map.Entry<Location, BlockData> entry = iterator.next();
                player.getPlayer().sendBlockChange(entry.getKey(), entry.getValue());
            } else {
                houseBuildingTasks.get(player.getUniqueId()).cancel();
                houseBuildingTasks.remove(player.getUniqueId());
            }
        }, 0, 1));
    }

    public static void creative2(VLPlayer player) {
        player.teleport(plot);
        clear(player);

        player.getPlayer().sendTitle(VaultLoader.getMessage("tour.games.creative.title"), "", 10, 70, 20);

        Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
            player.sendMessage(VaultLoader.getMessage("tour.games.creative.text"));
            TextComponent component = new TextComponent(VaultLoader.getMessage("tour.next"));
            component.setHoverEvent(nextHover);
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tourstage skyblock"));
            player.sendMessage(component);
        }, 80L);
    }

    public static void creative(VLPlayer player) {
        clear(player);
        player.teleport(secondLobbyCreative);
        AtomicInteger count = new AtomicInteger();
        AtomicInteger count0 = new AtomicInteger();
        Bukkit.getScheduler().runTaskTimer(VaultLoader.getInstance(), task -> {
            count.getAndIncrement();
            if (count.get() >= 165) {
                Bukkit.getScheduler().runTaskTimer(VaultLoader.getInstance(), t -> {
                    count0.getAndIncrement();
                    if (count0.get() >= 133) {
                        creative2(player);
                        t.cancel();
                        return;
                    }
                    Location location = player.getLocation().clone();
                    location.setZ(location.getZ() + 0.2);
                    location.setYaw(0F);
                    location.setPitch(33F);
                    player.teleport(location);
                }, 0, 1);

                task.cancel();
                return;
            }
            Location location = player.getLocation().clone();
            location.setZ(location.getZ() + 0.2);
            location.setY(location.getY() - 0.2);
            location.setYaw(0F);
            location.setPitch(33F);
            player.teleport(location);
        }, 0, 1);
    }

    public static void skyblock(VLPlayer player) {
        player.teleport(secondLobbySB);
        clear(player);
        AtomicInteger count = new AtomicInteger();
        AtomicInteger count0 = new AtomicInteger();
        Bukkit.getScheduler().runTaskTimer(VaultLoader.getInstance(), task -> {
            count.getAndIncrement();
            if (count.get() >= 165) {
                Bukkit.getScheduler().runTaskTimer(VaultLoader.getInstance(), t -> {
                    count0.getAndIncrement();
                    if (count0.get() >= 133) {
                        skyblock2(player);
                        t.cancel();
                        return;
                    }
                    Location location = player.getLocation().clone();
                    location.setX(location.getX() + 0.2);
                    location.setYaw(-90F);
                    location.setPitch(33F);
                    player.teleport(location);
                }, 0, 1);

                task.cancel();
                return;
            }
            Location location = player.getLocation().clone();
            location.setX(location.getX() + 0.2);
            location.setY(location.getY() - 0.2);
            location.setYaw(-90F);
            location.setPitch(33F);
            player.teleport(location);
        }, 0, 1);
    }

    public static void skyblock2(VLPlayer player) {
        clear(player);
        player.teleport(skyblock);

        player.getPlayer().sendTitle(VaultLoader.getMessage("tour.games.skyblock.title"), "", 10, 70, 20);
        Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
            player.sendMessage(VaultLoader.getMessage("tour.games.skyblock.text"));
            TextComponent component = new TextComponent(VaultLoader.getMessage("tour.next"));
            component.setHoverEvent(nextHover);
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tourstage ending"));
            player.sendMessage(component);
        }, 80L);
    }

    public static void ending(VLPlayer player) {
        clear(player);
        touringPlayers.remove(player.getUniqueId());
        preventMoving.remove(player.getUniqueId());
        preventMovingOnly.remove(player.getUniqueId());
        player.sendMessage(VaultLoader.getMessage("tour.congrats"));
        player.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
        player.setGameMode(GameMode.ADVENTURE);
        player.getPlayer().setGravity(true);
        player.getPlayer().setInvulnerable(false);
        player.getPlayer().setAllowFlight(false);
        player.getPlayer().setFlying(false);
        player.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        if (touringPlayers.contains(e.getPlayer().getUniqueId()) && !e.getMessage().startsWith("/tourstage")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (preventMoving.contains(e.getPlayer().getUniqueId())) e.setCancelled(true);
        if (preventMovingOnly.contains(e.getPlayer().getUniqueId()) && (
                e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY() ||
                        e.getFrom().getZ() != e.getTo().getZ()
        )) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (touringPlayers.contains(e.getPlayer().getUniqueId())) {
            touringPlayers.remove(e.getPlayer().getUniqueId());
            preventMoving.remove(e.getPlayer().getUniqueId());
            preventMovingOnly.remove(e.getPlayer().getUniqueId());
            if (houseBuildingTasks.containsKey(e.getPlayer().getUniqueId())) {
                houseBuildingTasks.get(e.getPlayer().getUniqueId()).cancel();
                houseBuildingTasks.remove(e.getPlayer().getUniqueId());
            }
            e.getPlayer().setGravity(true);
            e.getPlayer().setInvulnerable(false);
            e.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
            e.getPlayer().setAllowFlight(false);
            e.getPlayer().setFlying(false);
            e.getPlayer().teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
        }
    }
}
