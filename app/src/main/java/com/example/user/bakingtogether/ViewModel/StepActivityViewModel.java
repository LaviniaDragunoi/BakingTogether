package com.example.user.bakingtogether.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.Transformations;

import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.TheRepository;

import java.util.List;

public class StepActivityViewModel extends ViewModel {

    private final MutableLiveData<Integer> mRecipeId;
    private final MutableLiveData<Integer> mStepId;
    private final TheRepository mRepository;

    private final LiveData<List<StepEntity>> mStepsList;
    private final MediatorLiveData<StepEntity> mCurrentStep = new MediatorLiveData<>();

    public StepActivityViewModel(TheRepository repository){
        mRepository = repository;
        this.mRecipeId = new MutableLiveData<>();
        this.mStepId = new MutableLiveData<>();

        mStepsList =  Transformations.switchMap(mRecipeId, input ->
        {
            if(input != null) {
                return mRepository.getStepsByRecipeId(input);
            }
            return null;
        });

        mCurrentStep.addSource(mStepsList, input -> {
            if(input != null && !input.isEmpty() && mStepId.getValue() != null){
                mCurrentStep.setValue(input.get(mStepId.getValue()));
            }else mCurrentStep.setValue(input.get(0));
        });

        mCurrentStep.addSource(mStepId, input -> {
            if(input != null && mStepsList.getValue() != null && !mStepsList.getValue().isEmpty()){
                mCurrentStep.setValue(mStepsList.getValue().get(input));
            }else mCurrentStep.setValue(null);
        });
    }
    public boolean isFirst(){
//        boolean first = false;
//        List<StepEntity> stepsList = mStepsList.getValue();
//        if(mStepId ==  stepsList.get(0).getId()) {
//            first = true;
//        }
        return mStepsList.getValue().size() - 1 > mStepId.getValue();
    }

    public boolean isLast(){
//        boolean last = false;
//        List<StepEntity> stepsList = mStepsList.getValue();
//        if(mStepId ==  stepsList.get(stepsList.size()-1).getId()) {
//            last = true;
//        }
        return 0 < mStepId.getValue();
    }

    public void getNext(){
        mStepId.setValue(mStepId.getValue() + 1);
    }

    public void getPrevious(){
        mStepId.setValue(mStepId.getValue() - 1);
    }
    public LiveData<StepEntity> getStepDetails() {
        return mCurrentStep;
    }

    public void setmRecipeId(int recipeId){
        if(mRecipeId.getValue() == null || mRecipeId.getValue() != recipeId){
            mRecipeId.setValue(recipeId);
        }
    }

    public void setmStepId(int stepId){
        if(mStepId.getValue() == null || mStepId.getValue() != stepId){
            mStepId.setValue(stepId);
        }
    }

    public int getRecipeId(){
        return mRecipeId.getValue();
    }

    public int getStepId(){
        if(mStepId.getValue() == null) return -1;
        return mStepId.getValue();
    }

  /*  private MutableLiveData<Integer> mStepId;
    private TheRepository mRepository;
    private LiveData<StepEntity> mStep;
    private LiveData<List<StepEntity>> mStepsList;
    private MutableLiveData<Integer> mRecipeId;
    public StepActivityViewModel(TheRepository repository){
        mRepository = repository;
        mStep = mRepository.getStepByItsId(stepId);
        mStepsList = mRepository.getStepsByRecipeId(recipeId);

    }

  */
}
