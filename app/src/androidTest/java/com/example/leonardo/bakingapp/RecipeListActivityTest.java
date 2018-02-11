package com.example.leonardo.bakingapp;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.leonardo.bakingapp.view.RecipeListActivity;
import com.example.leonardo.bakingapp.viewAssertions.AdapterCountEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {


    @Rule
    public ActivityTestRule<RecipeListActivity> activityTestRule = new ActivityTestRule<>(RecipeListActivity.class);

    @Before
    public void registerIdlingResource() {
        IdlingRegistry.getInstance().register(activityTestRule.getActivity().getIdlingResource());
    }

    @After
    public void unregisterIdlingResource(){
        IdlingRegistry.getInstance().unregister(activityTestRule.getActivity().getIdlingResource());
    }

    @Test
    public void testRecipeListLoad(){
        onView(withId(R.id.recyclerView_recipes)).check(new AdapterCountEquals(4));
    }
}
