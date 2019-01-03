package com.chandan.android.bakingapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chandan.android.bakingapp.R;
import com.chandan.android.bakingapp.databinding.RecipeListCardviewBinding;
import com.chandan.android.bakingapp.model.BakingData;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    final private RecipeItemClickListener mOnClickListener;
    private LayoutInflater layoutInflater;

    private List<BakingData> recipeDataList = new ArrayList<>();

    public interface RecipeItemClickListener {
        void onRecipeItemClick(int clickedItemIndex);
    }

    public RecipeListAdapter(RecipeItemClickListener listener) {
        mOnClickListener = listener;
    }

    public void updateBakingListData(List<BakingData> recipeData) {
        this.recipeDataList = recipeData;
        notifyDataSetChanged();
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        RecipeListCardviewBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.recipe_list_cardview, parent, false);
        return new RecipeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, final int position) {
        holder.binding.setBakingdata(recipeDataList.get(position));

        holder.binding.recipeNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onRecipeItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeDataList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        private final RecipeListCardviewBinding binding;

        RecipeViewHolder(RecipeListCardviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
