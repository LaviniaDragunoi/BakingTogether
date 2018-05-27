package com.example.user.bakingtogether.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.user.bakingtogether.TheRepository;

public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private TheRepository mRepository;
    private int mRecipeId;
    public DetailsViewModelFactory(TheRepository repository, int recipeId){
        this.mRepository = repository;
        this.mRecipeId = recipeId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new DetailsActivityViewModel(mRepository, mRecipeId);
    }
}
