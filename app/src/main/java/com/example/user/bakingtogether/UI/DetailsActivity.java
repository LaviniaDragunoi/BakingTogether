package com.example.user.bakingtogether.UI;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.example.user.bakingtogether.AppExecutors;
import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.TheRepository;
import com.example.user.bakingtogether.UI.ObjectList.MyListsFragment;
import com.example.user.bakingtogether.ViewModel.DetailsActivityViewModel;
import com.example.user.bakingtogether.ViewModel.DetailsViewModelFactory;
import com.example.user.bakingtogether.adapter.ListsAdapter;
import com.example.user.bakingtogether.data.ApiUtils;
import com.example.user.bakingtogether.data.RecipeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class DetailsActivity extends AppCompatActivity {
    private Intent intent;
    RecipeEntity currentRecipe;
    private ListsAdapter objectAdapter;
    @BindView(R.id.object_list_recycler_view)
    RecyclerView objectListRV;
    @BindView(R.id.clear_ingredients)
    Button clearIngredients;
    @BindView(R.id.clear_steps)
    Button clearSteps;
    private AppRoomDatabase roomDB;
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private ArrayList<IngredientEntity> ingredientEntities;
    private ArrayList<StepEntity> stepEntities;
    private TheRepository repository;
    private DetailsViewModelFactory mDetailsViewModelFactory;
    private DetailsActivityViewModel mViewModel;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);




        roomDB = AppRoomDatabase.getsInstance(this);
        intent = getIntent();
        currentRecipe = intent.getParcelableExtra("Recipe");
        final int currentRecipeId = currentRecipe.getId();
        //set recipe title
        setTitle(currentRecipe.getName());

        //Ingredients list fragment

        MyListsFragment ingredientsFragment =  new MyListsFragment();
        //view model
        repository = TheRepository.getsInstance(AppExecutors.getInstance(),
                roomDB, roomDB.recipeDao(), ApiUtils.getRecipeInterfaceResponse());
        mDetailsViewModelFactory = new DetailsViewModelFactory(repository, currentRecipeId);
        mViewModel = ViewModelProviders.of(this, mDetailsViewModelFactory).get(DetailsActivityViewModel.class);

        mViewModel.getmIngredientsList().observe(this, ingredientEntityList -> {
            if (ingredientEntityList != null && ingredientEntityList.size() != 0) {
                ingredientsFragment.bindDataToAdapter(ingredientsFragment.convertIngredientListToObjectList(ingredientEntityList));
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.ingredients_list,ingredientsFragment)
                .commit();


        //Steps list fragment
        MyListsFragment stepsFragment =  new MyListsFragment();
        mDetailsViewModelFactory = new DetailsViewModelFactory(repository, currentRecipeId);
        mViewModel = ViewModelProviders.of(this, mDetailsViewModelFactory).get(DetailsActivityViewModel.class);
        mViewModel.getmStepsList().observe(this, stepEntityList -> {
            if (stepEntityList != null && stepEntityList.size() != 0) {

               stepsFragment.bindDataToAdapter(stepsFragment.convertStepsListToObjectList(stepEntityList));
            }
        });

        fragmentManager.beginTransaction()
                .replace(R.id.steps_list, stepsFragment)
                .commit();


    }


private void clearIngredientsList(){

}

}
