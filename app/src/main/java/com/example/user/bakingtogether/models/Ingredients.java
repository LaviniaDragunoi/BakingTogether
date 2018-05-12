package com.example.user.bakingtogether.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lavinia Dragunoi on 7-05-2018
 */
public class Ingredients {

    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    @SerializedName("measure")
    @Expose
    private Integer measure;

    @SerializedName("ingredient")
    @Expose
    private String ingredient;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getMeasure() {
        return measure;
    }

    public void setMeasure(Integer measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
