package com.example.user.bakingtogether.DB;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.user.bakingtogether.data.ApiUtils;
import com.example.user.bakingtogether.data.RecipeApiInterface;
import com.example.user.bakingtogether.data.RecipeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Database(entities = {RecipeEntity.class, IngredientEntity.class, StepEntity.class}, version = 1, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {

     public abstract RecipeDao recipeDao();
     private static final Object LOCK = new Object();
     private static Builder<AppRoomDatabase> sInstance;
     private static final String DATABASE_NAME = "recipe";

     public static AppRoomDatabase getsInstance(Context context){
         if(sInstance == null){
             synchronized (LOCK){
                 sInstance = Room.databaseBuilder(context.getApplicationContext(),
                         AppRoomDatabase.class, AppRoomDatabase.DATABASE_NAME);
             }
         }
        return sInstance.build();
     }
}
