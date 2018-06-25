package com.example.user.bakingtogether.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Parcelable;

import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.TheRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the StepActivity
 */
public class StepActivityViewModel extends ViewModel {

    public MutableLiveData<Integer> mStepId = new MutableLiveData<>();
    private TheRepository mRepository;
    private LiveData<List<StepEntity>> mStepsList;
    private LiveData<StepEntity> mStep;

    /**
     * StepActivityViewModel's constructor that will initialise the instances
     *
     * @param repository
     * @param recipeId
     * @param stepId
     */
    public StepActivityViewModel(TheRepository repository, Integer recipeId, Integer stepId) {
        mRepository = repository;
        mStepId.setValue(stepId);
        mStepsList = mRepository.getStepsByRecipeId(recipeId);
        mStep = mRepository.getStepByItsId(stepId);
    }

    /**
     * gets the MutableLiveData of the stepId
     *
     * @return
     */
    public MutableLiveData<Integer> getStepId() {
        return mStepId;
    }

    /**
     * sets the newId of the step on the MutablelLiveData variable mStepId
     *
     * @param newStepId
     */
    public void setStepId(int newStepId) {
        mStepId.setValue(newStepId);
    }

    public int getStepIdInt() {
        return mStepId.getValue();
    }

    /**
     * gets the list of steps that is an Parcelable arrayList
     *
     * @return
     */
    public ArrayList<? extends Parcelable> getList() {
        ArrayList<StepEntity> steps = new ArrayList<>();
        List<StepEntity> list = mStepsList.getValue();
        steps.addAll(list);
        return steps;
    }

    /**
     * gets the LiveData stepEntity
     *
     * @return
     */
    public LiveData<StepEntity> getStep() {
        return mStep;
    }

    /**
     * sets the newStepId for the step instance
     *
     * @param newStepId
     */
    public void setStep(int newStepId) {
        mStep = mRepository.getStepByItsId(newStepId);
    }
}
