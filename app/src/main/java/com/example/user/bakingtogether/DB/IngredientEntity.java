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
public class IngredientEntity {
    @PrimaryKey(autoGenerate = true)
    @Expose(deserialize = false, serialize = false)
    private int id;

    @Expose(deserialize = false, serialize = false)
    private Integer recipeId;

    @SerializedName("quantity")
    @Expose
    private Double quantity;

    @SerializedName("measure")
    @Expose
    private String measure;

    @SerializedName("ingredient")
    @Expose
    private String ingredient;

    public IngredientEntity(int recipeId, double quantity, String measure, String ingredient){
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;

    }
    @Ignore
    public IngredientEntity(double quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
