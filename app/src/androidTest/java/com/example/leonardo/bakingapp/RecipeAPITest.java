package com.example.leonardo.bakingapp;

import com.example.leonardo.bakingapp.api.RecipeAPI;
import com.example.leonardo.bakingapp.api.entity.Recipe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class RecipeAPITest {
    @Test
    public void getAllRecipies_Test() throws Exception {
        List<Recipe> recipes = RecipeAPI.getAllRecipies();
        assertNotNull(recipes);
        assertNotEquals(0, recipes.size());
    }
}
