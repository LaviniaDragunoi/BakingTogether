package com.example.user.bakingtogether.widget;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.user.bakingtogether.AppExecutors;
import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.DB.RecipeDao;
import com.example.user.bakingtogether.DB.RecipeDetails;
import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.TheRepository;
import com.example.user.bakingtogether.ViewModel.MainActivityViewModel;
import com.example.user.bakingtogether.ViewModel.MainViewModelFactory;
import com.example.user.bakingtogether.data.ApiUtils;
import com.example.user.bakingtogether.data.Ingredients;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.bakingtogether.UI.DetailsActivity.RECIPE_ID;
import static com.example.user.bakingtogether.UI.recipes.RecipesAdapter.DEFAULT_VALUE;


public class WidgetService extends RemoteViewsService {
    private List<RecipeDetails> mRecipes = new ArrayList<>();
    private List<IngredientEntity> ingredientList = new ArrayList<>();
    private TheRepository repository;
    private String recipeName;
    private Context mContext;


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingRemoteViewFactory(this.getApplicationContext(), intent);
    }

    private class BakingRemoteViewFactory implements RemoteViewsFactory {

        Context mContext;

        public BakingRemoteViewFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            createIngredientList();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if(ingredientList == null) return 0;
            return ingredientList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(),
                    R.layout.ingredient_item_widget);

            views.setTextViewText(R.id.widget_item, getIngredientString(position));

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

   public void createIngredientList() {
       SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
       int recipeId = sp.getInt("RecipeIdSh", DEFAULT_VALUE);
        AppRoomDatabase roomDB = AppRoomDatabase.getsInstance(this);
        repository = TheRepository.getsInstance(AppExecutors.getInstance(),
                roomDB, roomDB.recipeDao(), ApiUtils.getRecipeInterfaceResponse());

        ingredientList = repository.getIngredientsByRecipeIdWidget(recipeId);

    }
    public String getIngredientString(int position){
        return ingredientList.get(position).getQuantity() + " "
                + ingredientList.get(position).getMeasure() + " "
                + ingredientList.get(position).getIngredient();
    }


}
