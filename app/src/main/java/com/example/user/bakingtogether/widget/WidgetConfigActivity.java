package com.example.user.bakingtogether.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.user.bakingtogether.AppExecutors;
import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.IngredientEntity;
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
import butterknife.ButterKnife;

import static com.example.user.bakingtogether.widget.BakingWidgetProvider.updateAppWidget;
import static java.lang.String.valueOf;

public class WidgetConfigActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    List<String> recipesNames;
    List<RecipeDetails> recipeDetails;
    TheRepository repository;
    AppRoomDatabase roomDB;
    @BindView(R.id.recipe_spinner)
    Spinner spinner;
    @BindView(R.id.add_widget_button)
    Button addWidgetButton;
    private String selectedRecipe;
    private int mAppWidgetId;
    private Context mContext;
 //   private List<IngredientEntity> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);
        ButterKnife.bind(this);


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
        MainActivityViewModel viewModel = ViewModelProviders.of(this, mMainViewModelFactory).get(MainActivityViewModel.class);

        viewModel.getWidgetRecipeList().observe(this,recipesList->{
            if(recipesList!=null && recipesList.size() != 0){
                recipesNames = new ArrayList<>();
                recipeDetails = new ArrayList<>();
                recipeDetails.addAll(recipesList);
                for(int i=0; i< recipesList.size(); i++){
                    recipesNames.add(recipesList.get(i).getRecipe().getName());

                }

                //Populate the spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(WidgetConfigActivity.this,
                        android.R.layout.simple_spinner_item, recipesNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(WidgetConfigActivity.this);
                addWidgetButton.setOnClickListener(WidgetConfigActivity.this);

            }

        });

    }

    @SuppressLint("NewApi")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedRecipe = recipesNames.get(spinner.getSelectedItemPosition());

    }

    private void addWidget(Context context, int recipeId) {
        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("RecipeIdSh", recipeId);
        editor.apply();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        updateAppWidget(context, appWidgetManager, mAppWidgetId, recipeId);

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
            addWidget(WidgetConfigActivity.this, spinner.getSelectedItemPosition());}
    }

    public static void deleteRecipeIdPref(Context context, int mAppWidgetId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.remove(valueOf(mAppWidgetId));
        e.apply();
    }
}

