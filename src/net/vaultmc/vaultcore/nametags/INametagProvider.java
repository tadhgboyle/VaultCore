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

package net.vaultmc.vaultcore.nametags;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.World;

public interface INametagProvider {
    Nametag provideNametag(VLPlayer player, World world);

    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    class Nametag {
        @Getter
        private final String prefix;
        @Getter
        private final String suffix;
        @Getter
        private final String sortPriority;
    }
}
