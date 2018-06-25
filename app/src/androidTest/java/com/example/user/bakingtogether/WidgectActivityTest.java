package com.example.user.bakingtogether;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.user.bakingtogether.widget.WidgetConfigActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class WidgectActivityTest {
    private static final String RECIPE_NAME = "Choose your recipe";
    @Rule public ActivityTestRule<WidgetConfigActivity> mActivityTestRule =
            new ActivityTestRule<>(WidgetConfigActivity.class);

    @Test
    public void clickOnSpinner(){
        onView(allOf(withId(R.id.recipe_spinner))).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(RECIPE_NAME))).perform(click());
        onView(withId(R.id.recipe_spinner)).check(matches(withSpinnerText(containsString(RECIPE_NAME))));

    }
}
