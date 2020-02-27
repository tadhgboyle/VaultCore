package net.vaultmc.vaultcore.combat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedParticle;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class LegacyCombat extends ConstructorRegisterListener implements Runnable {
    private static final double REDUCTION_PER_ARMOUR_POINT = 0.04;

    private static final Set<EntityDamageEvent.DamageCause> NON_REDUCED_CAUSES = EnumSet.of(
            EntityDamageEvent.DamageCause.FIRE_TICK,
            EntityDamageEvent.DamageCause.VOID,
            EntityDamageEvent.DamageCause.SUFFOCATION,
            EntityDamageEvent.DamageCause.DROWNING,
            EntityDamageEvent.DamageCause.STARVATION,
            EntityDamageEvent.DamageCause.FALL,
            EntityDamageEvent.DamageCause.MAGIC,
            EntityDamageEvent.DamageCause.LIGHTNING
    );
    private static final Map<Material, Double> damage = new HashMap<>();
    private static final Map<UUID, ItemStack> offHandItem = new HashMap<>();
    private static final List<Material> interactive = Arrays.asList(
            Material.DISPENSER,
            Material.TNT,
            Material.CHEST,
            Material.CRAFTING_TABLE,
            Material.FURNACE,
            Material.STONE_BUTTON,
            Material.ACACIA_BUTTON,
            Material.BIRCH_BUTTON,
            Material.OAK_BUTTON,
            Material.DARK_OAK_BUTTON,
            Material.JUNGLE_BUTTON,
            Material.SPRUCE_BUTTON,
            Material.ACACIA_TRAPDOOR,
            Material.BIRCH_TRAPDOOR,
            Material.OAK_TRAPDOOR,
            Material.DARK_OAK_TRAPDOOR,
            Material.JUNGLE_TRAPDOOR,
            Material.SPRUCE_TRAPDOOR,
            Material.ACACIA_DOOR,
            Material.BIRCH_DOOR,
            Material.OAK_DOOR,
            Material.DARK_OAK_DOOR,
            Material.JUNGLE_DOOR,
            Material.SPRUCE_DOOR,
            Material.ACACIA_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.OAK_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.SPRUCE_FENCE_GATE,
            Material.ENCHANTING_TABLE,
            Material.END_PORTAL_FRAME,
            Material.BOOKSHELF,
            Material.ENDER_CHEST,
            Material.BEACON,
            Material.ANVIL,
            Material.CHIPPED_ANVIL,
            Material.DAMAGED_ANVIL,
            Material.TRAPPED_CHEST,
            Material.HOPPER,
            Material.DROPPER,
            Material.IRON_TRAPDOOR,
            Material.IRON_DOOR,
            Material.REPEATER,
            Material.COMPARATOR,
            Material.ACACIA_SIGN,
            Material.BIRCH_SIGN,
            Material.OAK_SIGN,
            Material.DARK_OAK_SIGN,
            Material.JUNGLE_SIGN,
            Material.SPRUCE_SIGN,
            Material.ACACIA_WALL_SIGN,
            Material.BIRCH_WALL_SIGN,
            Material.OAK_WALL_SIGN,
            Material.DARK_OAK_WALL_SIGN,
            Material.JUNGLE_WALL_SIGN,
            Material.SPRUCE_WALL_SIGN,
            Material.LEVER,
            Material.BREWING_STAND,
            Material.LOOM,
            Material.BARREL,
            Material.SMOKER,
            Material.BLAST_FURNACE,
            Material.CARTOGRAPHY_TABLE,
            Material.FLETCHING_TABLE,
            Material.GRINDSTONE,
            Material.LECTERN,
            Material.SMITHING_TABLE,
            Material.STONECUTTER,
            Material.BELL,
            Material.BEE_NEST,
            Material.BEEHIVE,
            Material.CAMPFIRE
    );
    private static final ItemStack SHIELD = new ItemStackBuilder(Material.SHIELD)
            .unbreakable(true)
            .build();
    private static final ItemStack lapis = new ItemStack(Material.LAPIS_LAZULI, 64);
    private static final int OFFHAND_SLOT = 40;

    static {
        damage.put(Material.DIAMOND_AXE, 6D);
        damage.put(Material.IRON_AXE, 5D);
        damage.put(Material.GOLDEN_AXE, 3D);
        damage.put(Material.STONE_AXE, 2D);
        damage.put(Material.WOODEN_AXE, 3D);
        damage.put(Material.GOLDEN_SHOVEL, 1D);
        damage.put(Material.WOODEN_SHOVEL, 1D);
        damage.put(Material.STONE_SHOVEL, 2D);
        damage.put(Material.IRON_SHOVEL, 3D);
        damage.put(Material.DIAMOND_SHOVEL, 4D);
        damage.put(Material.GOLDEN_SWORD, 4D);
        damage.put(Material.WOODEN_SWORD, 4D);
        damage.put(Material.STONE_SWORD, 5D);
        damage.put(Material.IRON_SWORD, 6D);
        damage.put(Material.DIAMOND_SWORD, 7D);
        damage.put(Material.GOLDEN_PICKAXE, 2D);
        damage.put(Material.WOODEN_PICKAXE, 2D);
        damage.put(Material.STONE_PICKAXE, 3D);
        damage.put(Material.IRON_PICKAXE, 4D);
        damage.put(Material.DIAMOND_PICKAXE, 5D);
        damage.put(Material.GOLDEN_HOE, 1D);
        damage.put(Material.WOODEN_HOE, 1D);
        damage.put(Material.STONE_HOE, 1D);
        damage.put(Material.IRON_HOE, 1D);
        damage.put(Material.DIAMOND_HOE, 1D);
    }

    private List<Location> sweepLocations = new ArrayList<>();

    public LegacyCombat() {
        Bukkit.getScheduler().runTaskTimer(VaultLoader.getInstance(), this, 40, 40);
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                sweepLocations.clear();
            }
        };
        task.runTaskTimer(VaultLoader.getInstance(), 0, 1);
        ProtocolLibrary.getProtocolManager().addPacketListener(new ParticleListener());
    }

    @EventHandler
    public void onItemSwap(PlayerSwapHandItemsEvent e) {
        if (e.getOffHandItem().getType() == Material.SHIELD) e.setCancelled(true);
    }

    private static boolean isTool(Material type) {
        return type.toString().matches(".*(AXE|SWORD|PICKAXE|SHOVEL|HOE)");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;
        LivingEntity damagedEntity = (LivingEntity) e.getEntity();
        if (!e.isApplicable(EntityDamageEvent.DamageModifier.MAGIC)) return;
        double armourPoints = damagedEntity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
        double reductionPercentage = armourPoints * REDUCTION_PER_ARMOUR_POINT;

        double reducedDamage = e.getDamage() * reductionPercentage;
        EntityDamageEvent.DamageCause damageCause = e.getCause();

        if (!NON_REDUCED_CAUSES.contains(damageCause) && e.isApplicable(EntityDamageEvent.DamageModifier.ARMOR)) {
            e.setDamage(EntityDamageEvent.DamageModifier.ARMOR, -reducedDamage);
        }

        double enchantmentReductionPercentage = calculateEnchantmentReductionPercentage(
                damagedEntity.getEquipment(), e.getCause());

        if (enchantmentReductionPercentage > 0) {
            e.setDamage(EntityDamageEvent.DamageModifier.MAGIC, 0);
            e.setDamage(EntityDamageEvent.DamageModifier.MAGIC,
                    -e.getFinalDamage() * enchantmentReductionPercentage);
        }
    }

    private double calculateEnchantmentReductionPercentage(EntityEquipment equipment, EntityDamageEvent.DamageCause cause) {
        int totalEpf = 0;
        for (ItemStack armorItem : equipment.getArmorContents()) {
            if (armorItem != null && armorItem.getType() != Material.AIR) {
                for (EnchantmentType enchantmentType : EnchantmentType.values()) {
                    if (!enchantmentType.protectsAgainst(cause)) continue;
                    int enchantmentLevel = armorItem.getEnchantmentLevel(enchantmentType.getEnchantment());
                    if (enchantmentLevel > 0) {
                        totalEpf += enchantmentType.getEpf(enchantmentLevel);
                    }
                }
            }
        }

        totalEpf = Math.min(25, totalEpf);
        totalEpf = (int) Math.ceil(totalEpf * ThreadLocalRandom.current().nextDouble(0.5, 1));
        totalEpf = Math.min(20, totalEpf);

        return REDUCTION_PER_ARMOUR_POINT * totalEpf;
    }

    @EventHandler
    public void onBrew(BrewEvent e) {
        Block block = e.getBlock();
        BlockState blockState = block.getState();
        refuel(blockState);
    }

    private void refuel(BlockState state) {
        if (!(state instanceof BrewingStand))
            return;

        BrewingStand brewingStand = (BrewingStand) state;
        brewingStand.setFuelLevel(20);
        brewingStand.update();
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Inventory inv = e.getInventory();
        Location loc = null;
        try {
            loc = inv.getLocation();
        } catch (Exception ignored) {
        }
        if (loc == null) return;
        Block block = loc.getBlock();

        refuel(block.getState());
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent e) {
        EnchantingInventory ei = (EnchantingInventory) e.getInventory();
        ei.setSecondary(lapis);
    }

    @EventHandler
    public void onLapisClick(InventoryClickEvent e) {
        if (e.getInventory().getType() != InventoryType.ENCHANTING) return;
        ItemStack item = e.getCurrentItem();
        if (item == null) {
            return;
        }
        if (item.getType() == Material.LAPIS_LAZULI && e.getSlot() == 1) {
            e.setCancelled(true);
        } else if (e.getCursor() != null && e.getCursor().getType() == Material.LAPIS_LAZULI && e.getClick() == ClickType.DOUBLE_CLICK) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();
        if (inventory.getType() != InventoryType.ENCHANTING) return;
        ((EnchantingInventory) inventory).setSecondary(null);
    }

    @EventHandler
    public void onLapisOpen(InventoryOpenEvent e) {
        if (e.getInventory().getType() != InventoryType.ENCHANTING) return;
        ((EnchantingInventory) e.getInventory()).setSecondary(lapis);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (offHandItem.containsKey(e.getPlayer().getUniqueId())) {
            e.getPlayer().getInventory().setItemInOffHand(offHandItem.get(e.getPlayer().getUniqueId()));
            offHandItem.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent e) {
        if (e.getRecipe().getResult().getType() == Material.SHIELD) e.setCancelled(true);
    }

    @EventHandler
    public void legacyCooldown(PlayerJoinEvent e) {
        e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(16);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().getType().toString().endsWith("SWORD") && (
                e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && e.getHand() == EquipmentSlot.HAND &&
                !e.getPlayer().isBlocking()) {
            if (e.getClickedBlock() != null && interactive.contains(e.getClickedBlock().getType())) {
                return;
            }
            e.setCancelled(true);
            offHandItem.put(e.getPlayer().getUniqueId(), e.getPlayer().getInventory().getItemInOffHand());
            e.getPlayer().getInventory().setItemInOffHand(SHIELD);
            e.getPlayer().setShieldBlockingDelay(0);
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        Projectile projectile = e.getEntity();
        ProjectileSource shooter = projectile.getShooter();
        if (shooter instanceof Player) {
            Player player = (Player) shooter;
            Vector playerDirection = player.getLocation().getDirection().normalize();
            Vector arrowVelocity = playerDirection.multiply(projectile.getVelocity().length());
            projectile.setVelocity(arrowVelocity);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProjectileHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        if (!(e.getDamager() instanceof Arrow)) {
            return;
        }

        Arrow arrow = (Arrow) e.getDamager();
        ProjectileSource shooter = arrow.getShooter();
        if (shooter instanceof Player) {
            Player shootingPlayer = (Player) shooter;
            if (player.getUniqueId().equals(shootingPlayer.getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSwapHandItems(PlayerSwapHandItemsEvent e) {
        if (shouldCancel(e.getOffHandItem())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOHClick(InventoryClickEvent e) {
        if (e.getInventory().getType() != InventoryType.CRAFTING || e.getSlot() != OFFHAND_SLOT) return;
        if (e.getClick().equals(ClickType.NUMBER_KEY) || shouldCancel(e.getCursor())) {
            e.setResult(Event.Result.DENY);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getSlot() == 40) e.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getType() == Material.SHIELD) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getInventory().getType() != InventoryType.CRAFTING
                || !e.getInventorySlots().contains(OFFHAND_SLOT)) return;

        if (shouldCancel(e.getOldCursor())) {
            e.setResult(Event.Result.DENY);
            e.setCancelled(true);
        }
    }

    private boolean shouldCancel(ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerInventory inv = player.getInventory();
            if (inv.getItemInOffHand().getType() == Material.SHIELD && !player.isBlocking()) {
                inv.setItemInOffHand(offHandItem.get(player.getUniqueId()));
                offHandItem.remove(player.getUniqueId());
            }

            for (int i = 0; i < inv.getContents().length; i++) {
                ItemStack stack = inv.getContents()[i];
                if (stack == null) continue;
                ItemMeta meta = stack.getItemMeta();
                if (isTool(stack.getType()) && meta.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE) == null) {
                    meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                            UUID.randomUUID(), "Attack+", damage.get(stack.getType()), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                }
                stack.setItemMeta(meta);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamaged(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        if (!(damager instanceof Player)) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            e.setCancelled(true);
            return;
        }

        Player attacker = (Player) e.getDamager();
        ItemStack weapon = attacker.getInventory().getItemInMainHand();

        if (weapon.getType().toString().endsWith("_SWORD"))
            onSwordAttack(e, attacker, weapon);
    }

    private void onSwordAttack(EntityDamageByEntityEvent e, Player attacker, ItemStack weapon) {
        Location attackerLocation = attacker.getLocation();
        int level = weapon.getEnchantmentLevel(Enchantment.SWEEPING_EDGE);
        double damage = LegacyCombat.damage.get(weapon.getType()) * level / (level + 1) + 1;

        if (e.getDamage() == damage) {
            if (sweepLocations.contains(attackerLocation)) {
                e.setCancelled(true);
            }
        } else {
            sweepLocations.add(attackerLocation);
        }
    }

    private enum EnchantmentType {
        PROTECTION(() -> EnumSet.allOf(EntityDamageEvent.DamageCause.class), 0.75, Enchantment.PROTECTION_ENVIRONMENTAL),
        FIRE_PROTECTION(() -> {
            return EnumSet.of(
                    EntityDamageEvent.DamageCause.FIRE,
                    EntityDamageEvent.DamageCause.FIRE_TICK,
                    EntityDamageEvent.DamageCause.LAVA,
                    EntityDamageEvent.DamageCause.HOT_FLOOR
            );
        }, 1.25, Enchantment.PROTECTION_FIRE),
        BLAST_PROTECTION(() -> EnumSet.of(
                EntityDamageEvent.DamageCause.ENTITY_EXPLOSION,
                EntityDamageEvent.DamageCause.BLOCK_EXPLOSION
        ), 1.5, Enchantment.PROTECTION_EXPLOSIONS),
        PROJECTILE_PROTECTION(() -> EnumSet.of(
                EntityDamageEvent.DamageCause.PROJECTILE
        ), 1.5, Enchantment.PROTECTION_PROJECTILE),
        FALL_PROTECTION(() -> EnumSet.of(
                EntityDamageEvent.DamageCause.FALL
        ), 2.5, Enchantment.PROTECTION_FALL);

        private Set<EntityDamageEvent.DamageCause> protection;
        private double typeModifier;
        private Enchantment enchantment;

        EnchantmentType(Supplier<Set<EntityDamageEvent.DamageCause>> protection, double typeModifier, Enchantment enchantment) {
            this.protection = protection.get();
            this.typeModifier = typeModifier;
            this.enchantment = enchantment;
        }

        public boolean protectsAgainst(EntityDamageEvent.DamageCause cause) {
            return protection.contains(cause);
        }

        public Enchantment getEnchantment() {
            return enchantment;
        }

        public int getEpf(int level) {
            return (int) Math.floor((6 + level * level) * typeModifier / 3);
        }
    }

    private static class ParticleListener extends PacketAdapter {
        public ParticleListener() {
            super(VaultLoader.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.WORLD_PARTICLES);
        }

        @Override
        public void onPacketSending(PacketEvent e) {
            WrappedParticle<?> particle = e.getPacket().getNewParticles().read(0);
            if (particle.getParticle() == Particle.SWEEP_ATTACK) {
                e.setCancelled(true);
            }
        }
    }
}
