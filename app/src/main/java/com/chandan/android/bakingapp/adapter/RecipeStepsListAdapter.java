package com.chandan.android.bakingapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chandan.android.bakingapp.R;
import com.chandan.android.bakingapp.databinding.RecipeStepsListBinding;
import com.chandan.android.bakingapp.model.IngredientsData;
import com.chandan.android.bakingapp.model.RecipeStepsData;

import java.util.List;

public class RecipeStepsListAdapter extends RecyclerView.Adapter<RecipeStepsListAdapter.RecipeStepsListHolder> {

    final private RecipeStepsClickListener mOnClickListener;
    private LayoutInflater layoutInflater;

    private List<IngredientsData> ingredientsDataList = null;
    private List<RecipeStepsData> recipeStepsList = null;

    public interface RecipeStepsClickListener {
        void onRecipeStepsClick(int clickedItemIndex);
    }

    public RecipeStepsListAdapter(RecipeStepsClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public void setupData(List<IngredientsData> ingredientDataList, List<RecipeStepsData> stepsDataList) {
        this.ingredientsDataList = ingredientDataList;
        this.recipeStepsList = stepsDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeStepsListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }

        RecipeStepsListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.recipe_steps_list, viewGroup, false);
        return new RecipeStepsListAdapter.RecipeStepsListHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsListHolder recipeStepsListHolder, final int position) {

        if (position == 0) { //Ingrdients
            Integer ingredientsCount = 0;

            String shortDescText = "\n" + recipeStepsListHolder.binding.getRoot().getResources().getString(R.string.indredigents_title);
            recipeStepsListHolder.binding.recipeDetailTextView.setText(shortDescText);

            StringBuilder strBuilder = new StringBuilder("");
            for (IngredientsData ingredientsData: ingredientsDataList) {
                ingredientsCount++;
                String formattedString = Integer.toString(ingredientsCount) + ". " +
                        ingredientsData.getIngredientsName() + ": "
                        + Double.toString(ingredientsData.getIngredientsQuatity()) + " "
                        + ingredientsData.getIngredientsMeasure() + "\n";

                strBuilder.append(formattedString);
            }

            recipeStepsListHolder.binding.recipeDetailDescriptionTextView.setText(strBuilder.toString());
            recipeStepsListHolder.binding.navigationImage.setVisibility(View.GONE);

        } else { //Steps
            RecipeStepsData recipeStepsData = recipeStepsList.get(position - 1);
            String formattedStr = "\n" + recipeStepsData.getStepShortDescription();

            recipeStepsListHolder.binding.recipeDetailTextView.setText(formattedStr);
            recipeStepsListHolder.binding.recipeDetailDescriptionTextView.setText(recipeStepsData.getStepDescription());
        }

        recipeStepsListHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null && position != 0) {
                    mOnClickListener.onRecipeStepsClick(position - 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.recipeStepsList.size() + 1;
    }

    class RecipeStepsListHolder extends RecyclerView.ViewHolder {

        private final RecipeStepsListBinding binding;

        RecipeStepsListHolder(RecipeStepsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
