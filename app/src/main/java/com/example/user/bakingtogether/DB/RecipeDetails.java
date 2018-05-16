package com.example.user.bakingtogether.DB;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetails {
    @Embedded
    private RecipeEntity recipe;
    @Relation(parentColumn = "id",
              entityColumn = "recipeId", entity = IngredientEntity.class)
    private List<IngredientEntity> ingredients = new ArrayList<>();

    @Relation(parentColumn = "id",
               entityColumn = "recipeId", entity = StepEntity.class)
    private List<StepEntity> steps = new ArrayList<>();

    public RecipeEntity getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeEntity recipe) {
        this.recipe = recipe;
    }

    public List<IngredientEntity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }

    public List<StepEntity> getSteps() {
        return steps;
    }

    public void setSteps(List<StepEntity> steps) {
        this.steps = steps;
    }
}
