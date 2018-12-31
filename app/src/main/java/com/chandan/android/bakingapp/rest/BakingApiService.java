package com.chandan.android.bakingapp.rest;

import com.chandan.android.bakingapp.model.BakingData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingApiService {

    @GET("2017/May/59121517_baking/baking.json")
    Call<List<BakingData>> getBakingRecipeData();
}
