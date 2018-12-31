package com.chandan.android.bakingapp.utilities;

import android.support.annotation.NonNull;

import com.chandan.android.bakingapp.model.BakingData;
import com.chandan.android.bakingapp.rest.BakingApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    private final static String BAKING_RECIPE_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/";

    private static Retrofit retrofit = null;

    public static void getBakingData(final Callback<List<BakingData>> arrayCallback) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BAKING_RECIPE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        BakingApiService bakingApiService = retrofit.create(BakingApiService.class);

        Call<List<BakingData>> call = bakingApiService.getBakingRecipeData();

        call.enqueue(new Callback<List<BakingData>>() {

            @Override
            public void onResponse(@NonNull Call<List<BakingData>> call, @NonNull Response<List<BakingData>> response) {
                arrayCallback.onResponse(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<List<BakingData>> call, @NonNull Throwable throwable) {
                arrayCallback.onFailure(call, throwable);
            }
        });
    }
}
