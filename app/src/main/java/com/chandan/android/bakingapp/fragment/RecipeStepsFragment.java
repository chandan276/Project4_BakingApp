package com.chandan.android.bakingapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chandan.android.bakingapp.R;

public class RecipeStepsFragment extends Fragment {

    public static final String LIST_TEXT_KEY = "list_text_key";

    private String recipeStepsText;

    public RecipeStepsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            recipeStepsText = savedInstanceState.getString(LIST_TEXT_KEY);
        }

        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        final TextView textView = (TextView) rootView.findViewById(R.id.recipe_step_text_view);

        if (recipeStepsText != null) {
            textView.setText(recipeStepsText);
        }

        return rootView;
    }

    public void setRecipeStepsText(String recipeStepsText) {
        this.recipeStepsText = recipeStepsText;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        currentState.putCharSequence(LIST_TEXT_KEY, recipeStepsText);
    }
}
