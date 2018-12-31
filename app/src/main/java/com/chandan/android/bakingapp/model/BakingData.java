package com.chandan.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BakingData implements Parcelable {

    private final static String Recipe_Id = "id";
    private final static String Recipe_Name = "name";
    private final static String Recipe_Total_Servings = "servings";
    private final static String Recipe_Image_Path = "image";
    private final static String Recipe_Ingredients = "ingredients";
    private final static String Recipe_Steps = "steps";

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

    public String getRecipeImagePath() {
        return recipeImagePath;
    }

    public void setRecipeImagePath(String recipeImagePath) {
        this.recipeImagePath = recipeImagePath;
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
