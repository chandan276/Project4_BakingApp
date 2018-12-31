package com.chandan.android.bakingapp.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.chandan.android.bakingapp.R;
import com.chandan.android.bakingapp.model.BakingData;
import com.chandan.android.bakingapp.model.BakingDataResponse;
import com.chandan.android.bakingapp.utilities.NetworkUtils;
import com.chandan.android.bakingapp.utilities.ProgressIndicatorHandler;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        setTitle(R.string.recipe_list_activity_screen_title);

        getBakingRecipeData();
    }

    private void getBakingRecipeData() {
        ProgressIndicatorHandler.showProgressIndicator(getApplicationContext(), getString(R.string.progress_indicator_home_label), getString(R.string.progress_indicator_home_detail_label), true);
        NetworkUtils.getBakingData(new Callback<List<BakingData>>() {
            @Override
            public void onResponse(@NonNull Call<List<BakingData>> call, @NonNull Response<List<BakingData>> response) {
                if (response.body() != null) {

                } else {
                    showToastMessage(getString(R.string.network_error));
                }
                ProgressIndicatorHandler.hideProgressIndicator();
            }

            @Override
            public void onFailure(@NonNull Call<List<BakingData>> call, @NonNull Throwable t) {
                ProgressIndicatorHandler.hideProgressIndicator();
                showToastMessage(t.getLocalizedMessage());
            }
        });
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
