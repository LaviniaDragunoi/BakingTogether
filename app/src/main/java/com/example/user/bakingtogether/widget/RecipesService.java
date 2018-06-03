package com.example.user.bakingtogether.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.user.bakingtogether.AppExecutors;
import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.TheRepository;
import com.example.user.bakingtogether.data.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecipesService extends IntentService {
    public static final String ACTION_SHOW_INGREDIENTS =
            "com.example.user.bakingtogether.action.show_ingredients";

    public static final String ACTION_CHOOSE_RECIPE = "com.example.user.bakingtogether.action.choose_recipe";
    @BindView(R.id.recipes_spinner)
    Spinner recipeSpinner;
    private int recipeId;
    private AppRoomDatabase  roomDB = AppRoomDatabase.getsInstance(this);
    private TheRepository repository = TheRepository.getsInstance(AppExecutors.getInstance(),
            roomDB, roomDB.recipeDao(), ApiUtils.getRecipeInterfaceResponse());

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public RecipesService() {
        super("RecipeService");
    }

    public static void startActionChooseRecipe(Context context){
        Intent intent = new Intent(context, RecipesService.class);
        intent.setAction(ACTION_CHOOSE_RECIPE);
        context.startService(intent);
    }
    public static void startActionShowIngredients(Context context){
        Intent intent = new Intent(context, RecipesService.class);
        intent.setAction(ACTION_SHOW_INGREDIENTS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
if(intent != null){
    final String action = intent.getAction();
    if(ACTION_SHOW_INGREDIENTS.equals(action)){
        handleActionShowIngredients();
    }else if(ACTION_CHOOSE_RECIPE.equals(action)){
       handleActionChooseRecipe();
    }
}
    }

    private void handleActionChooseRecipe() {

        List<RecipeEntity> recipes = repository.getRecipes().getValue();
        ArrayAdapter<RecipeEntity> recipeAdapter = new ArrayAdapter<RecipeEntity>(this,
                android.R.layout.simple_spinner_item, recipes);
        recipeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipeSpinner.setAdapter(recipeAdapter);
        recipeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int recipeSelected = recipeSpinner.getSelectedItemPosition();
                recipeId = recipes.get(recipeSelected).getId();
            }
        });


    }

    private void handleActionShowIngredients() {
        if(recipeId > 0){
            List<IngredientEntity> ingredientsList = repository.getIngredientsByRecipeId(recipeId).getValue();

        }
    }
}
