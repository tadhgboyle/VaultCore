package net.vaultmc.vaultcore.messenger;

import lombok.Getter;

import java.util.function.Consumer;

public class GeneralCallback<T> {
    @Getter
    private Consumer<T> success;
    @Getter
    private Consumer<T> failure;

    public GeneralCallback<T> success(Consumer<T> consumer) {
        success = consumer;
        return this;
    }

    public GeneralCallback<T> failure(Consumer<T> consumer) {
        failure = consumer;
        return this;
    }
}
