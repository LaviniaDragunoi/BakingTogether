package com.example.user.bakingtogether.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.DB.RecipeDetails;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.TheRepository;

import java.util.List;

public class DetailsActivityViewModel extends ViewModel{

private LiveData<RecipeDetails> mRecipeDetails;
private Integer mRecipeId;
private TheRepository mRepository;
private LiveData<List<IngredientEntity>> mIngredientsList;
private LiveData<List<StepEntity>> mStepsList;


    public DetailsActivityViewModel(TheRepository repository, Integer recipeId){
        mRepository = repository;
        mRecipeId = recipeId;
        mRecipeDetails = mRepository.getRecipeById(recipeId);
        mIngredientsList = mRepository.getIngredientsByRecipeId(recipeId);
        mStepsList = mRepository.getStepsByRecipeId(recipeId);

    }

    public LiveData<List<IngredientEntity>> getmIngredientsList() {
        return mIngredientsList;
    }

    public void setmIngredientsList(LiveData<List<IngredientEntity>> mIngredientsList) {
        this.mIngredientsList = mIngredientsList;
    }

    public LiveData<List<StepEntity>> getmStepsList() {
        return mStepsList;
    }

    public void setmStepsList(LiveData<List<StepEntity>> mStepsList) {
        this.mStepsList = mStepsList;
    }

    public LiveData<RecipeDetails> getmRecipeDetails() {
        return mRecipeDetails;
    }

    public void setmRecipeDetails(LiveData<RecipeDetails> mRecipeDetails) {
        this.mRecipeDetails = mRecipeDetails;
    }

}
