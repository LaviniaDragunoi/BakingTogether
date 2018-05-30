package com.example.user.bakingtogether;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.DB.RecipeDao;
import com.example.user.bakingtogether.DB.RecipeDetails;
import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.data.Ingredients;
import com.example.user.bakingtogether.data.RecipeApiInterface;
import com.example.user.bakingtogether.data.RecipeResponse;
import com.example.user.bakingtogether.data.Step;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TheRepository {
    private static final Object LOCK = new Object();
    private static TheRepository sInstance;
    private final AppRoomDatabase mRoomDB;
    private final RecipeDao mRecipeDao;
    private final RecipeApiInterface mRecipeApiInterface;
    private final AppExecutors mAppExecutors;

    public TheRepository(AppExecutors appExecutors,AppRoomDatabase roomDB, RecipeDao recipeDao, RecipeApiInterface recipeApiInterface){
        mAppExecutors = appExecutors;
        mRoomDB = roomDB;
        mRecipeDao = recipeDao;
        mRecipeApiInterface = recipeApiInterface;
    }

    public synchronized static TheRepository getsInstance(AppExecutors appExecutors, AppRoomDatabase roomDB,
                                                          RecipeDao recipeDao, RecipeApiInterface recipeApiInterface ){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new TheRepository(appExecutors,roomDB,recipeDao,recipeApiInterface);
            }
        }
        return sInstance;
    }

    public LiveData<List<RecipeEntity>> getInitialRecipeList(){
        MediatorLiveData<List<RecipeEntity>> mainRecipeList = new MediatorLiveData<>();
        LiveData<List<RecipeEntity>> recipesDB = mRecipeDao.loadAllRecipes();
        mainRecipeList.addSource(recipesDB, newRecipes ->{
            if(mainRecipeList.getValue() != newRecipes){
                mainRecipeList.setValue(newRecipes);
            }
        });

        LiveData<List<RecipeResponse>> retrofitRecipesResponse = getResponseRecipesRetrofit();
        retrofitRecipesResponse.observeForever(recipeResponses -> {
            if(recipeResponses != null && recipeResponses.size() > 0){
                mAppExecutors.networkIO().execute(()-> {
                    addRecipesEntityFromResponse(recipeResponses);

                });
            }else {
                mainRecipeList.setValue(null);
            }
        });
        return mainRecipeList;
    }

    private LiveData<List<RecipeResponse>> getResponseRecipesRetrofit() {
        MutableLiveData<List<RecipeResponse>> recipeResponse = new MutableLiveData<>();
        mRecipeApiInterface.getRecipeResponse().enqueue(new Callback<List<RecipeResponse>>() {
            @Override
            public void onResponse(Call<List<RecipeResponse>> call, Response<List<RecipeResponse>> response) {
                if(response.isSuccessful()){
                    recipeResponse.setValue(response.body());
                }else {
                    recipeResponse.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<List<RecipeResponse>> call, Throwable t) {
                recipeResponse.setValue(null);
            }
        });
        return recipeResponse;
    }

    public LiveData<RecipeDetails> getRecipeById(int recipeId) {
        return mRecipeDao.getRecipeById(recipeId);
    }
    public LiveData<List<RecipeEntity>> getRecipes(){
        return mRecipeDao.loadAllRecipes();
    }

    public LiveData<List<StepEntity>> getStepsByRecipeId(int recipeId){
        return mRecipeDao.getStepsByRecipeId(recipeId);
    }

    public LiveData<List<IngredientEntity>> getIngredientsByRecipeId(int recipeId){
        return mRecipeDao.getIngredientsByRecipeId(recipeId);
    }

    public LiveData<StepEntity> getStepByItsId(int stepId){
        return mRecipeDao.getStepByStepId(stepId);
    }
    private void addRecipesEntityFromResponse(List<RecipeResponse> recipeResponses){

            RecipeEntity  recipeEntity = null;
            List<RecipeEntity> recipesEntityList = new ArrayList<>();
        for(int i=0; i< recipeResponses.size(); i++) {
            List<Ingredients> ingredientsList;
            List<IngredientEntity> ingredientsListEntity = new ArrayList<>();
            List<StepEntity> stepsListEntity = new ArrayList<>();
            List<Step> steps;

            recipeEntity = new RecipeEntity(recipeResponses.get(i).getId(), recipeResponses.get(i).getName(),
                    recipeResponses.get(i).getServings(), recipeResponses.get(i).getImage());
            mRoomDB.recipeDao().insertRecipe(recipeEntity);
            int recipeId = recipeEntity.getId();
            recipesEntityList.add(recipeEntity);
            ingredientsList = recipeResponses.get(i).getIngredients();
            for(int j = 0; j< ingredientsList.size(); j++){
                IngredientEntity ingredient = new IngredientEntity(recipeId,(double)(ingredientsList.get(j).getQuantity()),
                        ingredientsList.get(j).getMeasure(), ingredientsList.get(j).getIngredient());
                ingredientsListEntity.add(ingredient);
                mRoomDB.recipeDao().insertIngredients(ingredient);
            }

            steps = recipeResponses.get(i).getSteps();
            for(int j = 0; j< steps.size(); j++){
                StepEntity stepEntity = new StepEntity(recipeId,
                        steps.get(j).getShortDescription(),steps.get(j).getDescription(),
                        steps.get(j).getVideoURL(),steps.get(j).getThumbnailURL());
                stepsListEntity.add(stepEntity);
                mRoomDB.recipeDao().insertSteps(stepEntity);
            }
        }

    }
}
