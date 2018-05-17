package com.example.user.bakingtogether.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.UI.ObjectList.ListFragment;
import com.example.user.bakingtogether.adapter.ListsAdapter;
import com.example.user.bakingtogether.data.RecipeResponse;

import java.util.List;

import butterknife.BindView;

public class DetailsActivity extends AppCompatActivity {
    private Intent intent;
    RecipeResponse currentRecipe;
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

        //set recipe title
        setTitle(currentRecipe.getName());

    ListFragment ingredientsFragment =  new ListFragment();
    List<IngredientEntity> ingredientEntityList = roomDB.recipeDao().getIngredientsByRecipeId(currentRecipeId);
    List<Object> objectList = ingredientsFragment.convertIngredientListToOrbjectList(ingredientEntityList);
    ingredientsFragment.bindDataToAdapter(this, objectList);
      FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.ingredients_list,ingredientsFragment)
                .commit();
    }

}
