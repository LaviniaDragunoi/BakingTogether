package com.example.user.bakingtogether.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.DB.RecipeDetails;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.TheRepository;

import java.util.List;

/**
 * ViewModel for the DetailsActivity
 */
public class DetailsActivityViewModel extends ViewModel {

    private LiveData<RecipeDetails> mRecipeDetails;
    private Integer mRecipeId;
    private TheRepository mRepository;
    private LiveData<List<IngredientEntity>> mIngredientsList;
    private LiveData<List<StepEntity>> mStepsList;

    /**
     * DetailsActivityViewModel constructor that will initialise the instances
     *
     * @param repository
     * @param recipeId
     */
    public DetailsActivityViewModel(TheRepository repository, Integer recipeId) {
        mRepository = repository;
        mRecipeId = recipeId;
        mRecipeDetails = mRepository.getRecipeById(recipeId);
        mIngredientsList = mRepository.getIngredientsByRecipeId(recipeId);
        mStepsList = mRepository.getStepsByRecipeId(recipeId);
    }

    /**
     * Method used for getting and observe the Ingredients list
     *
     * @return a LiveDta list of ingredients
     */
    public LiveData<List<IngredientEntity>> getmIngredientsList() {
        return mIngredientsList;
    }

    /**
     * Method used for setting the value of an LiveData list of Ingredients
     */
    public void setmIngredientsList(LiveData<List<IngredientEntity>> mIngredientsList) {
        this.mIngredientsList = mIngredientsList;
    }

    /**
     * Method used for getting and observe the Steps list
     *
     * @return a LiveDta list of steps
     */
    public LiveData<List<StepEntity>> getmStepsList() {
        return mStepsList;
    }

    /**
     * Method used for setting the value of an LiveData list of steps
     */
    public void setmStepsList(LiveData<List<StepEntity>> mStepsList) {
        this.mStepsList = mStepsList;
    }

    /**
     * Method used for getting and observe the RecipeDetails
     *
     * @return a LiveDta recipeDetails object
     */
    public LiveData<RecipeDetails> getmRecipeDetails() {
        return mRecipeDetails;
    }

    /**
     * Method used for setting the value of an LiveData of an recipeDetails object
     */
    public void setmRecipeDetails(LiveData<RecipeDetails> mRecipeDetails) {
        this.mRecipeDetails = mRecipeDetails;
    }
}
