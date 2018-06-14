package com.example.user.bakingtogether.widget;

import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.user.bakingtogether.AppExecutors;
import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.TheRepository;
import com.example.user.bakingtogether.data.ApiUtils;
import com.example.user.bakingtogether.data.RecipeApiInterface;

public class RecipeIntentService extends IntentService {

    private static final int DEFAULT_VALUE = 0;
    private TheRepository repository;
    private static final String ACTION_UPDATE_RECIPE = "com.example.user.bakingtogether.action.update_recipe";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RecipeIntentService(String name) {
        super("RecipeIntentService");

    }
    public static void startActionUpdateRecipe(Context context) {
        Intent intent = new Intent(context, RecipeIntentService.class);
        intent.setAction(ACTION_UPDATE_RECIPE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            final String action = intent.getAction();
            if(ACTION_UPDATE_RECIPE.equals(action)){
                handleActionUpdateRecipe();
            }
        }


    }

    private void handleActionUpdateRecipe() {

    }
}
