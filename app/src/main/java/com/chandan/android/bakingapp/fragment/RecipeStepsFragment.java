package com.chandan.android.bakingapp.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chandan.android.bakingapp.R;
import com.chandan.android.bakingapp.databinding.ActivityRecipeStepsDetailBinding;
import com.chandan.android.bakingapp.databinding.FragmentRecipeDetailBinding;

public class RecipeStepsFragment extends Fragment {

    public static final String LIST_TEXT_KEY = "list_text_key";

    private String recipeStepsText;
    private Integer recipeStepCount;

    OnTextViewClickListener mCallback;

    public interface OnTextViewClickListener {
        void onTextViewSelected(int position);
    }

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

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onTextViewSelected(recipeStepCount);
            }
        });

        return rootView;
    }

    public void setRecipeStepsText(String recipeStepsText) {
        this.recipeStepsText = recipeStepsText;
    }

    public void setRecipeStepCount(Integer recipeStepCount) {
        this.recipeStepCount = recipeStepCount;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        currentState.putCharSequence(LIST_TEXT_KEY, recipeStepsText);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnTextViewClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }
}
