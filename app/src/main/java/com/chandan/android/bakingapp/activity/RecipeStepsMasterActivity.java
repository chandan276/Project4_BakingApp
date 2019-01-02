package com.chandan.android.bakingapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.chandan.android.bakingapp.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_master);

        getDataFromIntent();

        if (bakingData != null) {
            populateUI();
        }
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            bakingData = (BakingData) intent.getParcelableExtra(Intent.EXTRA_TEXT);
        }
    }

    private void populateUI() {

        if (bakingData.getRecipeName() != null || !bakingData.getRecipeName().equals("")) {
            setTitle(bakingData.getRecipeName());
        } else {
            setTitle(getString(R.string.recipe_list_activity_screen_title));
        }

        List<IngredientsData> ingredientsDataList = bakingData.getRecipeIngredientsData();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (ingredientsDataList != null) {
            StringBuilder strBuilder = new StringBuilder("\n" + getString(R.string.indredigents_title) + "\n");
            for (IngredientsData ingredientsData: ingredientsDataList) {
                String formattedString = ingredientsData.getIngredientsName() + ": "
                        + Double.toString(ingredientsData.getIngredientsQuatity()) + " "
                        + ingredientsData.getIngredientsMeasure() + "\n";

                strBuilder.append(formattedString);
            }

            RecipeStepsFragment stepsFragment = new RecipeStepsFragment();
            stepsFragment.setRecipeStepsText(strBuilder.toString());
            stepsFragment.setRecipeStepCount(stepsCount);
            fragmentManager.beginTransaction()
                    .add(R.id.root_layout, stepsFragment)
                    .commit();
        }

        List<RecipeStepsData> recipeStepsDataList = bakingData.getRecipeStepsData();

        if (recipeStepsDataList != null) {
            for (RecipeStepsData recipeStepsData : recipeStepsDataList) {
                stepsCount++;
                RecipeStepsFragment stepFragment = new RecipeStepsFragment();

                String text = getString(R.string.step_description_na);
                if (recipeStepsData.getStepShortDescription() != null || !recipeStepsData.getStepShortDescription().equals("")) {
                    text = recipeStepsData.getStepShortDescription();
                }

                String formattedText = "\n" + text + "\n";
                stepFragment.setRecipeStepsText(formattedText);
                stepFragment.setRecipeStepCount(stepsCount);
                fragmentManager.beginTransaction()
                        .add(R.id.root_layout, stepFragment)
                        .commit();
            }
        }

        stepsCount = 0;
    }

    @Override
    public void onTextViewSelected(int position) {
        if (position == 0) return;

        Context context = RecipeStepsMasterActivity.this;
        Class destinationActivity = RecipeStepsDetailActivity.class;

        List<RecipeStepsData> stepsDataList = bakingData.getRecipeStepsData();

        Bundle bundle = new Bundle();
        bundle.putCharSequence(INTENT_TITLE_KEY, this.getTitle());
        bundle.putParcelableArrayList(STEPS_DATA_KEY, new ArrayList<RecipeStepsData>(stepsDataList));
        bundle.putInt(CURRENT_STEP_KEY, position - 1);

        Intent intent = new Intent(context, destinationActivity);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
