package com.example.leonardo.bakingapp;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.leonardo.bakingapp.view.RecipeListActivity;
import com.example.leonardo.bakingapp.viewMatchers.RecyclerViewMatcher;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityShareTest {
    @Rule
    public IntentsTestRule<RecipeListActivity> activityTestRule = new IntentsTestRule<>(RecipeListActivity.class);

    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
        intending(allOf(hasAction(Intent.ACTION_CHOOSER))).respondWith(new ActivityResult(Activity.RESULT_CANCELED, null));
    }

    @Before
    public void registerIdlingResource(){
        IdlingRegistry.getInstance().register(activityTestRule.getActivity().getIdlingResource());
    }

    @After
    public void unregisterIdlingResource(){
        IdlingRegistry.getInstance().unregister(activityTestRule.getActivity().getIdlingResource());
    }

    @Test
    public void testShareRecipe(){
        onView(new RecyclerViewMatcher(R.id.recyclerView_recipes)
                .atPositionOnView(1, R.id.button_share))
                .perform(click());

        intended(allOf(
                hasAction(Intent.ACTION_CHOOSER)
        ));
    }
}
