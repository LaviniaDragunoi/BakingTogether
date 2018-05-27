package com.example.user.bakingtogether.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.user.bakingtogether.TheRepository;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private TheRepository mRepository;

    public MainViewModelFactory(TheRepository repository){
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new MainActivityViewModel(mRepository);
    }
}
