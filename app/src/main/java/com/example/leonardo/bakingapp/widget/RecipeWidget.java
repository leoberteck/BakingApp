package com.example.leonardo.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.leonardo.bakingapp.R;
import com.example.leonardo.bakingapp.api.RecipeAPI;
import com.example.leonardo.bakingapp.api.entity.Ingredient;
import com.example.leonardo.bakingapp.api.entity.Recipe;
import com.example.leonardo.bakingapp.util.ResourceUtils;
import com.example.leonardo.bakingapp.view.RecipeDetailActivity;

import java.util.List;
import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    private static List<Recipe> recipeList;
    private static Recipe currentRecipe;
    private static final Random rand = new Random();

    static void updateAppWidget(
            final Context context
            , final AppWidgetManager appWidgetManager
            , final int appWidgetId) {

        if(recipeList == null || recipeList.size() == 0){
            RecipeAPI.getAllRecipiesAsync(new RecipeAPI.OnTaskFinishListener() {
                @Override
                public void onTaskFinish(List<Recipe> recipeList) {
                    RecipeWidget.recipeList = recipeList;
                    // Construct the RemoteViews object
                    Recipe recipe = null;
                    if(recipeList != null && recipeList.size() > 0){
                        recipe = recipeList.get(randInt(0, recipeList.size()-1));
                    }
                    updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
                }
            });
        } else {
            Recipe recipe = recipeList.get(randInt(0, recipeList.size()-1));
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    static void updateAppWidget(final Context context
            , final AppWidgetManager appWidgetManager
            , final int appWidgetId
            , final Recipe recipe){
        currentRecipe = recipe;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        ResourceUtils resourceUtils = new ResourceUtils(context);
        if(currentRecipe != null){
            views.setTextViewText(R.id.recipeText, getRecipeText(currentRecipe, resourceUtils));
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.RECIPE_EXTRA, currentRecipe);
            PendingIntent recipeClickPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.recipeText, recipeClickPendingIntent);
        } else {
            views.setTextViewText(R.id.recipeText, resourceUtils.getString(R.string.err_load_recipe_no_internet));
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static String getRecipeText(Recipe recipe, ResourceUtils resourceUtils){
        StringBuilder recipeText = new StringBuilder();
        recipeText.append(recipe.getName()).append("\n")
                .append("\n")
                .append(resourceUtils.getString(R.string.ingredients)).append("\n")
                .append("\n");
        for (Ingredient ingredient : recipe.getIngredients()) {
            recipeText.append(ingredient.getIngredient())
                    .append(" - ")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getMeasure())
                    .append("\n");
        }
        return recipeText.toString();
    }

    public static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

