package com.chandan.android.bakingapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class IngredientsData extends BaseObservable implements Parcelable {

    private final static String Ingredients_Quantity = "quantity";
    private final static String Ingredients_Measure = "measure";
    private final static String Ingredients_Name = "ingredient";

    @SerializedName(Ingredients_Quantity)
    private double ingredientsQuatity;

    @SerializedName(Ingredients_Measure)
    private String ingredientsMeasure;

    @SerializedName(Ingredients_Name)
    private String ingredientsName;

    public IngredientsData() { }

    public IngredientsData(double ingredientsQuatity, String ingredientsMeasure, String ingredientsName) {
        this.ingredientsQuatity = ingredientsQuatity;
        this.ingredientsMeasure = ingredientsMeasure;
        this.ingredientsName = ingredientsName;
    }

    private IngredientsData(Parcel in) {
        ingredientsQuatity = in.readDouble();
        ingredientsMeasure = in.readString();
        ingredientsName = in.readString();
    }

    public static final Creator<IngredientsData> CREATOR = new Creator<IngredientsData>() {
        @Override
        public IngredientsData createFromParcel(Parcel in) {
            return new IngredientsData(in);
        }

        @Override
        public IngredientsData[] newArray(int size) {
            return new IngredientsData[size];
        }
    };

    @Bindable
    public double getIngredientsQuatity() {
        return ingredientsQuatity;
    }

    public void setIngredientsQuatity(Integer ingredientsQuatity) {
        this.ingredientsQuatity = ingredientsQuatity;
    }

    @Bindable
    public String getIngredientsMeasure() {
        return ingredientsMeasure;
    }

    public void setIngredientsMeasure(String ingredientsMeasure) {
        this.ingredientsMeasure = ingredientsMeasure;
    }

    @Bindable
    public String getIngredientsName() {
        return ingredientsName;
    }

    public void setIngredientsName(String ingredientsName) {
        this.ingredientsName = ingredientsName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(ingredientsQuatity);
        dest.writeString(ingredientsMeasure);
        dest.writeString(ingredientsName);
    }
}
