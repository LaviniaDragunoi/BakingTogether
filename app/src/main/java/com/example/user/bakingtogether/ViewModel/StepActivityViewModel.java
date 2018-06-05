package com.example.user.bakingtogether.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.user.bakingtogether.DB.RecipeDetails;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.TheRepository;

import java.util.Collections;
import java.util.List;

public class StepActivityViewModel extends ViewModel {
    private Integer mRecipeId;
    public MutableLiveData<Integer> mStepId = new MutableLiveData<>();
    private LiveData<RecipeDetails> mRecipeDetails;
    private TheRepository mRepository;
    private LiveData<List<StepEntity>> mStepsList;
    private LiveData<StepEntity> mStep;

    public StepActivityViewModel(TheRepository repository, Integer recipeId, Integer stepId){
        mRepository = repository;
        mRecipeId = recipeId;
        mStepId.setValue(stepId);
        mRecipeDetails = mRepository.getRecipeById(recipeId);
        mStepsList = mRepository.getStepsByRecipeId(recipeId);
        mStep = mRepository.getStepByItsId(stepId);
    }

    public MutableLiveData<Integer> getStepId() {
        return mStepId;
    }

    public void setStepId(Integer stepId) {
        this.mStepId.postValue(stepId);
    }

    public LiveData<List<StepEntity>> getStepsList() {
        return mStepsList;
    }
    public List<StepEntity> getList(){ return mStepsList.getValue();}

    public void setStepsList(LiveData<List<StepEntity>> stepsList) {
        this.mStepsList = stepsList;
    }

    public LiveData<StepEntity> getStep() {
        return mStep;
    }
    public StepEntity getStepByItsOwnId(Integer stepId){
        return mRepository.getStepByItsId(stepId).getValue();
    }

    public void setStep(Integer stepId) {
        this.mStep = mRepository.getStepByItsId(stepId);
    }



//    public StepEntity setNextId(){
//        mStepId = mStepId++;
//        setStepId(mStepId);
//        return mRepository.getStepByItsId(mStepId).getValue();
//    }
//
//    public StepEntity setPreviousId(){
//
//        mStepId= mStepId--;
//        setStepId(mStepId);
//        return mRepository.getStepByItsId(mStepId).getValue();
//    }
}
