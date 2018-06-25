package com.example.user.bakingtogether.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.user.bakingtogether.DB.RecipeDetails;
import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.TheRepository;

import java.util.List;

/**
 * ViewModel for the DetailsActivity
 */
public class MainActivityViewModel extends ViewModel {
    private TheRepository mRepository;
    private LiveData<List<RecipeEntity>> recipeEntity;

    /**
     * MainACtivityViewModel's constructor that will initialise the instances
     *
     * @param repository
     */
    public MainActivityViewModel(TheRepository repository) {
        mRepository = repository;
        recipeEntity = repository.getInitialRecipeList();
    }

    /**
     * Method used for getting and observe the recipeEntity list
     *
     * @return a LiveDta list of recipeEntity
     */
    public LiveData<List<RecipeEntity>> getRecipeEntity() {
        return recipeEntity;
    }

    /**
     * Method used for getting and observe the RecipeDetails list for the widget
     *
     * @return a LiveDta list of recipeDetails
     */
    public LiveData<List<RecipeDetails>> getWidgetRecipeList() {
        return mRepository.getRecipesWidget();
    }

    public void setRecipeEntity(RecipeEntity recipeEntity){
       mRepository.setRecipe(recipeEntity);
    }
}
