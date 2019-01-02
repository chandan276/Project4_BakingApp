package com.chandan.android.bakingapp.fragment;


import android.content.Context;
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
import com.chandan.android.bakingapp.model.RecipeStepsData;
import com.squareup.picasso.Picasso;

public class RecipeDetailFragment extends Fragment {

    private static final String DUMMY_IMAGE_PATH = "https://www.google.com";

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

        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        final ImageView imageView = (ImageView) rootView.findViewById(R.id.video_preview_imageview);
        final TextView textView = (TextView) rootView.findViewById(R.id.recipe_detail_text_view);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipeStepsData != null) {
                    mCallback.onImageViewSelected(recipeStepsData.getStepVideoUrl());
                }
            }
        });

        if (recipeStepsData != null) {
            String path = DUMMY_IMAGE_PATH;
            if (recipeStepsData.getStepThumbnailUrl().length() != 0) {
                path = recipeStepsData.getStepThumbnailUrl();
            }
            Picasso.with(container.getContext())
                    .load(path)
                    .placeholder(R.drawable.preview_not_available)
                    .error(R.drawable.preview_not_available)
                    .into(imageView);

            textView.setText(recipeStepsData.getStepDescription());
        }

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
}
