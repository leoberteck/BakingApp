package com.example.leonardo.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.leonardo.bakingapp.R;
import com.example.leonardo.bakingapp.api.RecipeAPI;
import com.example.leonardo.bakingapp.api.entity.Recipe;
import com.example.leonardo.bakingapp.util.ResourceUtils;
import com.example.leonardo.bakingapp.view.RecipeDetailActivity;
import com.example.leonardo.bakingapp.widget.services.RecipeWidgetService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    public static final String RECIPE_INDEX_EXTRA = "RECIPE_INDEX_EXTRA";
    private static final Gson gson = new GsonBuilder().create();
    private static List<Recipe> recipeList;

    private int recipeIndex = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.hasExtra(RECIPE_INDEX_EXTRA)){
            recipeIndex = intent.getIntExtra(RECIPE_INDEX_EXTRA, 0);
            Log.i(RecipeWidget.class.getSimpleName(), "recipeIndex set to : " + recipeIndex);
        }
        super.onReceive(context, intent);
    }

    static void updateAppWidget(
            final Context context
            , final AppWidgetManager appWidgetManager
            , final int appWidgetId
            , final int recipeIndex) {

        if(recipeList == null || recipeList.size() == 0){
            RecipeAPI.getAllRecipiesAsync(new RecipeAPI.OnTaskFinishListener() {
                @Override
                public void onTaskFinish(List<Recipe> recipeList) {
                    RecipeWidget.recipeList = recipeList;
                    // Construct the RemoteViews object
                    Recipe recipe = null;
                    if(recipeList != null && recipeList.size() > 0){
                        recipe = recipeList.get(recipeIndex);
                    }
                    updateAppWidget(context, appWidgetManager, appWidgetId, recipeIndex, recipe);
                }
            });
        } else {
            Recipe recipe = recipeList.get(recipeIndex);
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeIndex, recipe);
        }
    }

    static void updateAppWidget(final Context context
            , final AppWidgetManager appWidgetManager
            , final int appWidgetId
            , final int recipeIndex
            , final Recipe recipe){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        ResourceUtils resourceUtils = new ResourceUtils(context);
        if(recipe != null){

            views.setTextViewText(R.id.recipeName, recipe.getName());

            Intent viewsFactoryIntent=new Intent(context, RecipeWidgetService.class);
            viewsFactoryIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            //Sorry... this just does not work with Parcelable, I tried.
            viewsFactoryIntent.putExtra(IngredientsViewFactory.INGREDIENTS_EXTRA, gson.toJson(recipe.getIngredients()));
            viewsFactoryIntent.setData(Uri.parse(viewsFactoryIntent.toUri(Intent.URI_INTENT_SCHEME)));
            views.setRemoteAdapter(R.id.ingredientsList, viewsFactoryIntent);

            Intent clickIntent=new Intent(context, RecipeDetailActivity.class);
            clickIntent.putExtra(RecipeDetailActivity.RECIPE_EXTRA, recipe);
            PendingIntent clickPI= PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.ingredientsList, clickPI);

            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.RECIPE_EXTRA, recipe);
            PendingIntent recipeClickPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.button_open_recipe, recipeClickPendingIntent);

            int toGo = recipeIndex + 1;
            if(recipeIndex == recipeList.size() - 1){
                toGo = 0;
            }

            Intent intentSync = new Intent(context, RecipeWidget.class);
            intentSync.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intentSync.putExtra(RECIPE_INDEX_EXTRA, toGo);
            intentSync.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
            PendingIntent pendingSync = PendingIntent.getBroadcast(context,0, intentSync, PendingIntent.FLAG_ONE_SHOT);
            views.setOnClickPendingIntent(R.id.button_next, pendingSync);

        } else {
            views.setTextViewText(R.id.recipeName, resourceUtils.getString(R.string.err_load_recipe_no_internet));
            views.setViewVisibility(R.id.ingredientsList, View.GONE);
            views.setViewVisibility(R.id.controlsLayout, View.GONE);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeIndex);
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

