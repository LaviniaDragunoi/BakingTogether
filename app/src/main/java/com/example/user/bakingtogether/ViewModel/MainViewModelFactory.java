package com.example.user.bakingtogether.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.user.bakingtogether.TheRepository;

/**
 * The MainActivityViewModel's factory
 */
public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private TheRepository mRepository;

    /**
     * constructor that will initialise the instances used for creating the ViewModel required
     *
     * @param repository
     */
    public MainViewModelFactory(TheRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MainActivityViewModel(mRepository);
    }
}
