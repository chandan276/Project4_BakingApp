package com.chandan.android.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chandan.android.bakingapp.R;
import com.chandan.android.bakingapp.model.BakingData;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    final private RecipeItemClickListener mOnClickListener;

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
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_list_cardview;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        BakingData bakingData = recipeDataList.get(position);
        holder.recipeNameTextView.setText(bakingData.getRecipeName());
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return recipeDataList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView recipeNameTextView;

        RecipeViewHolder(View itemView) {
            super(itemView);
            recipeNameTextView = (TextView) itemView.findViewById(R.id.recipe_name_text_view);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onRecipeItemClick(clickedPosition);
        }
    }
}
