package com.example.user.bakingtogether.UI;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class StepActivity extends AppCompatActivity {

    private AppRoomDatabase roomDB;
    StepEntity currentStep;

    @BindView(R.id.previous_fab)
    FloatingActionButton previousFAB;
    @BindView(R.id.next_fab)
    FloatingActionButton nextFAB;

    private List<StepEntity> stepsList;
    private int stepId;
    public SimpleExoPlayer player;
    public boolean playWhenReady;
    public long playBackPosition;
    public int currentWindow;
    private TheRepository repository;
    private StepViewModelFactory mStepViewModelFactory;
    private StepActivityViewModel mViewModel;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        StepFragment stepFragment = new StepFragment();
        roomDB = AppRoomDatabase.getsInstance(this);
        Intent intent = getIntent();
        int stepId = intent.getIntExtra("CurrentStep", -1);
        repository = TheRepository.getsInstance(AppExecutors.getInstance(),
                roomDB, roomDB.recipeDao(), ApiUtils.getRecipeInterfaceResponse());

        mStepViewModelFactory = new StepViewModelFactory(repository);
        mViewModel = ViewModelProviders.of(this, mStepViewModelFactory).get(StepActivityViewModel.class);
       if(stepId != -1){
           mViewModel.setmStepId(stepId);
        }


        mViewModel.getStepDetails().observe(this, stepEntity -> {
            if( stepEntity != null ){
                stepFragment.populateUI(stepEntity);
                if (mViewModel.isFirst()) {
                    previousFAB.setVisibility(View.GONE);
                    nextFAB.setVisibility(View.VISIBLE);
                } else {
                    if (mViewModel.isLast()) {
                        previousFAB.setVisibility(View.VISIBLE);
                        nextFAB.setVisibility(View.GONE);
                        } else {
                        previousFAB.setVisibility(View.VISIBLE);
                        nextFAB.setVisibility(View.VISIBLE);
                    }
                }
                nextFAB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewModel.getNext();
                        stepFragment.populateUI(stepEntity);
                    }
                });
                previousFAB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewModel.getPrevious();
                        stepFragment.populateUI(stepEntity);
                    }
                });

            }
        });
        Bundle stepBundle = new Bundle();
        stepBundle.putParcelable("CurrentStep", currentStep);
        stepFragment.setArguments(stepBundle);

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
