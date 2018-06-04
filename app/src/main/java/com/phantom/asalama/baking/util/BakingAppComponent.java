package com.phantom.asalama.baking.util;

import com.squareup.picasso.Picasso;

import dagger.Component;

@BakingAppScope
@Component(modules = {PIcassoModule.class, BakingServicesModule.class})
public interface BakingAppComponent {
    Picasso getPicasso();

    BakingServices getMovieService();
}
