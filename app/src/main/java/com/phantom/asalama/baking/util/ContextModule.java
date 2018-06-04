package com.phantom.asalama.baking.util;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    private final Context mContext;

    public ContextModule(Context context) {
        mContext = context;
    }

    @Provides
    public Context context() {
        return mContext;
    }
}
