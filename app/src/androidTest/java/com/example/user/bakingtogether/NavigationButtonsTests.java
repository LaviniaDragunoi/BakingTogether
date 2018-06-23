package com.example.user.bakingtogether;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.user.bakingtogether.UI.StepActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class NavigationButtonsTests {


    @Rule public ActivityTestRule<StepActivity> mStepActivityTestRule
            = new ActivityTestRule<>(StepActivity.class);
    @Test
    public void clickNext_IncrementStepId(){
        //Find the button view and perform an click action on it
        onView(allOf(withId(R.id.next_fab), hasFocus())).perform(click());
        //Check if the button does what is expected

    }
}
