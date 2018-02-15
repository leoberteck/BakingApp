package com.example.leonardo.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.leonardo.bakingapp.R;
import com.example.leonardo.bakingapp.api.entity.Ingredient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class IngredientsViewFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final String INGREDIENTS_EXTRA = "INGREDIENTS_EXTRA";
    private static final Gson gson = new GsonBuilder().create();

    private Context context;
    private ArrayList<Ingredient> ingredients;

    public IngredientsViewFactory(Context context, Intent intent) {
        this.context = context;
        Type listType = new TypeToken<ArrayList<Ingredient>>(){}.getType();
        ingredients = gson.fromJson(intent.getStringExtra(INGREDIENTS_EXTRA), listType);
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = ingredients.get(position);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient_item);
        remoteViews.setTextViewText(
            R.id.ingredient_text
            , ingredient.getIngredient()
                + " - " + ingredient.getQuantity()
                + " - " + ingredient.getMeasure()
        );
        return remoteViews;
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDataSetChanged() {}

    @Override
    public void onDestroy() {}

    @Override
    public RemoteViews getLoadingView() {return null;}

    @Override
    public int getViewTypeCount() {return 1;}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public boolean hasStableIds() {return true;}
}
