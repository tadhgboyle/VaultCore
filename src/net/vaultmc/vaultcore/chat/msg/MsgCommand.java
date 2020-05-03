package net.vaultmc.vaultcore.chat.msg;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.chat.IgnoreCommand;
import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RootCommand(literal = "msg", description = "Send a player a message.")
@Permission(Permissions.MsgCommand)
@PlayerOnly
@Aliases({"tell", "whisper", "w", "pm", "privatemessage"})
public class MsgCommand extends CommandExecutor implements Listener {
    @Getter
    private static final Map<UUID, UUID> replies = new HashMap<>();

    public MsgCommand() {
        unregisterExisting();
        this.register("msg", Arrays.asList(
                Arguments.createArgument("target", Arguments.playerArgument()),
                Arguments.createArgument("message", Arguments.greedyString())));
        VaultCore.getInstance().registerEvents(this);
    }

    @SubCommand("msg")
    public static void msg(VLPlayer sender, VLPlayer target, String message) {
        if (sender == target) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.msg.self_error"));
            return;
        }
        if (!PlayerSettings.getSetting(target, "settings.msg")) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.msg.disabled_messages"));
            return;
        }
        if (IgnoreCommand.isIgnoring(target, sender)) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.ignore.you_are_ignored"));
            return;
        }
        if (PlayerSettings.getSetting(sender, "settings.grammarly")) {
            message = Utilities.grammarly(message);
        }
        replies.put(target.getUniqueId(), sender.getUniqueId());
        if (PlayerSettings.getSetting(target, "settings.notifications"))
            Bukkit.getPlayer(target.getUniqueId()).playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.BLOCKS, 100, (float) Math.pow(2F, (-6F / 12F)) /* High C */);
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"), sender.getFormattedName(), target.getFormattedName(), message));
        target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.msg.format"), sender.getFormattedName(), target.getFormattedName(), message));
        SocialSpyCommand.sendSS("MSG", sender, target, message);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        replies.remove(e.getPlayer().getUniqueId());
    }
}
