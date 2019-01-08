package com.chandan.android.bakingapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.chandan.android.bakingapp.R;
import com.chandan.android.bakingapp.fragment.RecipeDetailFragment;
import com.chandan.android.bakingapp.fragment.RecipeStepsFragment;
import com.chandan.android.bakingapp.model.BakingData;
import com.chandan.android.bakingapp.model.IngredientsData;
import com.chandan.android.bakingapp.model.RecipeStepsData;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepsMasterActivity extends AppCompatActivity implements RecipeStepsFragment.OnTextViewClickListener {

    private BakingData bakingData = null;
    private Integer stepsCount = 0;

    private static final String INTENT_TITLE_KEY = "Title";
    private static final String STEPS_DATA_KEY = "Data";
    private static final String CURRENT_STEP_KEY = "CurrentPosition";

    private static final String BAKING_DATA_KEY = "baking_data";
    private static final String TWO_PANE_KEY = "two_pane";
    private static final String SELECTED_STEP_KEY = "selected_step";
    private static final String INGREDIENTS_FRAGMENT_KEY = "ingredients_fragment_key";
    private static final String STEP_FRAGMENT_KEY = "step_fragment_key";

    private boolean mTwoPane;
    private Integer selectedStep = 0;

    RecipeStepsFragment ingredientsFragment = null;
    RecipeStepsFragment stepFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_master);

        if (savedInstanceState != null) {
            bakingData = null;
            bakingData = savedInstanceState.getParcelable(BAKING_DATA_KEY);
            mTwoPane = savedInstanceState.getBoolean(TWO_PANE_KEY);
            selectedStep = savedInstanceState.getInt(SELECTED_STEP_KEY);
            ingredientsFragment = (RecipeStepsFragment) getSupportFragmentManager().getFragment(savedInstanceState, INGREDIENTS_FRAGMENT_KEY);
            stepFragment = (RecipeStepsFragment) getSupportFragmentManager().getFragment(savedInstanceState, STEP_FRAGMENT_KEY);
        } else {
            getDataFromIntent();
        }

        if (bakingData != null) {
            populateUI();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BAKING_DATA_KEY, bakingData);
        outState.putBoolean(TWO_PANE_KEY, mTwoPane);
        outState.putInt(SELECTED_STEP_KEY, selectedStep);
        getSupportFragmentManager().putFragment(outState, INGREDIENTS_FRAGMENT_KEY, ingredientsFragment);
        getSupportFragmentManager().putFragment(outState, STEP_FRAGMENT_KEY, stepFragment);
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

        List<IngredientsData> ingredientsDataList = bakingData.getRecipeIngredientsData();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (ingredientsDataList != null) {
            if (ingredientsFragment == null) {
                StringBuilder strBuilder = new StringBuilder("\n" + getString(R.string.indredigents_title) + "\n");
                for (IngredientsData ingredientsData: ingredientsDataList) {
                    String formattedString = ingredientsData.getIngredientsName() + ": "
                            + Double.toString(ingredientsData.getIngredientsQuatity()) + " "
                            + ingredientsData.getIngredientsMeasure() + "\n";

                    strBuilder.append(formattedString);
                }

                ingredientsFragment = new RecipeStepsFragment();
                ingredientsFragment.setRecipeStepsText(strBuilder.toString());
                ingredientsFragment.setRecipeStepCount(stepsCount);
                fragmentManager.beginTransaction()
                        .add(R.id.root_layout_master, ingredientsFragment)
                        .commit();
            }
        }

        List<RecipeStepsData> recipeStepsDataList = bakingData.getRecipeStepsData();

        if (recipeStepsDataList != null) {
            if (stepFragment == null) {
                for (RecipeStepsData recipeStepsData : recipeStepsDataList) {
                    stepsCount++;
                    stepFragment = new RecipeStepsFragment();

                    String text = getString(R.string.step_description_na);
                    if (recipeStepsData.getStepShortDescription() != null && !recipeStepsData.getStepShortDescription().equals("")) {
                        text = recipeStepsData.getStepShortDescription();
                    }

                    String formattedText = "\n" + text + "\n";
                    stepFragment.setRecipeStepsText(formattedText);
                    stepFragment.setRecipeStepCount(stepsCount);
                    fragmentManager.beginTransaction()
                            .add(R.id.root_layout_master, stepFragment)
                            .commit();
                }
            }
        }

        stepsCount = 0;

        if (findViewById(R.id.root_layout_detail) != null) {
            mTwoPane = true;

            RecipeStepsData stepsData = null;
            if (recipeStepsDataList != null) {
                stepsData = recipeStepsDataList.get(selectedStep);

                FragmentManager fragmentManagerDetail = getSupportFragmentManager();

                RecipeDetailFragment detailsFragment = new RecipeDetailFragment();
                detailsFragment.setRecipeStepsData(stepsData);
                fragmentManagerDetail.beginTransaction()
                        .add(R.id.root_layout_detail, detailsFragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onTextViewSelected(int position) {
        if (position == 0) return;

        selectedStep = position - 1;

        if (mTwoPane) {
            RecipeDetailFragment detailsFragment = new RecipeDetailFragment();

            List<RecipeStepsData> stepsDataList = bakingData.getRecipeStepsData();
            detailsFragment.setRecipeStepsData(stepsDataList.get(selectedStep));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root_layout_detail, detailsFragment)
                    .commit();

        } else {
            Context context = RecipeStepsMasterActivity.this;
            Class destinationActivity = RecipeStepsDetailActivity.class;

            List<RecipeStepsData> stepsDataList = bakingData.getRecipeStepsData();

            Bundle bundle = new Bundle();
            bundle.putCharSequence(INTENT_TITLE_KEY, this.getTitle());
            bundle.putParcelableArrayList(STEPS_DATA_KEY, new ArrayList<RecipeStepsData>(stepsDataList));
            bundle.putInt(CURRENT_STEP_KEY, selectedStep);

            Intent intent = new Intent(context, destinationActivity);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
