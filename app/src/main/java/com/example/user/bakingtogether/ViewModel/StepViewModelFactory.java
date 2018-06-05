package com.example.user.bakingtogether.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.user.bakingtogether.TheRepository;

public class StepViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private TheRepository repository;
    private Integer recipeId;
    private Integer stepId;

    public StepViewModelFactory(TheRepository repository, Integer recipeId, Integer stepId){
        this.repository = repository;
        this.recipeId = recipeId;
        this.stepId = stepId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new StepActivityViewModel(repository, recipeId, stepId);
    }
}