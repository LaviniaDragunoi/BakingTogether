package com.example.user.bakingtogether.DB;
/**
 * Created by Lavinia Dragunoi on 14-05-2018
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.user.bakingtogether.data.Ingredient;
import com.example.user.bakingtogether.data.Step;

import java.util.List;

@Entity(tableName = "recipes")
public class RecipeEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int servings;
    private String name, image;

    public RecipeEntity(int id, String name, int servings, String image){
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
