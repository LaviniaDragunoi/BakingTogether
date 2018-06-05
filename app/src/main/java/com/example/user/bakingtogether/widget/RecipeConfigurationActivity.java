package com.example.user.bakingtogether.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.user.bakingtogether.AppExecutors;
import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.RecipeDetails;
import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.TheRepository;
import com.example.user.bakingtogether.UI.recipes.RecipesAdapter;
import com.example.user.bakingtogether.data.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecipeConfigurationActivity extends AppCompatActivity {
    @BindView(R.id.recipe_spinner)
    Spinner recipeSpinner;
    ArrayAdapter recipesAdapter;
    private int mAppWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_configuration_activity);
        AppRoomDatabase roomDB = AppRoomDatabase.getsInstance(this);
        TheRepository repository = TheRepository.getsInstance(AppExecutors.getInstance(),
                roomDB, roomDB.recipeDao(), ApiUtils.getRecipeInterfaceResponse());
       List<RecipeEntity> recipes = new ArrayList<>();
       recipes = repository.getRecipes().getValue();

       recipesAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, recipes);
        recipesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipeSpinner.setAdapter(recipesAdapter);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        recipeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }
    public void onBackButtonClick(View view) {
        finish();
    }
}
