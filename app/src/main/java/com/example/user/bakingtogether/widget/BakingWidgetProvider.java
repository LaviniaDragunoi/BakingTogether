package com.example.user.bakingtogether.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.example.user.bakingtogether.R;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int recipeId) {

        // Construct the RemoteViews object
        RemoteViews views = setRemoteAdapter(context);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * Sets the remote adapter used to fill in the grid items
     */
    private static RemoteViews setRemoteAdapter(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);
        views.setRemoteAdapter(R.id.ingredients_list_widget, new Intent(context, WidgetService.class));
        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            WidgetConfigActivity.deleteRecipeIdPref(context, appWidgetId);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            int recipeId = sp.getInt("RecipeIdSh", -1);
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

