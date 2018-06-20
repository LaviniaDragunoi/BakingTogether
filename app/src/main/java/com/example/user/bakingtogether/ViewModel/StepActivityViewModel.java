package com.example.user.bakingtogether.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Parcelable;

import com.example.user.bakingtogether.DB.RecipeDetails;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.TheRepository;

import java.util.ArrayList;
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

    public int getStepIdInt(){
        return mStepId.getValue();
    }


    public ArrayList<? extends Parcelable> getList(){
        ArrayList<StepEntity> steps = new ArrayList<>();
        List<StepEntity> list = mStepsList.getValue();

            steps.addAll(list);
                return steps;}


    public LiveData<StepEntity> getStep() {
        return mStep;
    }

    public void setStep(int newStepId){
       mStep = mRepository.getStepByItsId(newStepId);
    }
    public void setStepId(int newStepId){
        mStepId.setValue(newStepId);
    }
    public StepEntity getCurrentStep(){
        return mStep.getValue();
    }

}
