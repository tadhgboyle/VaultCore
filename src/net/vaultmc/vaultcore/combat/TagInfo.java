package net.vaultmc.vaultcore.combat;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

@AllArgsConstructor
@Data
public class TagInfo {
    private UUID attacker;
    private BukkitTask task;
}
