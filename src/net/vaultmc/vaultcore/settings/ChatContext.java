package net.vaultmc.vaultcore.settings;

public enum ChatContext {
    CHAT_GROUP("Chat Group", "!", "chat-group"),
    STAFF_CHAT("Staff Chat", "#", "staff-chat"),
    ADMIN_CHAT("Admin Chat", ",", "admin-chat");

    public String name;
    public String defaultKey;
    public String internalName;

    ChatContext(String name, String defaultKey, String internalName) {
        this.name = name;
        this.defaultKey = defaultKey;
        this.internalName = internalName;
    }
}
