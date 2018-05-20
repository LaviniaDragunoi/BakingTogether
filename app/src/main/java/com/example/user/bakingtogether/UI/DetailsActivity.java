package com.example.user.bakingtogether.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.UI.ObjectList.MyListsFragment;
import com.example.user.bakingtogether.adapter.ListsAdapter;
import com.example.user.bakingtogether.data.RecipeResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DetailsActivity extends AppCompatActivity {
    private Intent intent;
    RecipeEntity currentRecipe;
    private ListsAdapter objectAdapter;
    @BindView(R.id.object_list_recycler_view)
    RecyclerView objectListRV;
    private AppRoomDatabase roomDB;
    private static final String TAG = DetailsActivity.class.getSimpleName();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        roomDB = AppRoomDatabase.getsInstance(this);
        intent = getIntent();
        currentRecipe = intent.getParcelableExtra("Recipe");
        int currentRecipeId = currentRecipe.getId();

        Intent stepIntent = new Intent(this, StepActivity.class);
        stepIntent.putExtra("RecipeId", currentRecipeId);

        //set recipe title
        setTitle(currentRecipe.getName());

        //Ingredients list fragment
    MyListsFragment ingredientsFragment =  new MyListsFragment();
        List<IngredientEntity> ingredientEntityList = roomDB.recipeDao().getIngredientsByRecipeId(currentRecipeId);
    ArrayList<IngredientEntity> ingredientEntities = new ArrayList<>(ingredientEntityList);
        Bundle ingredientBundle = new Bundle();
        ingredientBundle.putParcelableArrayList("IngredientsList", ingredientEntities);
        ingredientsFragment.setArguments(ingredientBundle);
      FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.ingredients_list,ingredientsFragment)
                .commit();

        //Steps list fragment
       /* MyListsFragment stepsFragment =  new MyListsFragment();
        List<StepEntity> stepEntityList = roomDB.recipeDao().getStepsByRecipeId(currentRecipeId);
        List<Object> objectListStep = stepsFragment.convertStepsListToOrbjectList(stepEntityList);
        stepsFragment.bindDataToAdapter(objectListStep);
        fragmentManager.beginTransaction()
                .replace(R.id.steps_list,ingredientsFragment)
                .commit();*/

    }


}
