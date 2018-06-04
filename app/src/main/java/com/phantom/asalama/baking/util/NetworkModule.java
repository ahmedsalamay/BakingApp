package com.phantom.asalama.baking.util;


import android.content.Context;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

@Module(includes = ContextModule.class)
public class NetworkModule {
    @Provides
    @BakingAppScope
    public OkHttpClient okHttpClient
            (HttpLoggingInterceptor httpLoggingInterceptor, Cache cache) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .cache(cache)
                .build();
        return okHttpClient;
    }

    @Provides
    @BakingAppScope
    public HttpLoggingInterceptor loggingInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.i(message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BASIC);
    }

    @Provides
    @BakingAppScope
    public Cache cache(File file) {
        return new Cache(file, 10 * 1000 * 1000);//10mg cache
    }

    @Provides
    @BakingAppScope
    public File CacheFile(Context context) {
        return new File(context.getCacheDir(), "okhttp cache");
    }
}
