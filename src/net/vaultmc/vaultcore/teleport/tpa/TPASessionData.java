package net.vaultmc.vaultcore.teleport.tpa;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class TPASessionData {
    private UUID from;
    private UUID to;
}
