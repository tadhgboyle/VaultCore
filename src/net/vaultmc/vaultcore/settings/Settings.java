package net.vaultmc.vaultcore.settings;

import lombok.Getter;

public enum Settings {

    ALLOW_MESSAGES("Allow Messages", "msg"),
    CYCLE("Cycle Hotbar", "cycle"),
    ALLOW_TPA("Allow TPA's", "tpa"),
    AUTO_TPA("Auto Accept TPA's", "autotpa"),
    MINIMAL_MESSAGES("Minimal Messages", "minimal_messages"),
    MINIMAL_CAPS("Minimal Caps", "minimal_caps"),
    CR_ITEM_DROPS("Item Drops", "item_drops"),
    GRAMMARLY("Grammarly", "grammarly");

    @Getter
    public String name;
    @Getter
    public String vc_name;

    Settings(String name, String vc_name) {
        this.name = name;
        this.vc_name = vc_name;
    }
}
