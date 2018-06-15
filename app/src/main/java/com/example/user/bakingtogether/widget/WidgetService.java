package com.example.user.bakingtogether.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.user.bakingtogether.AppExecutors;
import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.RecipeDao;
import com.example.user.bakingtogether.DB.RecipeDetails;
import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.TheRepository;
import com.example.user.bakingtogether.data.ApiUtils;

import java.util.ArrayList;
import java.util.List;

public class WidgetService extends RemoteViewsService {
    private List<RecipeEntity> mRecipes = new ArrayList<>();
    private TheRepository repository;


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

            initRecipesGrid();
        }

        @Override
        public void onDataSetChanged() {
            initRecipesGrid();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if(mRecipes == null) return 0;
            return mRecipes.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(),
                    R.layout.grid_item_widget);
            views.setTextViewText(R.id.widget_recipe_name, mRecipes.get(position).getName());
            views.setImageViewResource(R.id.widget_recipe_image, R.drawable.baking_ic_tran);
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

    private void initRecipesGrid() {
        AppRoomDatabase roomDB = AppRoomDatabase.getsInstance(this);
        repository = TheRepository.getsInstance(AppExecutors.getInstance(),
                roomDB, roomDB.recipeDao(), ApiUtils.getRecipeInterfaceResponse());
        mRecipes = repository.getInitialRecipeList().getValue();

    }
}
