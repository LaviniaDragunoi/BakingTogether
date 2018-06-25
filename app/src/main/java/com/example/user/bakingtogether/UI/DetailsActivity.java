package com.example.user.bakingtogether.UI;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.user.bakingtogether.AppExecutors;
import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.TheRepository;
import com.example.user.bakingtogether.UI.ObjectList.MyListsFragment;
import com.example.user.bakingtogether.ViewModel.DetailsActivityViewModel;
import com.example.user.bakingtogether.ViewModel.DetailsViewModelFactory;
import com.example.user.bakingtogether.data.ApiUtils;

import java.util.Objects;

import butterknife.BindView;

import static com.example.user.bakingtogether.adapter.RecipesAdapter.DEFAULT_VALUE;
import static com.example.user.bakingtogether.adapter.RecipesAdapter.EXTRA_RECIPE;

public class DetailsActivity extends AppCompatActivity {

    public static final String RECIPE_ID = "com.example.user.bakingtogether.widget.extra.RECIPE_ID";
    RecipeEntity currentRecipe;
    @BindView(R.id.object_list_recycler_view)
    RecyclerView objectListRV;
    private int currentRecipeId;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Initialise the RoomDb
        AppRoomDatabase roomDB = AppRoomDatabase.getsInstance(this);
        //Initialise the repository
        TheRepository repository = TheRepository.getsInstance(AppExecutors.getInstance(),
                roomDB, roomDB.recipeDao(), ApiUtils.getRecipeInterfaceResponse());

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_RECIPE)) {
                currentRecipe = intent.getParcelableExtra(EXTRA_RECIPE);
                currentRecipeId = currentRecipe.getId();
            } else if (intent.hasExtra(RECIPE_ID)) {
                currentRecipeId = intent.getIntExtra(RECIPE_ID, DEFAULT_VALUE);
                currentRecipe = repository.getRecipeEntityById(currentRecipeId);
            }
        }
        //set recipe title
        setTitle(currentRecipe.getName());
        //Ingredients list fragment
        MyListsFragment ingredientsFragment = new MyListsFragment();
        //view model
        DetailsViewModelFactory mDetailsViewModelFactory = new DetailsViewModelFactory(repository,
                currentRecipeId);
        DetailsActivityViewModel mViewModel = ViewModelProviders.of(this,
                mDetailsViewModelFactory).get(DetailsActivityViewModel.class);
        //Get the ingredientsList from ViewModel and if is not empty list will bind the list into adapter
        mViewModel.getmIngredientsList().observe(this, ingredientEntityList -> {
            if (ingredientEntityList != null && ingredientEntityList.size() != 0) {
                ingredientsFragment.bindDataToAdapter(ingredientsFragment
                        .convertIngredientListToObjectList(ingredientEntityList));
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.ingredients_list, ingredientsFragment)
                .commit();
        //Steps list fragment
        MyListsFragment stepsFragment = new MyListsFragment();
        mDetailsViewModelFactory = new DetailsViewModelFactory(repository, currentRecipeId);
        mViewModel = ViewModelProviders.of(this, mDetailsViewModelFactory).get(DetailsActivityViewModel.class);
        //Get the stepsList from ViewModel and if is not empty list will bind the list into adapter
        mViewModel.getmStepsList().observe(this, stepEntityList -> {
            if (stepEntityList != null && stepEntityList.size() != 0) {
                stepsFragment.bindDataToAdapter(stepsFragment.convertStepsListToObjectList(stepEntityList));
            }
        });
        fragmentManager.beginTransaction()
                .replace(R.id.steps_list, stepsFragment)
                .commit();
    }
}
