package com.example.user.bakingtogether.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.UI.ObjectList.StepFragment;

import java.util.Objects;

public class StepActivity extends AppCompatActivity {

    private AppRoomDatabase roomDB;
    StepEntity currentStep;



    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
       currentStep = intent.getParcelableExtra("CurrentStep");

       int currentStepId = currentStep.getId();

        StepFragment stepFragment = new StepFragment();

       Bundle stepBundle = new Bundle();
       stepBundle.putParcelable("CurrentStep", currentStep);
        stepFragment.setArguments(stepBundle);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.popBackStack();
        fragmentManager.beginTransaction()
                .replace(R.id.step_container,stepFragment)
                .commit();




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
