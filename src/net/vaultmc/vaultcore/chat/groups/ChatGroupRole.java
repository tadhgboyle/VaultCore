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

package net.vaultmc.vaultcore.chat.groups;

import lombok.Getter;

public enum ChatGroupRole {
    OWNER(3, "Owner"),
    ADMIN(2, "Admin"),
    MEMBER(1, "Member");

    @Getter
    private final int level;
    @Getter
    private final String name;

    ChatGroupRole(int level, String name) {
        this.level = level;
        this.name = name;
    }
}
