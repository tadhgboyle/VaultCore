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

package net.vaultmc.vaultcore.misc.commands.mail;

import java.util.UUID;

public class Mail {

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
}
