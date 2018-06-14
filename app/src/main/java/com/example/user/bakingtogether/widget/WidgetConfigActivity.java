package com.example.user.bakingtogether.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.user.bakingtogether.AppExecutors;
import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.RecipeDetails;
import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.TheRepository;
import com.example.user.bakingtogether.ViewModel.MainActivityViewModel;
import com.example.user.bakingtogether.ViewModel.MainViewModelFactory;
import com.example.user.bakingtogether.data.ApiUtils;
import com.example.user.bakingtogether.data.RecipeApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WidgetConfigActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    List<String> recipes = new ArrayList<>();
    TheRepository repository;
    MainActivityViewModel viewModel;
    AppRoomDatabase roomDB;
    @BindView(R.id.recipe_spinner)
    Spinner spinner;
    @BindView(R.id.add_widget_button)
    Button addWidgetButton;
    private String selectedRecipe;
    private int mAppWidgetId;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);

        spinner.setOnItemSelectedListener(this);

        //Find the widget Id from the Intent
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        //If the appWidget Id is invalid finish
        if(mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID){
            finish();
            return;
        }

        //Getting the list of recipes
        roomDB = AppRoomDatabase.getsInstance(this);
        AppExecutors executors = AppExecutors.getInstance();
        RecipeApiInterface recipeApiInterface = ApiUtils.getRecipeInterfaceResponse();
        repository = TheRepository.getsInstance(executors,
                roomDB,roomDB.recipeDao(),recipeApiInterface);
        MainViewModelFactory mMainViewModelFactory = new MainViewModelFactory(repository);
        viewModel = ViewModelProviders.of(this, mMainViewModelFactory).get(MainActivityViewModel.class);
        viewModel.getRecipeEntity().observe(this,recipeEntities ->{
            if(recipeEntities != null) {
                for(int i=0; i< recipeEntities.size(); i++){
                recipes.add(recipeEntities.get(i).getName());

                }
                addWidgetButton.setOnClickListener(this);
            }
        });

        //Populate the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, recipes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(adapter);
    }

    @SuppressLint("NewApi")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedRecipe = recipes.get(spinner.getSelectedItemPosition());
        Intent selectedRecipeIntent = new Intent(this, RecipeIntentService.class);
        selectedRecipeIntent.putExtra("SelectedRecipeName", selectedRecipe);
        this.startForegroundService(selectedRecipeIntent);
    }

    private void addWidget(WidgetConfigActivity widgetConfigActivity, String recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
        BakingAppWidgetProvider.updateAppWidget(mContext, appWidgetManager, mAppWidgetId, recipe);

        // Pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.add_widget_button){
            addWidget(this, selectedRecipe);}
    }
}
