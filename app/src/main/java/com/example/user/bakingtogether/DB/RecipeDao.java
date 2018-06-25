package com.example.user.bakingtogether.DB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.support.annotation.VisibleForTesting;

import java.util.List;

/**
 * Created by Lavinia Dragunoi
 * Abstract class that contains methods to access the database
 */
@Dao
public abstract class RecipeDao {

    @Query("DELETE FROM recipes")
    public abstract void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertRecipe(RecipeEntity recipeEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertIngredients(IngredientEntity ingredientEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertSteps(StepEntity stepEntity);

    @Query("SELECT * FROM recipes")
    public abstract LiveData<List<RecipeEntity>> loadAllRecipes();

    @Query("SELECT * FROM recipes WHERE id = :id")
    @Transaction
    public abstract LiveData<RecipeDetails> getRecipeById(int id);

    @Query("SELECT * FROM recipes")
    @Transaction
    public abstract LiveData<List<RecipeDetails>> getRecipesForWidget();

    @Query("SELECT * FROM recipes WHERE id = :id")
    @Transaction
    public abstract RecipeEntity getRecipeByIdForDetailsActiv(int id);

    @Query("SELECT * FROM IngredientEntity WHERE recipeId = :id")
    public abstract LiveData<List<IngredientEntity>> getIngredientsByRecipeId(int id);

    @Query("SELECT * FROM IngredientEntity WHERE recipeId = :id")
    public abstract List<IngredientEntity> getIngredientsListByRecipeId(int id);

    @Query("SELECT * FROM StepEntity WHERE recipeId = :id")
    public abstract LiveData<List<StepEntity>> getStepsByRecipeId(int id);

    @Query("SELECT * FROM StepEntity WHERE id = :id")
    public abstract LiveData<StepEntity> getStepByStepId(int id);

    //for testing
    @VisibleForTesting
    @Query("SELECT * FROM recipes WHERE name = :name")
    public abstract RecipeEntity findRecipeByName(String name);
}
