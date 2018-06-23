package com.example.user.bakingtogether.utils;

import com.example.user.bakingtogether.DB.RecipeEntity;

public class TestUtil  {
    public static RecipeEntity createRecipeEntity(int id, String name, int servings, String image){
        return new RecipeEntity(id, name, servings, image);
    }
}
