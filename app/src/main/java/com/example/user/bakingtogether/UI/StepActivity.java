package com.example.user.bakingtogether.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.UI.ObjectList.StepFragment;

import java.util.ArrayList;
import java.util.Objects;


public class StepActivity extends AppCompatActivity {
    private static final int DEFAULT_VALUE = -1;
    private ArrayList<StepEntity> stepsList;
    private Integer stepId;
    private int recipeId;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_step);
        Intent intent = getIntent();
        if(intent != null){
            stepsList = intent.getParcelableArrayListExtra("StepsList");
            recipeId = intent.getIntExtra("CurrentRecipe", DEFAULT_VALUE);
            stepId = intent.getIntExtra("CurrentStep",DEFAULT_VALUE);
        }
        StepFragment stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        if(recipeId != DEFAULT_VALUE && stepId != null && stepsList != null) {
            bundle.putParcelableArrayList("List", stepsList);
            bundle.putInt("StepId", stepId);
            bundle.putInt("RecipeId", recipeId);
            stepFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.step_container, stepFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);

    }

}

