package net.vaultmc.vaultcore.misc.commands.mail;

import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.DBConnection;

import java.util.UUID;

public class Mail {

    DBConnection database = VaultCore.getDatabase();

    public int id;
    public UUID recipient;
    public UUID sender;
    public long sent;
    public String message;

    public Mail(int id, UUID recipient, UUID sender, long sent, String message) {
        this.id = id;
        this.recipient = recipient;
        this.sender = sender;
        this.sent = sent;
        this.message = message;
    }

    public void markRead(int id) {

    }
}
