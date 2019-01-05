package com.chandan.android.bakingapp.fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chandan.android.bakingapp.R;
import com.chandan.android.bakingapp.activity.RecipeStepsDetailActivity;
import com.chandan.android.bakingapp.databinding.FragmentRecipeDetailBinding;
import com.chandan.android.bakingapp.model.RecipeStepsData;
import com.squareup.picasso.Picasso;

public class RecipeDetailFragment extends Fragment {

    public static final String RECIPE_LIST_KEY = "recipe_list_key";

    private RecipeStepsData recipeStepsData = null;

    OnImageViewClickListener mCallback;

    public interface OnImageViewClickListener {
        void onImageViewSelected(String urlString);
    }

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    public void setRecipeStepsData(RecipeStepsData recipeStepsData) {
        this.recipeStepsData = recipeStepsData;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            recipeStepsData = savedInstanceState.getParcelable(RECIPE_LIST_KEY);
        }

        FragmentRecipeDetailBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_recipe_detail, container, false);
        View rootView = binding.getRoot();

        binding.setRecipestepsdata(recipeStepsData);

        binding.videoPreviewImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipeStepsData != null) {
                    mCallback.onImageViewSelected(recipeStepsData.getStepVideoUrl());
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnImageViewClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        currentState.putParcelable(RECIPE_LIST_KEY, recipeStepsData);
    }
}
