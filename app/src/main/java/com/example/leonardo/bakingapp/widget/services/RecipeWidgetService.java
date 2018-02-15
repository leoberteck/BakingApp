package com.example.leonardo.bakingapp.widget.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.leonardo.bakingapp.widget.IngredientsViewFactory;

public class RecipeWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsViewFactory(getApplicationContext(), intent);
    }
}
