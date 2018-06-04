package com.phantom.asalama.baking;

import android.app.Activity;
import android.app.Application;

import com.phantom.asalama.baking.util.BakingAppComponent;
import com.phantom.asalama.baking.util.BakingServices;
import com.phantom.asalama.baking.util.ContextModule;
import com.phantom.asalama.baking.util.DaggerBakingAppComponent;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

public class BakingApplication extends Application {
    private Picasso mPicasso;
    private BakingServices mBakingServices;

    public static BakingApplication get(Activity activity) {
        return (BakingApplication) activity.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        BakingAppComponent bakingAppComponent = DaggerBakingAppComponent.builder()
                .contextModule(new ContextModule(this))
                .build();

        mBakingServices = bakingAppComponent.getMovieService();
        mPicasso = bakingAppComponent.getPicasso();

    }

    public BakingServices getmMovieService() {
        return mBakingServices;
    }

    public Picasso getmPicasso() {
        return mPicasso;
    }
}
