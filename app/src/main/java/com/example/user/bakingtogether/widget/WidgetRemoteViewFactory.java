package com.example.user.bakingtogether.widget;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.user.bakingtogether.AppExecutors;
import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.TheRepository;
import com.example.user.bakingtogether.data.ApiUtils;

import java.util.ArrayList;
import java.util.List;

public class WidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final int DEFAULT_VALUE = -1 ;
    private final int recipeId;
    private TheRepository repository;
    List<IngredientEntity> ingredientListWidget = new ArrayList<>();
    Context mContext = null;
    private AppRoomDatabase roomDB;


    public WidgetRemoteViewFactory(Context context, Intent intent) {
        recipeId = intent.getIntExtra("RecipeIdWidget", DEFAULT_VALUE);
        if(recipeId == -1) return;
        mContext = context;
    }

    @Override
    public void onCreate() {
        initiateList();

    }

    @Override
    public void onDataSetChanged() {
        initiateList();
    }



    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientListWidget.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_item_widget);
        String ingredient = createIngredientString(position);
        view.setTextViewText(R.id.widget_item, ingredient);

        return view;
    }

    private String createIngredientString(int position) {
        return ingredientListWidget.get(position).getQuantity() + " "
                + ingredientListWidget.get(position).getMeasure() + " "
                + ingredientListWidget.get(position).getIngredient();
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

    private void initiateList() {
        ingredientListWidget.clear();
        repository = TheRepository.getsInstance(AppExecutors.getInstance(),
                roomDB, roomDB.recipeDao(), ApiUtils.getRecipeInterfaceResponse());
        ingredientListWidget = repository.getIngredientsByRecipeIdWidget(recipeId);
    }
}
