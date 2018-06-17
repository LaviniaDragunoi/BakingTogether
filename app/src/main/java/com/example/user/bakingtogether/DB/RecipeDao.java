package com.example.user.bakingtogether.DB;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

/**
 * Created by Lavinia Dragunoi on 14-05-2018
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
    public abstract List<RecipeDetails> getRecipesForWidget();

    @Query("SELECT * FROM recipes WHERE id = :id")
    @Transaction
    public abstract RecipeEntity getRecipeByIdForWidget(int id);

    @Query("SELECT * FROM IngredientEntity WHERE recipeId = :id")
    public abstract LiveData<List<IngredientEntity>> getIngredientsByRecipeId(int id);

    @Query("SELECT * FROM IngredientEntity WHERE recipeId = :id")
    public abstract List<IngredientEntity> getIngredientsListByRecipeId(int id);

 @Query("SELECT * FROM IngredientEntity WHERE recipeId = :id")
 public abstract IngredientEntity getIngredientByRecipeId(int id);

    @Query("SELECT * FROM StepEntity WHERE recipeId = :id")
    public abstract LiveData<List<StepEntity>> getStepsByRecipeId(int id);

    @Query("SELECT * FROM StepEntity WHERE recipeId = :id")
    public abstract List<StepEntity> getStepsListByRecipeId(int id);

    @Query("SELECT * FROM StepEntity WHERE id = :id")
    public abstract LiveData<StepEntity> getStepByStepId(int id);


}
