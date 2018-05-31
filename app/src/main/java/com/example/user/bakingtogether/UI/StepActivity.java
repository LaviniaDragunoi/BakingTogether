package com.example.user.bakingtogether.UI;

import android.annotation.SuppressLint;
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

public class StepActivity extends AppCompatActivity {

    private static final int DEFAULT_VALUE = -1;
    @BindView(R.id.previous_fab)
    FloatingActionButton previousFAB;
    @BindView(R.id.next_fab)
    FloatingActionButton nextFAB;
    private int stepId;
    private int recipeId;
    private TheRepository repository;
    List<StepEntity> stepsList;
    private AppRoomDatabase roomDB;
    private final static  String TAG = StepActivity.class.getSimpleName();
    private StepActivityViewModel mViewModel;
    private StepViewModelFactory mStepFactory;
    private StepEntity currentStep;
    private StepFragment stepFragment;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        roomDB = AppRoomDatabase.getsInstance(this);
        repository = TheRepository.getsInstance(AppExecutors.getInstance(),
                roomDB, roomDB.recipeDao(), ApiUtils.getRecipeInterfaceResponse());

        Intent intent = getIntent();
        stepId = intent.getIntExtra("CurrentStep", DEFAULT_VALUE);
        recipeId = intent.getIntExtra("CurrentRecipe", DEFAULT_VALUE);
        stepsList = intent.getParcelableArrayListExtra("StepsList");
        if ( stepId != DEFAULT_VALUE && recipeId != DEFAULT_VALUE){
            mStepFactory = new StepViewModelFactory(repository,recipeId, stepId);
        }

         stepFragment = new StepFragment();

        mViewModel = ViewModelProviders.of(this, mStepFactory).get(StepActivityViewModel.class);
        mViewModel.getStep().observe(this, stepEntity -> {
            if(stepEntity != null){
                currentStep = new StepEntity(stepEntity.getId(),stepEntity.getRecipeId(),
                        stepEntity.getShortDescription(), stepEntity.getDescription(),
                        stepEntity.getVideoURL(), stepEntity.getThumbnailURL());

                Bundle bundle = new Bundle();
                bundle.putParcelable("CurrentStepDetails", currentStep);
                ArrayList<StepEntity> steps = new ArrayList<>(stepsList);
                bundle.putParcelableArrayList("StepListCurrent", steps);
                stepFragment.setArguments(bundle);
                stepFragment.populateUI(stepEntity);
//               if (stepId != stepsList.get(0).getId() &&
//                       stepId != stepsList.get(stepsList.size()-1).getId()) {
//                    previousFAB.setVisibility(View.VISIBLE);
//                    nextFAB.setVisibility(View.VISIBLE);
//                } else {
//                    if (stepId == stepsList.get(0).getId() &&
//                            stepId != stepsList.get(stepsList.size()-1).getId()) {
//                        previousFAB.setVisibility(View.GONE);
//                        nextFAB.setVisibility(View.VISIBLE);
//                    } else if(stepEntity.getId() != stepsList.get(0).getId() &&
//                            stepEntity.getId() == stepsList.get(stepsList.size()-1).getId()) {
//                        previousFAB.setVisibility(View.VISIBLE);
//                        nextFAB.setVisibility(View.GONE);
//                    }
//                }



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
        getSupportActionBar().setTitle(title);
    }


}
