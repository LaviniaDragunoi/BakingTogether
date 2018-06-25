package com.example.user.bakingtogether.UI;

import android.annotation.SuppressLint;
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

import static com.example.user.bakingtogether.adapter.ListsAdapter.CURRENT_RECIPE;
import static com.example.user.bakingtogether.adapter.ListsAdapter.CURRENT_STEP;
import static com.example.user.bakingtogether.adapter.ListsAdapter.STEPS_LIST;


public class StepActivity extends AppCompatActivity {
    private static final int DEFAULT_VALUE = -1;
    private ArrayList<StepEntity> stepsList;
    private Integer stepId;
    private int recipeId;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if the device is in landscape mode video will play on fullscreen with hide ACtionBar
        if (getResources().getBoolean(R.bool.isLandscape) && !getResources().getBoolean(R.bool.isTablet)) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_step);
        if (getResources().getBoolean(R.bool.isLandscape) && !getResources().getBoolean(R.bool.isTablet)) {
            getSupportActionBar().hide();
        }
        if (savedInstanceState == null) {
            StepFragment stepFragment = new StepFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(STEPS_LIST, getIntent().getParcelableArrayListExtra(STEPS_LIST));
            bundle.putInt(CURRENT_STEP, getIntent().getIntExtra(CURRENT_STEP, DEFAULT_VALUE));
            bundle.putInt(CURRENT_RECIPE, getIntent().getIntExtra(CURRENT_RECIPE, DEFAULT_VALUE));
            stepFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_container, stepFragment)
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

    //Set title on ACtionBar that is received from fragment
    public void setActionBarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }
}

