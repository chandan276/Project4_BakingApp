package com.chandan.android.bakingapp.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.chandan.android.bakingapp.R;
import com.chandan.android.bakingapp.fragment.RecipeStepsFragment;
import com.chandan.android.bakingapp.model.BakingData;
import com.chandan.android.bakingapp.model.IngredientsData;
import com.chandan.android.bakingapp.model.RecipeStepsData;

import java.util.List;

public class RecipeStepsMasterActivity extends AppCompatActivity {

    private BakingData bakingData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_master);

        getDataFromIntent();
        populateUI();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            bakingData = (BakingData) intent.getParcelableExtra(Intent.EXTRA_TEXT);
        }
    }

    private void populateUI() {
        setTitle(bakingData.getRecipeName());

        List<IngredientsData> ingredientsDataList = bakingData.getRecipeIngredientsData();

        StringBuilder strBuilder = new StringBuilder("");
        for (IngredientsData ingredientsData: ingredientsDataList) {
            String formattedString = ingredientsData.getIngredientsName() + ": "
                    + Double.toString(ingredientsData.getIngredientsQuatity()) + " "
                    + ingredientsData.getIngredientsMeasure() + "\n";

            strBuilder.append(formattedString);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        RecipeStepsFragment stepsFragment = new RecipeStepsFragment();
        stepsFragment.setRecipeStepsText(strBuilder.toString());
        fragmentManager.beginTransaction()
                .add(R.id.root_layout, stepsFragment)
                .commit();

        List<RecipeStepsData> recipeStepsDataList = bakingData.getRecipeStepsData();

        for (RecipeStepsData recipeStepsData : recipeStepsDataList) {
            RecipeStepsFragment stepFragment = new RecipeStepsFragment();
            String text = "\n\n" + recipeStepsData.getStepShortDescription() + "\n\n";
            stepFragment.setRecipeStepsText(text);
            fragmentManager.beginTransaction()
                    .add(R.id.root_layout, stepFragment)
                    .commit();
        }
    }
}
