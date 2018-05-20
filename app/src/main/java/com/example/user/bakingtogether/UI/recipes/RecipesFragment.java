package com.example.user.bakingtogether.UI.recipes;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.data.ApiUtils;
import com.example.user.bakingtogether.data.Ingredients;
import com.example.user.bakingtogether.data.RecipeApiInterface;
import com.example.user.bakingtogether.data.RecipeResponse;
import com.example.user.bakingtogether.data.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecipesFragment extends Fragment {

    @BindView(R.id.recipes_recycler_view) RecyclerView recipesRW;
    @BindView(R.id.recipes_progress_bar) ProgressBar recipesPB;
    @BindView(R.id.error_loading_recipes)
    TextView errorMessage;
    private RecipesAdapter recipesAdapter;
    private Retrofit retrofit;
    private Context mContext;
    private AppRoomDatabase roomDB;

    public RecipesFragment(){

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, final Bundle saveInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_recipes_main, container, false);
        ButterKnife.bind(this, rootView);
        roomDB = AppRoomDatabase.getsInstance(getContext());
        recipesPB.setVisibility(View.VISIBLE);
        RecipeApiInterface apiService = ApiUtils.getRecipeInterfaceResponse();
        Call<List<RecipeResponse>> call = apiService.getRecipeResponse();
        call.enqueue(new Callback<List<RecipeResponse>>() {
            @Override
            public void onResponse(Call<List<RecipeResponse>> call, Response<List<RecipeResponse>> response) {
                List<RecipeResponse> recipes = response.body();

                //writing in RoomDB

                RecipeEntity  recipeEntity = null;
                List<RecipeEntity> recipesEntityList = new ArrayList<>();

                for(int i=0; i< recipes.size(); i++) {
                    List<Ingredients> ingredientsList = new ArrayList<>();
                    List<IngredientEntity> ingredientsListEntity = new ArrayList<>();
                    List<StepEntity> stepsListEntity = new ArrayList<>();
                    List<Step> steps = new ArrayList<>();

                            recipeEntity = new RecipeEntity(recipes.get(i).getId(), recipes.get(i).getName(),
                            recipes.get(i).getServings(), recipes.get(i).getImage());
                    roomDB.recipeDao().insertRecipe(recipeEntity);
                    int recipeId = recipeEntity.getId();
                    recipesEntityList.add(recipeEntity);
                    ingredientsList = recipes.get(i).getIngredients();
                    for(int j = 0; j< ingredientsList.size(); j++){
                        IngredientEntity ingredient = new IngredientEntity(recipeId,(double)(ingredientsList.get(j).getQuantity()),
                                ingredientsList.get(j).getMeasure(), ingredientsList.get(j).getIngredient());
                        ingredientsListEntity.add(ingredient);
                        roomDB.recipeDao().insertIngredients(ingredient);
                    }

                    steps = recipes.get(i).getSteps();
                    for(int j = 0; j< steps.size(); j++){
                        StepEntity stepEntity = new StepEntity(recipeId,
                                steps.get(j).getShortDescription(),steps.get(j).getDescription(),
                                steps.get(j).getVideoURL(),steps.get(j).getThumbnailURL());
                        stepsListEntity.add(stepEntity);
                        roomDB.recipeDao().insertSteps(stepEntity);
                    }

                }


                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(recipesRW.getContext(),1);
                recipesPB.setVisibility(View.INVISIBLE);
                recipesRW.setLayoutManager(layoutManager);
                recipesAdapter = new RecipesAdapter(mContext,recipesEntityList);
                recipesRW.setAdapter(recipesAdapter);
            }

            @Override
            public void onFailure(Call<List<RecipeResponse>> call, Throwable t) {
                recipesPB.setVisibility(View.INVISIBLE);
                errorMessage.setVisibility(View.VISIBLE);

            }
        });
        return rootView;
    }
}
