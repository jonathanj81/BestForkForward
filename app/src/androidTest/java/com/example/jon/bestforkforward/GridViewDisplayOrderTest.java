package com.example.jon.bestforkforward;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.example.jon.bestforkforward.UI.MainActivity;
import com.example.jon.bestforkforward.UI.MasterFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Observable;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class GridViewDisplayOrderTest {

    public final ActivityTestRule<MainActivity> myActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    private static final Intent MY_ACTIVITY_INTENT = new Intent(InstrumentationRegistry.getTargetContext(), MainActivity.class);

    @Before
    public void setup() {
        myActivityTestRule.launchActivity(MY_ACTIVITY_INTENT);
        MasterFragment fragment = new MasterFragment();
        myActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment).commit();
    }

    @Test
    public void check_gridview_order(){

        String[] desserts = {"Nutella Pie", "Brownies", "Yellow Cake", "Cheesecake"};

        for (int i = 0; i < 4; i++) {
            onData(anything()).inAdapterView(allOf(withId(R.id.recyclerview),
                    withChild(withId(R.id.dessert_name_textview)))).atPosition(i)
                    .onChildView(withId(R.id.dessert_name_textview))
                    .check(matches(withText(desserts[i])));
        }
    }
}
