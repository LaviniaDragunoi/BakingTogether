package com.example.user.bakingtogether.DB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("id"), @Index("recipeId")},
            foreignKeys = @ForeignKey(entity = RecipeEntity.class,
                    parentColumns = "id",
                    childColumns = "recipeId",
                    onDelete = CASCADE))
    public class StepEntity{
        @PrimaryKey(autoGenerate = true)
        @Expose(deserialize = false, serialize = false)
        private int id;

        @Expose(deserialize = false, serialize = false)
        private Integer recipeId;

        @SerializedName("shortDescription")
        @Expose
        private String shortDescription;

        @SerializedName("description")
        @Expose
        private String description;

        @SerializedName("videoURL")
        @Expose
        private String videoURL;
    @SerializedName("thumbnailURL")
    @Expose
    private String thumbnailURL;

        public StepEntity(int recipeId, String shortDescription, String description, String videoURL, String thumbnailURL){
            this.recipeId = recipeId;
            this.shortDescription = shortDescription;
            this.description = description;
            this.videoURL = videoURL;
            this.thumbnailURL = thumbnailURL;

        }

        @Ignore
        public StepEntity(int id,int recipeId, String shortDescription, String description, String videoURL, String thumbnailURL){
            this.recipeId = recipeId;
            this.id = id;
            this.shortDescription = shortDescription;
            this.description = description;
            this.videoURL = videoURL;
            this.thumbnailURL = thumbnailURL;

        }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
