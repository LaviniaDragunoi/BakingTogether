package com.example.user.bakingtogether.DB;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

/**
 * Room Database is an abstract class that serves as the main access of the database, it includes the list
 * of entities associated with the database within the annotation
 */
@Database(entities = {RecipeEntity.class, IngredientEntity.class, StepEntity.class}, version = 1, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {
    @VisibleForTesting
    public static final String DATABASE_NAME_TESTING = "basic-sample-db";
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    private static final Object LOCK = new Object();
    public static final String DATABASE_NAME = "recipe";
    private static Builder<AppRoomDatabase> sInstance;

    public static AppRoomDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppRoomDatabase.class, AppRoomDatabase.DATABASE_NAME);
            }
        }
        return sInstance.build();
    }

    public abstract RecipeDao recipeDao();

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    @VisibleForTesting
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME_TESTING).exists()) {
            setDatabaseCreated();
        }
    }
@VisibleForTesting
    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

@VisibleForTesting
    private static void insertData(final AppRoomDatabase database, final RecipeEntity products) {
        database.runInTransaction(() -> {
            database.recipeDao().insertRecipe(products);
        });
    }
@VisibleForTesting
    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

    @VisibleForTesting
    public LiveData<Boolean> getDatabaseCreated() {
        if(sInstance != null) mIsDatabaseCreated.postValue(true);
        return mIsDatabaseCreated;
    }
}

