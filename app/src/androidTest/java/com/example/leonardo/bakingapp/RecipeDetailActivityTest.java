package com.example.leonardo.bakingapp;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.leonardo.bakingapp.api.entity.Ingredient;
import com.example.leonardo.bakingapp.api.entity.Recipe;
import com.example.leonardo.bakingapp.api.entity.Step;
import com.example.leonardo.bakingapp.view.RecipeDetailActivity;
import com.example.leonardo.bakingapp.viewAssertions.AdapterCountEquals;
import com.example.leonardo.bakingapp.viewMatchers.RecyclerViewMatcher;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    @Rule
    public IntentsTestRule<RecipeDetailActivity> activityTestRule = new IntentsTestRule<>(RecipeDetailActivity.class, true, false);

    private static final Recipe recipe;
    private static final String RECIPE_NAME = "Fake recipe";
    private static final String INGREDIENT_NAME = "Fake ingredient";
    private static final String INGREDIENT_MEASURE = "XXX";
    private static final int INGREDIENT_QUANTITY = 99;
    private static final String STEP_SHORT_DESC = "Fake step short description";
    private static final String STEP_LONG_DESC = "Fake step long description";
    private static final int RECIPE_SERVINGS = 10;

    static {
        recipe = new Recipe();
        recipe.setId(1);
        recipe.setImage(null);
        recipe.setName(RECIPE_NAME);
        recipe.setServings(RECIPE_SERVINGS);
        List<Ingredient> ingredients = new ArrayList<>();
        while (ingredients.size() <= 10){
            ingredients.add(
                    new Ingredient(INGREDIENT_QUANTITY
                            , INGREDIENT_MEASURE
                            , INGREDIENT_NAME + " " + ingredients.size() + 1
                    )
            );
        }
        recipe.setIngredients(ingredients);
        List<Step> steps = new ArrayList<>();
        while(steps.size() < 10){
            int id = steps.size() + 1;
            steps.add(
                    new Step(id
                            , STEP_SHORT_DESC + " " + id
                            , STEP_LONG_DESC + " " + id
                            , null
                            , null)
            );
        }
        recipe.setSteps(steps);
    }

    @Before
    public void launch(){
        Intent launchIntent = new Intent();
        launchIntent.putExtra(RecipeDetailActivity.RECIPE_EXTRA, recipe);
        activityTestRule.launchActivity(launchIntent);

        intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void testFragmentStepListLoad(){
        onView(withId(R.id.recyclerView_recipe_steps)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerView_recipe_steps)).check(new AdapterCountEquals(recipe.getSteps().size() + 1));
    }

    @Test
    public void testOpenFragmentDetail(){
        onView(new RecyclerViewMatcher(R.id.recyclerView_recipe_steps)
            .atPosition(2))
            .perform(click());

        onView(withId(R.id.textView_step_short_description)).check(matches(isDisplayed()));
    }
}
