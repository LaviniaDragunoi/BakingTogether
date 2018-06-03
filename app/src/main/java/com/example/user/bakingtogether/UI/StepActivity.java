package com.example.user.bakingtogether.UI;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.user.bakingtogether.AppExecutors;
import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.TheRepository;
import com.example.user.bakingtogether.UI.ObjectList.StepFragment;
import com.example.user.bakingtogether.ViewModel.StepActivityViewModel;
import com.example.user.bakingtogether.ViewModel.StepViewModelFactory;
import com.example.user.bakingtogether.data.ApiUtils;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity implements StepFragment.OnButtonClickListener{

    private static final int DEFAULT_VALUE = -1;
    @BindView(R.id.previous_fab)
    FloatingActionButton previousFAB;
    @BindView(R.id.next_fab)
    FloatingActionButton nextFAB;
    List<StepEntity> stepsList;
    private final static  String TAG = StepActivity.class.getSimpleName();
    private StepActivityViewModel mViewModel;
    private StepViewModelFactory mStepFactory;
    private StepEntity currentStep;
    private StepFragment stepFragment;
    private int lastStepId, firstStepId;
    private int stepId;
    private TheRepository repository;
    private int recipeId;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        AppRoomDatabase roomDB = AppRoomDatabase.getsInstance(this);
        repository = TheRepository.getsInstance(AppExecutors.getInstance(),
                roomDB, roomDB.recipeDao(), ApiUtils.getRecipeInterfaceResponse());

        Intent intent = getIntent();
        stepId = intent.getIntExtra("CurrentStep", DEFAULT_VALUE);
        recipeId = intent.getIntExtra("CurrentRecipe", DEFAULT_VALUE);
        stepsList = intent.getParcelableArrayListExtra("StepsList");
        if ( stepId != DEFAULT_VALUE && recipeId != DEFAULT_VALUE){
            mStepFactory = new StepViewModelFactory(repository, recipeId, stepId);
        }

         stepFragment = new StepFragment();
        firstStepId = stepsList.get(0).getId();
        lastStepId = stepsList.get(stepsList.size()-1).getId();
        mViewModel = ViewModelProviders.of(this, mStepFactory).get(StepActivityViewModel.class);
        mViewModel.getStep().observe(this, stepEntity -> {
            if(stepEntity != null){
                currentStep = new StepEntity(stepEntity.getId(),stepEntity.getRecipeId(),
                        stepEntity.getShortDescription(), stepEntity.getDescription(),
                        stepEntity.getVideoURL(), stepEntity.getThumbnailURL());

                Bundle bundle = new Bundle();
                bundle.putParcelable("CurrentStepDetails", currentStep);
                bundle.putInt("First", firstStepId);
                bundle.putInt("Last", lastStepId);

                stepFragment.setArguments(bundle);
                stepFragment.populateUI(stepEntity, firstStepId, lastStepId);



            }
        });



        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_container,stepFragment)
                .commit();
        fragmentManager.popBackStack();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }


    @Override
    public void onButtonSelected(int stepId) {
        mStepFactory = new StepViewModelFactory(repository, recipeId, stepId);
        mViewModel = ViewModelProviders.of(this, mStepFactory).get(StepActivityViewModel.class);
        mViewModel.getStep().observe(this, stepEntity ->{
            if(stepEntity != null) {
                StepEntity step = new StepEntity(stepEntity.getId(), stepEntity.getRecipeId(),
                        stepEntity.getShortDescription(), stepEntity.getDescription(),
                        stepEntity.getVideoURL(), stepEntity.getThumbnailURL());
               Bundle bundle = new Bundle();
                bundle.putParcelable("CurrentStepDetails", step);
                bundle.putInt("First", firstStepId);
                bundle.putInt("Last", lastStepId);
                nextFAB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stepFragment.setArguments(bundle);
                    stepFragment.populateUI(step, firstStepId, lastStepId);
                }});
                previousFAB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        stepFragment.setArguments(bundle);
                        stepFragment.populateUI(step, firstStepId, lastStepId);
                    }
                });

            }
        });
    }
}
