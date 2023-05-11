package com.acrontum.caltrack.util;

import com.microsoft.graph.authentication.IAuthenticationProvider;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class PlainTokenProvider implements IAuthenticationProvider {

    @NotNull
    @Override
    public CompletableFuture<String> getAuthorizationTokenAsync(@NotNull URL requestUrl) {
        return CompletableFuture.completedFuture("");
    }
}
