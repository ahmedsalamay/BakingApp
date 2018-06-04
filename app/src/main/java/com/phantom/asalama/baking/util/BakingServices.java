package com.phantom.asalama.baking.util;

import com.phantom.asalama.baking.models.Baking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingServices {
    String URL = "topher/2017/May/59121517_baking/baking.json";

    @GET(URL)
    Call<List<Baking>> getBakingInfo();
}
