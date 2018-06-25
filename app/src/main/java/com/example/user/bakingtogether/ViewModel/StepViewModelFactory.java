package com.example.user.bakingtogether.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.user.bakingtogether.TheRepository;

/**
 * The DetailsActivityViewModel's factory
 */
public class StepViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private TheRepository repository;
    private Integer recipeId;
    private Integer stepId;

    /**
     * constructor that will initialise the instances used for creating the ViewModel required
     *
     * @param repository
     * @param recipeId
     * @param stepId
     */
    public StepViewModelFactory(TheRepository repository, Integer recipeId, Integer stepId) {
        this.repository = repository;
        this.recipeId = recipeId;
        this.stepId = stepId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new StepActivityViewModel(repository, recipeId, stepId);
    }
}