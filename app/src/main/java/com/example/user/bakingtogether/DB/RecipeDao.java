package com.example.user.bakingtogether.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Lavinia Dragunoi on 14-05-2018
 */
@Dao
public abstract class RecipeDao {

    @Insert(onConflict = REPLACE)
    public abstract void insertRecipe(RecipeEntity recipeEntity);

    @Insert(onConflict = REPLACE)
    public abstract void insertIngredients(List<IngredientEntity> ingredientEntityList);

    @Insert(onConflict = REPLACE)
    public abstract void insertSteps(List<StepEntity> stepEntityList);

    @Query("SELECT * FROM recipes")
    public abstract List<RecipeEntity> loadAllRecipes();

    @Query("SELECT * FROM recipes WHERE id = :id")
    @Transaction
    public abstract RecipeDetails getRecipeById(int id);

    @Query("SELECT * FROM IngredientEntity WHERE recipeId = :id")
    public abstract List<IngredientEntity> getIngredientsByRecipeId(int id);

    @Query("SELECT * FROM StepEntity WHERE recipeId = :id")
    public abstract List<StepEntity> getStepsByRecipeId(int id);

}
