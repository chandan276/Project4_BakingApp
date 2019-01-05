package com.chandan.android.bakingapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.chandan.android.bakingapp.R;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BakingData extends BaseObservable implements Parcelable {

    private final static String Recipe_Id = "id";
    private final static String Recipe_Name = "name";
    private final static String Recipe_Total_Servings = "servings";
    private final static String Recipe_Image_Path = "image";
    private final static String Recipe_Ingredients = "ingredients";
    private final static String Recipe_Steps = "steps";

    private static final String DUMMY_IMAGE_PATH = "https://www.google.com";

    @SerializedName(Recipe_Id)
    private Integer recipeId;

    @SerializedName(Recipe_Name)
    private String recipeName;

    @SerializedName(Recipe_Total_Servings)
    private String recipeTotalServings;

    @SerializedName(Recipe_Image_Path)
    private String recipeImagePath;

    @SerializedName(Recipe_Ingredients)
    private List<IngredientsData> recipeIngredientsData;

    @SerializedName(Recipe_Steps)
    private List<RecipeStepsData> recipeStepsData;

    public BakingData() { }

    public BakingData(Integer recipeId, String recipeName, String recipeTotalServings, String recipeImagePath, List<IngredientsData> recipeIngredientsData, List<RecipeStepsData> recipeStepsData) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeTotalServings = recipeTotalServings;
        this.recipeImagePath = recipeImagePath;
        this.recipeIngredientsData = recipeIngredientsData;
        this.recipeStepsData = recipeStepsData;
    }

    private BakingData(Parcel in) {
        if (in.readByte() == 0) {
            recipeId = null;
        } else {
            recipeId = in.readInt();
        }
        recipeName = in.readString();
        recipeTotalServings = in.readString();
        recipeImagePath = in.readString();
        recipeIngredientsData = in.createTypedArrayList(IngredientsData.CREATOR);
        recipeStepsData = in.createTypedArrayList(RecipeStepsData.CREATOR);
    }

    public static final Creator<BakingData> CREATOR = new Creator<BakingData>() {
        @Override
        public BakingData createFromParcel(Parcel in) {
            return new BakingData(in);
        }

        @Override
        public BakingData[] newArray(int size) {
            return new BakingData[size];
        }
    };

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    @Bindable
    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeTotalServings() {
        return recipeTotalServings;
    }

    public void setRecipeTotalServings(String recipeTotalServings) {
        this.recipeTotalServings = recipeTotalServings;
    }

    @Bindable
    public String getRecipeImagePath() {
        return recipeImagePath;
    }

    public void setRecipeImagePath(String recipeImagePath) {
        this.recipeImagePath = recipeImagePath;
    }

    @BindingAdapter({"bind:recipeImagePath"})
    public static void loadImage(ImageView view, String imageUrl) {
        String path = DUMMY_IMAGE_PATH;
        if (imageUrl.length() != 0) {
            path = imageUrl;
        }

        Picasso.with(view.getContext())
                .load(path)
                .into(view);
    }

    public List<IngredientsData> getRecipeIngredientsData() {
        return recipeIngredientsData;
    }

    public void setRecipeIngredientsData(List<IngredientsData> recipeIngredientsData) {
        this.recipeIngredientsData = recipeIngredientsData;
    }

    public List<RecipeStepsData> getRecipeStepsData() {
        return recipeStepsData;
    }

    public void setRecipeStepsData(List<RecipeStepsData> recipeStepsData) {
        this.recipeStepsData = recipeStepsData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (recipeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(recipeId);
        }
        dest.writeString(recipeName);
        dest.writeString(recipeTotalServings);
        dest.writeString(recipeImagePath);
        dest.writeTypedList(recipeIngredientsData);
        dest.writeTypedList(recipeStepsData);
    }
}
