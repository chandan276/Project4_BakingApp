package com.chandan.android.bakingapp.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.chandan.android.bakingapp.R;
import com.chandan.android.bakingapp.fragment.RecipeDetailFragment;
import com.chandan.android.bakingapp.model.RecipeStepsData;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepsDetailActivity extends AppCompatActivity implements RecipeDetailFragment.MediaPlayerStateListener {

    private List<RecipeStepsData> recipeStepsDataList = null;
    private Integer recipeStepCurrentPosition = 0;

    private static final String INTENT_TITLE_KEY = "Title";
    private static final String STEPS_DATA_KEY = "Data";
    private static final String CURRENT_STEP_KEY = "CurrentPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_detail);

        if(savedInstanceState == null) {
            setTitle(getIntent().getCharSequenceExtra(INTENT_TITLE_KEY));
            recipeStepsDataList = getIntent().getParcelableArrayListExtra(STEPS_DATA_KEY);
            recipeStepCurrentPosition = getIntent().getIntExtra(CURRENT_STEP_KEY, 0);
        }

        if (recipeStepsDataList != null) {
            populateUI();
        }
    }

    public void populateUI() {
        RecipeStepsData stepsData = recipeStepsDataList.get(recipeStepCurrentPosition);

        FragmentManager fragmentManager = getSupportFragmentManager();

        RecipeDetailFragment detailsFragment = new RecipeDetailFragment();
        detailsFragment.setRecipeStepsData(stepsData);
        fragmentManager.beginTransaction()
                .add(R.id.root_layout_detail, detailsFragment)
                .commit();
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    // ExoPlayer Event Callbacks
    @Override
    public void onMediaPlayerError(String error) {
        showToastMessage(error);
    }

    @Override
    public void onMediaPlayerNoUrlAvailable() {
        showToastMessage(getString(R.string.video_url_na));
    }
}
