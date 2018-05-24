package com.example.user.bakingtogether.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.UI.ObjectList.MyListsFragment;
import com.example.user.bakingtogether.adapter.ListsAdapter;
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

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        roomDB = AppRoomDatabase.getsInstance(this);
        intent = getIntent();
        currentRecipe = intent.getParcelableExtra("Recipe");
        int currentRecipeId = currentRecipe.getId();

        //set recipe title
        setTitle(currentRecipe.getName());

        //Ingredients list fragment
    MyListsFragment ingredientsFragment =  new MyListsFragment();
        List<IngredientEntity> ingredientEntityList = roomDB.recipeDao().getIngredientsByRecipeId(currentRecipeId);
    ArrayList<IngredientEntity> ingredientEntities = new ArrayList<>(ingredientEntityList);
        Bundle ingredientBundle = new Bundle();
        ingredientBundle.putParcelableArrayList("IngredientsList", ingredientEntities);
        ingredientsFragment.setArguments(ingredientBundle);


        //Steps list fragment
      MyListsFragment stepsFragment =  new MyListsFragment();
        List<StepEntity> stepEntityList = roomDB.recipeDao().getStepsByRecipeId(currentRecipeId);
        ArrayList<StepEntity> stepEntities = new ArrayList<>(stepEntityList);
        Bundle stepBundle = new Bundle();
        stepBundle.putParcelableArrayList("StepsList", stepEntities);
       stepsFragment.setArguments(stepBundle);


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.ingredients_list,ingredientsFragment)
                .replace(R.id.steps_list, stepsFragment)
                .commit();

    }

private void clearIngredientsList(){

}
}
