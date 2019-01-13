package com.chandan.android.bakingapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.chandan.android.bakingapp.R;
import com.chandan.android.bakingapp.adapter.RecipeStepsListAdapter;
import com.chandan.android.bakingapp.fragment.RecipeDetailFragment;
import com.chandan.android.bakingapp.model.BakingData;
import com.chandan.android.bakingapp.model.RecipeStepsData;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepsListActivity extends AppCompatActivity implements RecipeStepsListAdapter.RecipeStepsClickListener, RecipeDetailFragment.MediaPlayerStateListener {

    private BakingData bakingData = null;
    private RecipeStepsListAdapter mAdapter;
    private boolean mTwoPane;
    private Integer selectedStep = 0;

    private static final String BAKING_DATA_KEY = "data_key";
    private static final String TWO_PANE_KEY = "two_pane";
    private static final String SELECTED_STEP_KEY = "selected_step";

    private static final String INTENT_TITLE_KEY = "Title";
    private static final String STEPS_DATA_KEY = "Data";
    private static final String CURRENT_STEP_KEY = "CurrentPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_list);

        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(BAKING_DATA_KEY)) {
                bakingData = savedInstanceState.getParcelable(BAKING_DATA_KEY);
                mTwoPane = savedInstanceState.getBoolean(TWO_PANE_KEY);
                selectedStep = savedInstanceState.getInt(SELECTED_STEP_KEY);
            } else {
                getDataFromIntent();
            }
        } else {
            getDataFromIntent();
        }

        if (bakingData != null) {
            populateUI();
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BAKING_DATA_KEY, bakingData);
        outState.putBoolean(TWO_PANE_KEY, mTwoPane);
        outState.putInt(SELECTED_STEP_KEY, selectedStep);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            bakingData = (BakingData) intent.getParcelableExtra(Intent.EXTRA_TEXT);
        }
    }

    private void populateUI() {

        if (bakingData.getRecipeName() != null && !bakingData.getRecipeName().equals("")) {
            setTitle(bakingData.getRecipeName());
        } else {
            setTitle(getString(R.string.recipe_list_activity_screen_title));
        }

        setupRecyclerView();

        if (findViewById(R.id.root_layout_detail) != null) {
            mTwoPane = true;

            RecipeStepsData stepsData = null;
            if (bakingData.getRecipeStepsData() != null) {
                stepsData = bakingData.getRecipeStepsData().get(selectedStep);

                FragmentManager fragmentManagerDetail = getSupportFragmentManager();

                RecipeDetailFragment detailsFragment = new RecipeDetailFragment();
                detailsFragment.setRecipeStepsData(stepsData);
                fragmentManagerDetail.beginTransaction()
                        .add(R.id.root_layout_detail, detailsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    private void setupRecyclerView() {
        RecyclerView mRecipeList = (RecyclerView) findViewById(R.id.recipe_steps_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecipeList.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecipeList.getContext(),
                layoutManager.getOrientation());
        mRecipeList.addItemDecoration(mDividerItemDecoration);

        mRecipeList.setHasFixedSize(true);

        mAdapter = new RecipeStepsListAdapter(this);
        mRecipeList.setAdapter(mAdapter);

        updateAdapter();
    }

    private void updateAdapter() {
        mAdapter.setupData(bakingData.getRecipeIngredientsData(), bakingData.getRecipeStepsData());
    }

    @Override
    public void onRecipeStepsClick(int clickedItemIndex) {

        selectedStep = clickedItemIndex;

        if (mTwoPane) {

            RecipeDetailFragment detailsFragment = new RecipeDetailFragment();

            List<RecipeStepsData> stepsDataList = bakingData.getRecipeStepsData();
            detailsFragment.setRecipeStepsData(stepsDataList.get(selectedStep));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root_layout_detail, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Context context = RecipeStepsListActivity.this;
            Class destinationActivity = RecipeStepsDetailActivity.class;

            List<RecipeStepsData> stepsDataList = bakingData.getRecipeStepsData();

            Bundle bundle = new Bundle();
            bundle.putCharSequence(INTENT_TITLE_KEY, this.getTitle());
            bundle.putParcelableArrayList(STEPS_DATA_KEY, new ArrayList<RecipeStepsData>(stepsDataList));
            bundle.putInt(CURRENT_STEP_KEY, clickedItemIndex);

            Intent intent = new Intent(context, destinationActivity);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMediaPlayerError(String error) {
        showToastMessage(error);
    }

    @Override
    public void onMediaPlayerNoUrlAvailable() {
        showToastMessage(getString(R.string.video_url_na));
    }
}
