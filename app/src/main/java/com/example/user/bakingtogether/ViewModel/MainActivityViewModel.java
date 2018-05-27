package com.example.user.bakingtogether.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.TheRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private TheRepository mRepository;
    private LiveData<List<RecipeEntity>> recipeEntity;

    public MainActivityViewModel(TheRepository repository){
        mRepository = repository;
        recipeEntity = repository.getInitialRecipeList();

    }

    public LiveData<List<RecipeEntity>> getRecipeEntity() {
        return recipeEntity;
    }
}
