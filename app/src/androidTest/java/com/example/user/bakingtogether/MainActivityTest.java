/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.user.bakingtogether;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.UI.MainActivity;
import com.example.user.bakingtogether.utils.EspressoTestUtil;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;

/**
 * MainActivity testing, learned and taken from https://github.com/googlesamples
 * /android-architecture-components/blob/master/BasicSample/
 */
public class MainActivityTest extends Assert {

    private static final String RECIPE_NAME = "Brownies";
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    public MainActivityTest() {
        // delete the database
        InstrumentationRegistry.getTargetContext().deleteDatabase(AppRoomDatabase.DATABASE_NAME_TESTING);
    }

    @Before
    public void disableRecyclerViewAnimations() {
        // Disable RecyclerView animations
        EspressoTestUtil.disableAnimations(mActivityRule);
    }

    @Before
    public void waitForDbCreation() throws Throwable {
        final CountDownLatch latch = new CountDownLatch(1);
        final LiveData<Boolean> databaseCreated = AppRoomDatabase.getsInstance(
                InstrumentationRegistry.getTargetContext()).getDatabaseCreated();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                databaseCreated.observeForever(new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        if (Boolean.TRUE.equals(aBoolean)) {
                            databaseCreated.removeObserver(this);
                            latch.countDown();
                        }
                    }
                });
            }
        });

        MatcherAssert.assertThat("database should've initialized",
                latch.await(1, TimeUnit.MINUTES), CoreMatchers.is(true));
    }

    @Test
    public void clickOnRecyclerView() {

        // Test if perform click action on RecyclerView
        onView(allOf(withId(R.id.recipes_recycler_view), hasFocus()))
                .perform(click());

        onData(anything()).inAdapterView(allOf(withId(R.id.recipes_recycler_view), hasFocus())).atPosition(1).perform(click());
        onView(withId(R.id.recipe_name)).check(matches(withText(RECIPE_NAME)));

    }
}
