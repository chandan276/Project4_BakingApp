package com.chandan.android.bakingapp.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.chandan.android.bakingapp.R;
import com.chandan.android.bakingapp.adapter.RecipeListAdapter;
import com.chandan.android.bakingapp.model.BakingData;
import com.chandan.android.bakingapp.utilities.NetworkUtils;
import com.chandan.android.bakingapp.utilities.ProgressIndicatorHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends AppCompatActivity implements RecipeListAdapter.RecipeItemClickListener {

    private static final int GRID_SPAN = 1;
    private RecipeListAdapter mAdapter;

    private List<BakingData> bakingData = new ArrayList<>();

    private static final String RESPONSE_CALLBACKS_TEXT_KEY = "callbacks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        setTitle(R.string.recipe_list_activity_screen_title);

        initializeUI();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RESPONSE_CALLBACKS_TEXT_KEY)) {
                bakingData.clear();
                bakingData = savedInstanceState.getParcelableArrayList(RESPONSE_CALLBACKS_TEXT_KEY);
                updateAdapter();
            } else {
                getBakingRecipeData();
            }
        } else {
            getBakingRecipeData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RESPONSE_CALLBACKS_TEXT_KEY, new ArrayList<BakingData>(bakingData));
    }

    private void initializeUI() {
        RecyclerView mRecipeList = (RecyclerView) findViewById(R.id.recipe_recyclerview);

        GridLayoutManager layoutManager = new GridLayoutManager(this, GRID_SPAN);
        mRecipeList.setLayoutManager(layoutManager);

        mRecipeList.setHasFixedSize(true);

        mAdapter = new RecipeListAdapter(this);
        mRecipeList.setAdapter(mAdapter);
    }

    private void getBakingRecipeData() {
        ProgressIndicatorHandler.showProgressIndicator(this, getString(R.string.progress_indicator_home_label), getString(R.string.progress_indicator_home_detail_label), true);
        NetworkUtils.getBakingData(new Callback<List<BakingData>>() {
            @Override
            public void onResponse(@NonNull Call<List<BakingData>> call, @NonNull Response<List<BakingData>> response) {
                if (response.body() != null) {
                    bakingData = response.body();
                    updateAdapter();
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

    private void updateAdapter() {
        mAdapter.updateBakingListData(bakingData);
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRecipeItemClick(int clickedItemIndex) {
        Log.d("dklavs", "asfad");
    }
}
