package com.example.user.bakingtogether;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.RecipeDao;
import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.utils.TestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(AndroidJUnit4.class)
public class RoomTest {
    private RecipeDao recipeDao;
    private AppRoomDatabase mDb;
    private String testText = "NewTestRecipe";
    private static final String RECIPE_NAME = "Nutella Pie";


    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppRoomDatabase.class).build();
       recipeDao = mDb.recipeDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

   @Test
    public void writeInRoomDB() throws Exception {
        RecipeEntity recipeEntity = TestUtil.createRecipeEntity(0, testText, 5, "");
        recipeEntity.setName(testText);
        recipeDao.insertRecipe(recipeEntity);
       RecipeEntity recipe = recipeDao.findRecipeByName(testText);
       assertThat(recipe.getName(), equalTo(recipeEntity.getName()));
    }

    @Test
    public void readFromDao(){
        recipeDao.findRecipeByName(RECIPE_NAME);
    }
}
