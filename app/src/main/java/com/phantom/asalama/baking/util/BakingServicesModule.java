package com.phantom.asalama.baking.util;

import com.fatboyindustrial.gsonjodatime.DateTimeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = NetworkModule.class)
public class BakingServicesModule {

    private final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    @Provides
    @BakingAppScope
    public BakingServices movieService(OkHttpClient okHttpClient, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        return retrofit.create(BakingServices.class);
    }

    @Provides
    @BakingAppScope
    public Gson gson() {
        return new GsonBuilder().
                registerTypeAdapter(DateTime.class, new DateTimeConverter()).create();
    }

}
