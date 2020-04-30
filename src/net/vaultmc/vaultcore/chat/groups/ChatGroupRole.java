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
