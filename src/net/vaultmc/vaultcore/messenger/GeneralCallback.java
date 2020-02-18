package net.vaultmc.vaultcore.messenger;

import lombok.Getter;

import java.util.function.Consumer;

public class GeneralCallback<T> {
    @Getter
    private Consumer<T> success;
    @Getter
    private Consumer<T> failure;

    public void success(Consumer<T> consumer) {
        success = consumer;
    }

    public void failure(Consumer<T> consumer) {
        failure = consumer;
    }
}
