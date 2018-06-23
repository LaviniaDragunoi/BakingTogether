package com.example.user.bakingtogether;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.user.bakingtogether.UI.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.user.bakingtogether.utils.CustomMatchers.withResourceName;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class RecipeClickTest {
    public static final String RECIPE_NAME = "Nutella Pie";

    @Rule public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickOnRecipe_opensCorrectRecipe(){
        //Find the recipe item that will be clicked on
        onView(allOf(withId(R.id.recipes_recycler_view), hasFocus())).perform(click());
        //Verify if it was opened the recipe with the correct name
      // onView(allOf(withId(R.id.ingredients_list)).check(matches(isDisplayed()));
        testActionBarTitleForScreenOneActivity();
    }


    public void testActionBarTitleForScreenOneActivity() {
        onView(allOf(isDescendantOfA(withResourceName("android:id/action_bar_container")), withText("My Activity")))
                .check(matches(isDisplayed()));
    }
}
