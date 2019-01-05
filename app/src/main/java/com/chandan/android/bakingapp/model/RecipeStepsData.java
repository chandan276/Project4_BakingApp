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

public class RecipeStepsData extends BaseObservable implements Parcelable {

    private final static String Step_Id = "id";
    private final static String Step_ShortDescription = "shortDescription";
    private final static String Step_Description = "description";
    private final static String Step_VideoUrl = "videoURL";
    private final static String Step_ThumbnailUrl = "thumbnailURL";

    private static final String DUMMY_IMAGE_PATH = "https://www.google.com";

    @SerializedName(Step_Id)
    private Integer stepId;

    @SerializedName(Step_ShortDescription)
    private String stepShortDescription;

    @SerializedName(Step_Description)
    private String stepDescription;

    @SerializedName(Step_VideoUrl)
    private String stepVideoUrl;

    @SerializedName(Step_ThumbnailUrl)
    private String stepThumbnailUrl;

    public RecipeStepsData() { }

    public RecipeStepsData(Integer stepId, String stepShortDescription, String stepDescription, String stepVideoUrl, String stepThumbnailUrl) {
        this.stepId = stepId;
        this.stepShortDescription = stepShortDescription;
        this.stepDescription = stepDescription;
        this.stepVideoUrl = stepVideoUrl;
        this.stepThumbnailUrl = stepThumbnailUrl;
    }

    private RecipeStepsData(Parcel in) {
        if (in.readByte() == 0) {
            stepId = null;
        } else {
            stepId = in.readInt();
        }
        stepShortDescription = in.readString();
        stepDescription = in.readString();
        stepVideoUrl = in.readString();
        stepThumbnailUrl = in.readString();
    }

    public static final Creator<RecipeStepsData> CREATOR = new Creator<RecipeStepsData>() {
        @Override
        public RecipeStepsData createFromParcel(Parcel in) {
            return new RecipeStepsData(in);
        }

        @Override
        public RecipeStepsData[] newArray(int size) {
            return new RecipeStepsData[size];
        }
    };

    public Integer getStepId() {
        return stepId;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    @Bindable
    public String getStepShortDescription() {
        return stepShortDescription;
    }

    public void setStepShortDescription(String stepShortDescription) {
        this.stepShortDescription = stepShortDescription;
    }

    @Bindable
    public String getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    @Bindable
    public String getStepVideoUrl() {
        return stepVideoUrl;
    }

    public void setStepVideoUrl(String stepVideoUrl) {
        this.stepVideoUrl = stepVideoUrl;
    }

    @Bindable
    public String getStepThumbnailUrl() {
        return stepThumbnailUrl;
    }

    public void setStepThumbnailUrl(String stepThumbnailUrl) {
        this.stepThumbnailUrl = stepThumbnailUrl;
    }

    @BindingAdapter({"bind:stepThumbnailUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        String path = DUMMY_IMAGE_PATH;
        if (imageUrl.length() != 0) {
            path = imageUrl;
        }
        Picasso.with(view.getContext())
                .load(path)
                .placeholder(R.drawable.preview_not_available)
                .error(R.drawable.preview_not_available)
                .into(view);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        if (stepId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(stepId);
        }
        dest.writeString(stepShortDescription);
        dest.writeString(stepDescription);
        dest.writeString(stepVideoUrl);
        dest.writeString(stepThumbnailUrl);
    }
}
