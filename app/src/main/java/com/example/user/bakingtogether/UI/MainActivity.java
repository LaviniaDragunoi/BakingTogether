package com.example.user.bakingtogether.UI;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.user.bakingtogether.BuildConfig;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.UI.recipes.RecipesFragment;

import java.lang.reflect.Method;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //Method used for debugging Room Db purposes
    public static void setInMemoryRoomDatabases(SupportSQLiteDatabase... database) {
        if (BuildConfig.DEBUG) {
            try {
                Class<?> debugDB = Class.forName("com.amitshekhar.DebugDB");
                Class[] argTypes = new Class[]{HashMap.class};
                HashMap<String, SupportSQLiteDatabase> inMemoryDatabases = new HashMap<>();
                // set your inMemory databases
                inMemoryDatabases.put("InMemoryOne.db", database[0]);
                Method setRoomInMemoryDatabase = debugDB.getMethod("setInMemoryRoomDatabases", argTypes);
                setRoomInMemoryDatabase.invoke(null, inMemoryDatabases);
            } catch (Exception ignore) { }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Creating  and populate the fragment that will display the recipe list.
        RecipesFragment recipesFragment = new RecipesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.recipes_list, recipesFragment)
                .commit();
        //Method called for debugging Room DB
        setInMemoryRoomDatabases();
    }
}



