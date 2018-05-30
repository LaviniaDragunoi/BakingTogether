package com.example.user.bakingtogether.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.user.bakingtogether.TheRepository;

public class StepViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private TheRepository mRepository;

    public StepViewModelFactory(TheRepository repository){
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new StepActivityViewModel(mRepository);
    }
}
