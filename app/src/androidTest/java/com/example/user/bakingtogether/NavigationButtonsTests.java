package com.example.user.bakingtogether;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.user.bakingtogether.UI.StepActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class NavigationButtonsTests {


    @Rule public ActivityTestRule<StepActivity> mStepActivityTestRule
            = new ActivityTestRule<>(StepActivity.class);
    @Test
    public void clickNext_IncrementStepId(){
        //Find the button view and perfom an click action on it
        onView(withId(R.id.next_fab)).perform(click());
        //Check if the button does what is expected

    }
}
