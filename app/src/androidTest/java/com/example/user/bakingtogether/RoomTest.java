package com.example.user.bakingtogether;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.RecipeDao;
import com.example.user.bakingtogether.DB.RecipeEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class RoomTest {
    private RecipeDao recipeDao;
    private AppRoomDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
       recipeDao = mDb.recipeDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

 /*   @Test
    public void writeInRoomDB() throws Exception {
        RecipeEntity recipeEntity = TestUtil.createUser(3);
        user.setName("george");
        mUserDao.insert(user);
        List<User> byName = recipeDao.("george");
        assertThat(byName.get(0), equalTo(user));
    }
}-*/
    }